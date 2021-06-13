package com.adcubum.timerecording.work.date;

import org.joda.time.Duration;

import com.adcubum.timerecording.settings.round.RoundMode;
import com.adcubum.timerecording.work.date.TimeType.TIME_TYPE;

public interface Time {

   /**
    * Compares this {@link Time} instance to the given other one
    * 
    * @param otherTime
    *        the other {@link Time} instance to compare with
    * @return an {@link Integer} corresponding to the compare result
    */
   int compareTo(Time otherTime);

   /**
    * Returns <code>true</code> if this {@link TimeImpl} has been started
    * before the given date or <code>false</code> if not
    * Note that this methods only compares the days of both {@link TimeImpl} instances.
    * 
    * @param time2Check
    *        the {@link TimeImpl} to check
    * @return <code>true</code> if this {@link TimeImpl} has been started
    *         before the given date, otherwise returns <code>false</code>
    */
   boolean isBefore(Time time2Check);

   /**
    * Adds the given amount of seconds to this {@link TimeImpl} and returns a new instance
    * 
    * @param seconds2Add
    * @return a new {@link TimeImpl} instance which has added the given amount of seconds
    */
   Time addSeconds(long seconds2Add);

   /**
    * @return the amount of days of this TimeImpl instance
    */
   long getDays();

   /**
    * @return the amount of minutes of this TimeImpl instance
    */
   long getMinutes();

   /**
    * Returns the amount of milliseconds of this {@link TimeImpl}
    * 
    * @return the amount of milliseconds
    */
   long getTime();

   /**
    * @return the {@link Duration} of this {@link Time}
    */
   Duration getDuration();

   /**
    * @return the {@link RoundMode} of this {@link Time}
    */
   RoundMode getRoundMode();

   /**
    * Returns the factor to make proper calculations with the given time. E.g. for
    * the TIME_TYPE 'HOUR' the value 3'600'000 is returned, since an hour has
    * 3'600'000 milliseconds
    * 
    * @param type
    * @return the appropriate calculating factor
    */
   public static long getTimeRefactorValue(TIME_TYPE type) {
      switch (type) {
         case HOUR:
            return 3600000l;
         case MIN:
            return 60000l;
         case SEC:
            return 1000l;
         case MILI_SEC:
            return 1l;
         default:
            throw new IllegalStateException("Unknown TIME_TYPE value '" + type + "'!");
      }
   }

}
