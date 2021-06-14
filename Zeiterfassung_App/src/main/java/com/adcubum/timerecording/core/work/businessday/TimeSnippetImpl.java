/**
 * 
 */
package com.adcubum.timerecording.core.work.businessday;

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
public class TimeSnippetImpl implements TimeSnippet {
   private Date date; // the date of the day, when this TimeSnippet took place
   private Time beginTimeStamp;
   private Time endTimeStamp;
   private TimeSnippedChangedCallbackHandler callbackHandler;

   public TimeSnippetImpl(Date date) {
      this.date = date;
   }

   public TimeSnippetImpl(Time time) {
      this.date = new Date(time.getTime());
   }

   /**
    * Creates a copy of the given {@link TimeSnippet}
    * 
    * @param otherTimeSnippet
    *        the other {@link TimeSnippet} to create a copy of
    */
   public TimeSnippetImpl(TimeSnippet otherTimeSnippet) {
      requireNonNull(otherTimeSnippet);
      this.date = otherTimeSnippet.getDate();
      this.beginTimeStamp = otherTimeSnippet.getBeginTimeStamp();
      this.endTimeStamp = otherTimeSnippet.getEndTimeStamp();
      this.callbackHandler = otherTimeSnippet.getCallbackHandler();
   }

   @Override
   public void addAdditionallyTime(String amountOfTime2Add) {
      float additionallyTime = NumberFormat.parseFloat(amountOfTime2Add) - getDuration();

      long additionallyDuration = (long) (Time.getTimeRefactorValue(TimeType.DEFAULT) * additionallyTime);
      setEndTimeStamp(TimeFactory.createNew(getEndTimeStamp().getTime() + additionallyDuration));
   }

   @Override
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

   @Override
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

   @Override
   public float getDuration() {
      return getDuration(TimeType.DEFAULT);
   }

   @Override
   public String getDurationRep() {
      return NumberFormat.format(getDuration());
   }

   @Override
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

   @Override
   public Date getDate() {
      return date;
   }

   @Override
   public void setBeginTimeStamp(Time beginTimeStamp) {
      this.beginTimeStamp = beginTimeStamp;
      notifyCallbackHandler(ChangedValue.of(-1, getBeginTimeStampRep(), ValueTypes.BEGIN));
   }

   @Override
   public void setEndTimeStamp(Time endTimeStamp) {
      this.endTimeStamp = endTimeStamp;
      notifyCallbackHandler(ChangedValue.of(-1, getEndTimeStampRep(), ValueTypes.END));
   }

   @Override
   public Time getEndTimeStamp() {
      return endTimeStamp;
   }

   @Override
   public Time getBeginTimeStamp() {
      return beginTimeStamp;
   }

   @Override
   public String getEndTimeStampRep() {
      return String.valueOf(endTimeStamp);
   }

   @Override
   public String getBeginTimeStampRep() {
      return String.valueOf(beginTimeStamp);
   }

   @Override
   public TimeSnippedChangedCallbackHandler getCallbackHandler() {
      return callbackHandler;
   }

   @Override
   public final void setCallbackHandler(TimeSnippedChangedCallbackHandler callbackHandler) {
      this.callbackHandler = callbackHandler;
   }

   @Override
   public TimeSnippet createTimeStampForIncrement(Time beginTimeStamp, int divisor) {
      float totalDuration = getDuration(TIME_TYPE.MILI_SEC);
      long newDuration4Increment = (long) (totalDuration / divisor);

      TimeSnippet currentTimeSnippet = TimeSnippetFactory.createNew(date);
      Time currentEndTimeStamp = TimeFactory.createNew(newDuration4Increment + beginTimeStamp.getTime());
      currentTimeSnippet.setBeginTimeStamp(beginTimeStamp);
      currentTimeSnippet.setEndTimeStamp(currentEndTimeStamp);
      return currentTimeSnippet;
   }
}
