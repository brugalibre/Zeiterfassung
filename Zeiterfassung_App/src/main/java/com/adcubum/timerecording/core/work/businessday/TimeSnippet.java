/**
 * 
 */
package com.adcubum.timerecording.core.work.businessday;

import static java.util.Objects.nonNull;
import static java.util.Objects.requireNonNull;

import java.text.ParseException;
import java.util.Date;

import com.adcubum.timerecording.core.work.businessday.update.callback.TimeSnippedChangedCallbackHandler;
import com.adcubum.timerecording.core.work.businessday.update.callback.impl.ChangedValue;
import com.adcubum.timerecording.work.date.Time;
import com.adcubum.timerecording.work.date.TimeFactory;
import com.adcubum.timerecording.work.date.TimeType;
import com.adcubum.timerecording.work.date.TimeType.TIME_TYPE;
import com.adcubum.util.parser.DateParser;
import com.adcubum.util.parser.NumberFormat;
import com.adcubum.util.utils.StringUtil;

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
   /** String literal for representing a <code>null</code> value for either begin or end TimeStamp */
   public static final String NULL_TIME_REP = "-";
   private Date date; // the date of the day, when this TimeSnippet took place
   private Time beginTimeStamp;
   private Time endTimeStamp;
   private TimeSnippedChangedCallbackHandler callbackHandler;

   public TimeSnippet(Date date) {
      this.date = date;
   }

   public TimeSnippet(Time time) {
      this.date = new Date(time.getTime());
   }

   /**
    * Creates a copy of the given {@link TimeSnippet}
    * 
    * @param otherTimeSnippet
    *        the other {@link TimeSnippet} to create a copy of
    */
   public TimeSnippet(TimeSnippet otherTimeSnippet) {
      requireNonNull(otherTimeSnippet);
      this.date = otherTimeSnippet.date;
      this.beginTimeStamp = otherTimeSnippet.beginTimeStamp;
      this.endTimeStamp = otherTimeSnippet.endTimeStamp;
      this.callbackHandler = otherTimeSnippet.callbackHandler;
   }

   /**
    * Tries to parse the given end time stamp (as String) and sums this value up to
    * the given begin Time Stamp
    * 
    * @param amountOfTime2Add
    *        the amount of time to add as String
    * @throws NumberFormatException
    *         if there goes anything wrong while parsing
    */
   public void addAdditionallyTime(String amountOfTime2Add) {
      float additionallyTime = NumberFormat.parseFloat(amountOfTime2Add) - getDuration();

      long additionallyDuration = (long) (Time.getTimeRefactorValue(TimeType.DEFAULT) * additionallyTime);
      setEndTimeStamp(TimeFactory.createNew(getEndTimeStamp().getTime() + additionallyDuration));
   }

   /**
    * Tries to parse a new {@link Date} from the given timestamp value and sets this
    * value as new begin-time stamp
    * 
    * @param newTimeStampValue
    *        the new begin-time-stamp as String
    * @param negativeDurationNOK
    *        <code>true</code> if a resulting negative duration is not okay and thus the begin time stamp is not changed
    */
   public void updateAndSetBeginTimeStamp(String newTimeStampValue, boolean negativeDurationNOK) {
      String convertedTimeStampValue = convertInput(newTimeStampValue);
      if (!StringUtil.isEqual(convertedTimeStampValue, getBeginTimeStampRep())) {
         Time time = DateParser.getTime(newTimeStampValue, getBeginTimeStamp());
         if (getEndTimeStamp().getTime() - time.getTime() < 0 && negativeDurationNOK) {
            return;
         }
         setBeginTimeStamp(TimeFactory.createNew(time));
      }
   }

   /**
    * Tries to parse a new {@link Date} from the given timestamp value and sets this
    * value as new end-time stamp
    * 
    * @param newTimeStampValue
    *        the new begin-time-stamp as String
    * @param negativeDurationNOK
    *        <code>true</code> if a resulting negative duration is not okay and thus the end time stampt is not changed
    */
   public void updateAndSetEndTimeStamp(String newTimeStampValue, boolean negativeDurationNOK) {
      String convertedTimeStampValue = convertInput(newTimeStampValue);
      if (!StringUtil.isEqual(convertedTimeStampValue, getEndTimeStampRep())) {
         Time time = DateParser.getTime(newTimeStampValue, getEndTimeStamp());
         if (time.getTime() - getBeginTimeStamp().getTime() < 0 && negativeDurationNOK) {
            return;
         }
         setEndTimeStamp(TimeFactory.createNew(time));
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
    * end-point yet, a new end-point at the most current now is created
    * 
    * @return the amount of minutes between the start, and end-point
    */
   public float getDuration(TIME_TYPE type) {
      long now = System.currentTimeMillis();
      Time endTimeSnippet = (endTimeStamp != null ? endTimeStamp : TimeFactory.createNew(now));
      Time tmpBeginTimeStamp = (beginTimeStamp != null ? beginTimeStamp : TimeFactory.createNew(now));
      long time = endTimeSnippet.getTime() - tmpBeginTimeStamp.getTime();
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
      Time currentEndTimeStamp = TimeFactory.createNew(newDuration4Increment + beginTimeStamp.getTime());
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
    * Creates a copy of the given {@link TimeSnippet}. The other {@link TimeSnippet} may be null
    * 
    * @param otherTimeSnippet
    *        the {@link TimeSnippet} to create a copy of
    * @return a copy of the given {@link TimeSnippet} or <code>null</code> if the given {@link TimeSnippet} is null
    */
   public static TimeSnippet createTimeSnippet(TimeSnippet otherTimeSnippet) {
      if (nonNull(otherTimeSnippet)) {
         return new TimeSnippet(otherTimeSnippet);
      }
      return null;
   }
}
