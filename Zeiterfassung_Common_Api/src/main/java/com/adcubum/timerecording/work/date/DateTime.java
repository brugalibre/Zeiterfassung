package com.adcubum.timerecording.work.date;

import java.time.LocalDate;

import com.adcubum.timerecording.work.date.TimeType.TIME_TYPE;

/**
 * The Time interface represents date and time 21.02.2021, 18:55:45. It is used to add
 * additionally features such as add a amount of time to an existing
 * 
 * @author dstalder
 *
 */
public interface DateTime {

   /**
    * Compares this {@link DateTime} instance to the given other one
    * 
    * @param otherDateTime
    *        the other {@link DateTime} instance to compare with
    * @return an {@link Integer} corresponding to the compare result
    */
   int compareTo(DateTime otherDateTime);

   /**
    * Returns <code>true</code> if this {@link DateTime} has been started
    * before the given date or <code>false</code> if not
    * Note that this methods only compares the days of both {@link DateTime} instances.
    * 
    * @param dateTime2Check
    *        the {@link DateTime} to check
    * @return <code>true</code> if this {@link DateTime} has been started
    *         before the given date, otherwise returns <code>false</code>
    */
   boolean isBefore(DateTime dateTime2Check);

   /**
    * Adds the given amount of seconds to this {@link DateTime} and returns a new instance
    * 
    * @param seconds2Add
    * @return a new {@link DateTime} instance which has added the given amount of seconds
    */
   DateTime addSeconds(long seconds2Add);

   /**
    * @return the plain date representation of this {@link DateTime} as a {@link LocalDate}
    */
   LocalDate getLocalDate();

   /**
    * @return the amount of minutes of this {@link DateTime} instance
    */
   long getMinutes();

   /**
    * Returns the amount of milliseconds of this {@link DateTime}
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
