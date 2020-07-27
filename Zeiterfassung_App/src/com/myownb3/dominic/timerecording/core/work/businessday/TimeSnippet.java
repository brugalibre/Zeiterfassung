/**
 * 
 */
package com.myownb3.dominic.timerecording.core.work.businessday;

import java.text.ParseException;
import java.util.Date;

import com.myownb3.dominic.timerecording.core.callbackhandler.TimeSnippedChangedCallbackHandler;
import com.myownb3.dominic.timerecording.core.callbackhandler.impl.ChangedValue;
import com.myownb3.dominic.timerecording.core.work.date.Time;
import com.myownb3.dominic.timerecording.core.work.date.TimeType;
import com.myownb3.dominic.timerecording.core.work.date.TimeType.TIME_TYPE;
import com.myownb3.dominic.util.parser.DateParser;
import com.myownb3.dominic.util.parser.NumberFormat;
import com.myownb3.dominic.util.utils.StringUtil;

/**
 * Defines a period, that begin with the {@link #beginTimeStamp} and ends with
 * the {@link #endTimeStamp}. The time between these two time-stamps can be
 * calculated by invoking the {@link #getDuration()} or
 * {@link #getDuration(TIME_TYPE)} method.
 * 
 * @author Dominic
 *
 */
public class TimeSnippet {
   private Date date; // the date of the day, when this TimeSnippet took place
   private Time beginTimeStamp;
   private Time endTimeStamp;
   private TimeSnippedChangedCallbackHandler callbackHandler;

   public TimeSnippet(Date date) {
      this.date = date;
   }

   /**
    * Trys to parse the given end time stamp (as String) and summs this value up to
    * the given begin Time Stamp
    * 
    * @param newEndAsString
    *        the new End-time stamp as String
    * @throws NumberFormatException
    *         if there goes anything wrong while parsing
    */
   public void addAdditionallyTime(String newEndAsString) {
      float additionallyTime = NumberFormat.parseFloat(newEndAsString) - getDuration();

      long additionallyDuration = (long) (Time.getTimeRefactorValue(TimeType.DEFAULT) * additionallyTime);
      setEndTimeStamp(new Time(getEndTimeStamp().getTime() + additionallyDuration));
   }

   /**
    * Trys to parse a new {@link Date} from the given timestamp value and sets this
    * value as new begin-time stamp
    * 
    * @param newTimeStampValue
    *        the new begin-time-stamp as String
    */
   public void updateAndSetBeginTimeStamp(String newTimeStampValue) {
      String convertedTimeStampValue = convertInput(newTimeStampValue);
      if (!StringUtil.isEqual(convertedTimeStampValue, getBeginTimeStampRep())) {
         Time time = DateParser.getTime(newTimeStampValue, getBeginTimeStamp());
         setBeginTimeStamp(new Time(time));
      }
   }

   /**
    * Trys to parse a new {@link Date} from the given timestamp value and sets this
    * value as new end-time stamp
    * 
    * @param newTimeStampValue
    *        the new begin-time-stamp as String
    */
   public void updateAndSetEndTimeStamp(String newTimeStampValue) {
      String convertedTimeStampValue = convertInput(newTimeStampValue);
      if (!StringUtil.isEqual(convertedTimeStampValue, getEndTimeStampRep())) {
         Time time = DateParser.getTime(newTimeStampValue, getEndTimeStamp());
         setEndTimeStamp(new Time(time));
      }
   }

   private String convertInput(String newTimeStampValue) {
      try {
         return DateParser.convertInput(newTimeStampValue);
      } catch (ParseException e) {
         // ignore
      }
      return newTimeStampValue;
   }

   /**
    * @return the amount of minutes between the start, and end-point
    */
   public float getDuration() {
      return getDuration(TimeType.DEFAULT);
   }

   public String getDurationRep() {
      return NumberFormat.format(getDuration());
   }

   /**
    * Return the amount of minutes between the start, and end-point. If there is no
    * ent-point yet, a new end-point at the most current now is created
    * 
    * @return the amount of minutes between the start, and end-point
    */
   public float getDuration(TIME_TYPE type) {
      Time endTimeSnippet = (endTimeStamp != null ? endTimeStamp : new Time(System.currentTimeMillis()));
      long time = endTimeSnippet.getTime() - getBeginTimeStamp().getTime();
      int factor = (int) Time.getTimeRefactorValue(type);

      return NumberFormat.parse(time, factor);
   }

   private void notifyCallbackHandler(ChangedValue changedValue) {
      if (callbackHandler != null) {
         callbackHandler.handleTimeSnippedChanged(changedValue);
      }
   }

   public Date getDate() {
      return date;
   }

   public void setBeginTimeStamp(Time beginTimeStamp) {
      this.beginTimeStamp = beginTimeStamp;
      notifyCallbackHandler(ChangedValue.of(-1, getBeginTimeStampRep(), ValueTypes.BEGIN));
   }

   public void setEndTimeStamp(Time endTimeStamp) {
      this.endTimeStamp = endTimeStamp;
      notifyCallbackHandler(ChangedValue.of(-1, getEndTimeStampRep(), ValueTypes.END));
   }

   public Time getEndTimeStamp() {
      return endTimeStamp;
   }

   public Time getBeginTimeStamp() {
      return beginTimeStamp;
   }

   public String getEndTimeStampRep() {
      return String.valueOf(endTimeStamp);
   }

   public String getBeginTimeStampRep() {
      return String.valueOf(beginTimeStamp);
   }

   public final void setCallbackHandler(TimeSnippedChangedCallbackHandler callbackHandler) {
      this.callbackHandler = callbackHandler;
   }

   /**
    * Creates a new {@link TimeSnippet} with the given {@link Time} as a begin time
    * stamp. The duration of this new created {@link TimeSnippet} is the total
    * duration of this current TimeSnipped divided by the given divisor
    * 
    * @param beginTimeStamp
    *        the begin time stamp
    * @param divisor
    *        the divisor
    * @return a new created {@link TimeSnippet}
    */
   public TimeSnippet createTimeStampForIncrement(Time beginTimeStamp, int divisor) {

      float totalDuration = getDuration(TIME_TYPE.MILI_SEC);
      long newDuration4Increment = (long) (totalDuration / divisor);

      TimeSnippet currentTimeSnippet = new TimeSnippet(date);
      Time currentEndTimeStamp = new Time(newDuration4Increment + beginTimeStamp.getTime());
      currentTimeSnippet.setBeginTimeStamp(beginTimeStamp);
      currentTimeSnippet.setEndTimeStamp(currentEndTimeStamp);
      return currentTimeSnippet;
   }

   /**
    * Create new {@link TimeSnippet} for the given {@link Date} and begin,- an end
    * time stamp as String values
    * 
    * @param date
    *        the {@link Date}
    * @param beginValue
    *        the begin time stamp as String
    * @param endValue
    *        the end time stamp as String
    * @return a new {@link TimeSnippet}
    * @throws ParseException
    *         if the {@code #beginValue} or {@code #endValue}
    *         could'nt be parsed
    */
   public static TimeSnippet createTimeSnippet(Date date, String beginValue, String endValue) throws ParseException {
      TimeSnippet timeSnippet = new TimeSnippet(date);
      timeSnippet.setBeginTimeStamp(DateParser.getTime(beginValue, date));
      timeSnippet.setEndTimeStamp(DateParser.getTime(endValue, date));
      return timeSnippet;
   }

   /**
    * Creates a copy of the given timeSnippet
    * 
    * @param otherTimeSnippet
    *        the {@link TimeSnippet} to create a copy of
    * @return a copy of the given timeSnippet
    */
   public static TimeSnippet of(TimeSnippet otherTimeSnippet) {
      if (otherTimeSnippet == null) {
         return null;
      }
      TimeSnippet timeSnippet = new TimeSnippet(otherTimeSnippet.getDate());
      timeSnippet.setBeginTimeStamp(otherTimeSnippet.getBeginTimeStamp());
      timeSnippet.setEndTimeStamp(otherTimeSnippet.getEndTimeStamp());
      return timeSnippet;
   }
}
