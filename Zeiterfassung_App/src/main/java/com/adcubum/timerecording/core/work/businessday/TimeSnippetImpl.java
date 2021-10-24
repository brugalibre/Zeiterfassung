/**
 * 
 */
package com.adcubum.timerecording.core.work.businessday;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static java.util.Objects.requireNonNull;

import java.text.ParseException;
import java.util.UUID;

import com.adcubum.timerecording.core.work.businessday.update.callback.TimeSnippedChangedCallbackHandler;
import com.adcubum.timerecording.core.work.businessday.update.callback.impl.ChangedValue;
import com.adcubum.timerecording.work.date.DateTime;
import com.adcubum.timerecording.work.date.DateTimeFactory;
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
   private UUID id;
   private DateTime beginTimeStamp;
   private DateTime endTimeStamp;
   private TimeSnippedChangedCallbackHandler callbackHandler;

   protected TimeSnippetImpl(UUID id, Long beginTimeStampValue, Long endTimeStampValue) {
      this.id = id;
      requireNonNull(beginTimeStampValue, "A TimeSnippetImpl needs a begin time stamp!");
      setBeginTimeStamp(DateTimeFactory.createNew(beginTimeStampValue));
      if (nonNull(endTimeStampValue)) {
         setEndTimeStamp(DateTimeFactory.createNew(endTimeStampValue));
      }
   }

   /**
    * Creates a new and empty {@link TimeSnippetImpl}
    */
   protected TimeSnippetImpl() {
      // nothing to do
   }

   /**
    * Creates a copy of the given {@link TimeSnippet}
    * 
    * @param otherTimeSnippet
    *        the other {@link TimeSnippet} to create a copy of
    */
   protected TimeSnippetImpl(TimeSnippet otherTimeSnippet) {
      requireNonNull(otherTimeSnippet);
      this.beginTimeStamp = otherTimeSnippet.getBeginTimeStamp();
      this.endTimeStamp = otherTimeSnippet.getEndTimeStamp();
      this.callbackHandler = otherTimeSnippet.getCallbackHandler();
      this.id = null;// since we create a new one, we need also a new id
   }

   @Override
   public void addAdditionallyTime(String amountOfTime2Add) {
      float additionallyTime = NumberFormat.parseFloat(amountOfTime2Add) - getDuration();

      long additionallyDuration = (long) (DateTime.getTimeRefactorValue(TimeType.DEFAULT) * additionallyTime);
      setEndTimeStamp(DateTimeFactory.createNew(getEndTimeStamp().getTime() + additionallyDuration));
   }

   @Override
   public void updateAndSetBeginTimeStamp(String newTimeStampValue, boolean negativeDurationNOK) {
      String convertedTimeStampValue = convertInput(newTimeStampValue);
      if (!StringUtil.isEqual(convertedTimeStampValue, getBeginTimeStampRep())) {
         DateTime time = DateParser.getTime(newTimeStampValue, getBeginTimeStamp());
         if (getEndTimeStamp().getTime() - time.getTime() < 0 && negativeDurationNOK) {
            return;
         }
         setBeginTimeStamp(DateTimeFactory.createNew(time));
      }
   }

   @Override
   public void updateAndSetEndTimeStamp(String newTimeStampValue, boolean negativeDurationNOK) {
      String convertedTimeStampValue = convertInput(newTimeStampValue);
      if (!StringUtil.isEqual(convertedTimeStampValue, getEndTimeStampRep())) {
         DateTime time = DateParser.getTime(newTimeStampValue, getEndTimeStamp());
         if (time.getTime() - getBeginTimeStamp().getTime() < 0 && negativeDurationNOK) {
            return;
         }
         setEndTimeStamp(DateTimeFactory.createNew(time));
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
      DateTime endTimeSnippet = (endTimeStamp != null ? endTimeStamp : DateTimeFactory.createNew(now));
      DateTime tmpBeginTimeStamp = (beginTimeStamp != null ? beginTimeStamp : DateTimeFactory.createNew(now));
      long time = endTimeSnippet.getTime() - tmpBeginTimeStamp.getTime();
      int factor = (int) DateTime.getTimeRefactorValue(type);

      return NumberFormat.parse(time, factor);
   }

   private void notifyCallbackHandler(ChangedValue changedValue) {
      if (callbackHandler != null) {
         callbackHandler.handleTimeSnippedChanged(changedValue);
      }
   }

   @Override
   public DateTime getDateTime() {
      return nonNull(beginTimeStamp) ? beginTimeStamp : DateTimeFactory.createNew();
   }

   @Override
   public void setBeginTimeStamp(DateTime beginTimeStamp) {
      this.beginTimeStamp = beginTimeStamp;
      notifyCallbackHandler(ChangedValue.of(getBeginTimeStampRep(), ValueTypes.BEGIN));
   }

   @Override
   public void setEndTimeStamp(DateTime endTimeStamp) {
      this.endTimeStamp = endTimeStamp;
      notifyCallbackHandler(ChangedValue.of(getEndTimeStampRep(), ValueTypes.END));
   }

   @Override
   public DateTime getEndTimeStamp() {
      return endTimeStamp;
   }

   @Override
   public DateTime getBeginTimeStamp() {
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
   public UUID getId() {
      return id;
   }

   @Override
   public final void setCallbackHandler(TimeSnippedChangedCallbackHandler callbackHandler) {
      this.callbackHandler = callbackHandler;
   }

   @Override
   public TimeSnippet createTimeStampForIncrement(DateTime beginTimeStamp, int divisor) {
      float totalDuration = getDuration(TIME_TYPE.MILI_SEC);
      long newDuration4Increment = (long) (totalDuration / divisor);
      return new TimeSnippetBuilder()
            .withBeginTimeStamp(beginTimeStamp.getTime())
            .withEndTimeStamp(newDuration4Increment + beginTimeStamp.getTime())
            .build();
   }

   @Override
   public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + ((beginTimeStamp == null) ? 0 : beginTimeStamp.hashCode());
      result = prime * result + ((endTimeStamp == null) ? 0 : endTimeStamp.hashCode());
      return result;
   }

   @Override
   public boolean equals(Object obj) {
      if (this == obj)
         return true;
      if (obj == null)
         return false;
      if (getClass() != obj.getClass())
         return false;
      TimeSnippetImpl other = (TimeSnippetImpl) obj;
      if (beginTimeStamp == null) {
         if (other.beginTimeStamp != null)
            return false;
      } else if (!beginTimeStamp.equals(other.beginTimeStamp))
         return false;
      if (endTimeStamp == null) {
         if (other.endTimeStamp != null)
            return false;
      } else if (!endTimeStamp.equals(other.endTimeStamp))
         return false;
      return true;
   }



   public static final class TimeSnippetBuilder {

      private long beginTimeStampValue;
      private long endTimeStampValue;
      private DateTime beginTime;
      private DateTime endTime;

      private TimeSnippetBuilder() {
         // private
      }

      public static TimeSnippetBuilder of() {
         return new TimeSnippetBuilder();
      }

      public TimeSnippetBuilder withBeginTimeStamp(long beginTimeStampValue) {
         this.beginTimeStampValue = beginTimeStampValue;
         return this;
      }

      public TimeSnippetBuilder withEndTimeStamp(long endTimeStampValue) {
         this.endTimeStampValue = endTimeStampValue;
         return this;
      }

      public TimeSnippetBuilder withBeginTime(DateTime beginTime) {
         this.beginTime = beginTime;
         return this;
      }

      public TimeSnippetBuilder withEndTime(DateTime endTime) {
         this.endTime = endTime;
         return this;
      }

      public TimeSnippetImpl build() {
         TimeSnippetImpl timeSnippetImpl = new TimeSnippetImpl();
         if (isNull(beginTime)) {
            timeSnippetImpl.setBeginTimeStamp(DateTimeFactory.createNew(beginTimeStampValue));
         } else {
            timeSnippetImpl.setBeginTimeStamp(beginTime);
         }
         if (isNull(endTime)) {
            timeSnippetImpl.setEndTimeStamp(DateTimeFactory.createNew(endTimeStampValue));
         } else {
            timeSnippetImpl.setEndTimeStamp(endTime);
         }
         return timeSnippetImpl;
      }

   }
}
