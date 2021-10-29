package com.adcubum.timerecording.core.work.businessday;

import java.util.Date;

import com.adcubum.timerecording.core.work.businessday.model.common.DomainModel;
import com.adcubum.timerecording.work.date.DateTime;
import com.adcubum.timerecording.work.date.TimeType.TIME_TYPE;

public interface TimeSnippet extends DomainModel {
   /** String literal for representing a <code>null</code> value for either begin or end TimeStamp */
   public static final String NULL_TIME_REP = "-";

   /**
    * Tries to parse the given end time stamp (as String) and sums this value up to
    * the given begin Time Stamp
    * 
    * @param amountOfTime2Add
    *        the amount of time to add as String
    * @throws NumberFormatException
    *         if there goes anything wrong while parsing
    * @return a new, changed instance, copied from this {@link TimeSnippet}
    */
   TimeSnippet addAdditionallyTime(String amountOfTime2Add);

   /**
    * Tries to parse a new {@link Date} from the given timestamp value and sets this
    * value as new begin-time stamp
    * 
    * @param newTimeStampValue
    *        the new begin-time-stamp as String
    * @param negativeDurationNOK
    *        <code>true</code> if a resulting negative duration is not okay and thus the begin time stamp is not changed
    * @return a new, changed instance, copied from this {@link TimeSnippet}
    */
   TimeSnippet updateAndSetBeginTimeStamp(String newTimeStampValue, boolean negativeDurationNOK);

   /**
    * Tries to parse a new {@link Date} from the given timestamp value and sets this
    * value as new end-time stamp
    * 
    * @param newTimeStampValue
    *        the new begin-time-stamp as String
    * @param negativeDurationNOK
    *        <code>true</code> if a resulting negative duration is not okay and thus the end time stampt is not changed
    * @return a new, changed instance, copied from this {@link TimeSnippet}
    */
   TimeSnippet updateAndSetEndTimeStamp(String newTimeStampValue, boolean negativeDurationNOK);

   /**
    * Creates a new {@link TimeSnippet} with the given {@link DateTime} as a begin time
    * stamp. The duration of this new created {@link TimeSnippet} is the total
    * duration of this current TimeSnipped divided by the given divisor
    * 
    * @param beginTimeStamp
    *        the begin time stamp
    * @param divisor
    *        the divisor
    * @return a new created {@link TimeSnippet}
    */
   TimeSnippet createTimeStampForIncrement(DateTime beginTimeStamp, int divisor);

   /**
    * Sets a new value for its begin-timestamp
    * 
    * @param beginTimeStamp
    *        the new begin timestamp
    * @return a new, changed instance, copied from this {@link TimeSnippet}
    */
   TimeSnippet setBeginTimeStamp(DateTime beginTimeStamp);

   /**
    * Sets a new value for its begin-timestamp
    * 
    * @param beginTimeStamp
    *        the new begin timestamp
    * @return a new, changed instance, copied from this {@link TimeSnippet}
    */
   TimeSnippet setEndTimeStamp(DateTime endTimeStamp);

   /**
    * @return the amount of minutes between the start, and end-point
    */
   float getDuration();

   /**
    * Return the amount of minutes between the start, and end-point. If there is no
    * end-point yet, a new end-point at the most current now is created
    * 
    * @return the amount of minutes between the start, and end-point
    */
   float getDuration(TIME_TYPE type);

   /**
    * @return the representation of the current duration
    */
   String getDurationRep();

   /**
    * Returns the {@link DateTime} when this {@link TimeSnippet} begins
    * <b>Note</b> That this call is equivalent to {@link #getBeginTimeStamp()}
    * 
    * @return the {@link DateTime} when this {@link TimeSnippet} begins
    */
   DateTime getDateTime();

   /**
    * @return the time stamp object at the begin
    */
   DateTime getBeginTimeStamp();

   /**
    * @return the time stamp object at the end
    */
   DateTime getEndTimeStamp();

   String getEndTimeStampRep();

   String getBeginTimeStampRep();

}
