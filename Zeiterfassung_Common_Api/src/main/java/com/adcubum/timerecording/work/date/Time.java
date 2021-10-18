package com.adcubum.timerecording.work.date;

import java.time.LocalDate;

import com.adcubum.timerecording.work.date.TimeType.TIME_TYPE;

/**
 * The Time interface represents date and time. So basically this interface should probably be renamed
 * into DateTime or something like that
 * 
 * @author dstalder
 *
 */
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
    * Returns <code>true</code> if this {@link Time} has been started
    * before the given date or <code>false</code> if not
    * Note that this methods only compares the days of both {@link Time} instances.
    * 
    * @param time2Check
    *        the {@link Time} to check
    * @return <code>true</code> if this {@link Time} has been started
    *         before the given date, otherwise returns <code>false</code>
    */
   boolean isBefore(Time time2Check);

   /**
    * Adds the given amount of seconds to this {@link Time} and returns a new instance
    * 
    * @param seconds2Add
    * @return a new {@link Time} instance which has added the given amount of seconds
    */
   Time addSeconds(long seconds2Add);

   /**
    * @return the plain date representation of this {@link Time} as a {@link LocalDate}
    */
   LocalDate getLocalDate();

   /**
    * @return the amount of minutes of this {@link Time} instance
    */
   long getMinutes();

   /**
    * Returns the amount of milliseconds of this {@link Time}
    * 
    * @return the amount of milliseconds
    */
   long getTime();

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
