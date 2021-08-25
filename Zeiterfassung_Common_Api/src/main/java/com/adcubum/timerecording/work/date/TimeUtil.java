package com.adcubum.timerecording.work.date;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class TimeUtil {

   private TimeUtil() {
      // private
   }

   /**
    * Verifies if the given {@link Time} instance is before or after midnight of the given reference {@link Date}
    * 
    * @param time2Check
    *        the {@link Time} which is checked
    * @param referenceDate
    *        the reference date
    * @return <code>true</code> if the given {@link Time} instance is before or after midnight of the given reference {@link Date}.
    *         Otherwise this returns <code>false</code>
    */
   public static boolean isTimeBeforeOrAfterMidnightOfGivenDate(Time time2Check, Date referenceDate) {
      return isTimeBeforeMidnightOfGivenDate(time2Check, referenceDate)
            || isTimeAfterMidnightOfGivenDate(time2Check, referenceDate);
   }

   private static boolean isTimeAfterMidnightOfGivenDate(Time time2Check, Date referenceDate) {
      Calendar calender2CheckUpperBounds = new GregorianCalendar();
      calender2CheckUpperBounds.setTime(referenceDate);
      calender2CheckUpperBounds.set(Calendar.HOUR_OF_DAY, 23);
      calender2CheckUpperBounds.set(Calendar.MINUTE, 59);
      calender2CheckUpperBounds.set(Calendar.SECOND, 59);
      calender2CheckUpperBounds.set(Calendar.MILLISECOND, 0);
      Time time2CheckUpperBounds = TimeFactory.createNew(calender2CheckUpperBounds.getTime());
      return time2CheckUpperBounds.isBefore(time2Check);
   }

   /**
    * Verifies if the given {@link Time} instance is before midnight of the given reference {@link Date}
    * 
    * @param time2Check
    *        the {@link Time} which is checked
    * @param referenceDate
    *        the reference date
    * @return <code>true</code> if the given {@link Time} instance is before midnight of the given reference {@link Date}.
    *         Otherwise this returns <code>false</code>
    */
   public static boolean isTimeBeforeMidnightOfGivenDate(Time time2Check, Date referenceDate) {
      Calendar calender2CheckLowerBounds = new GregorianCalendar();
      calender2CheckLowerBounds.setTime(referenceDate);
      calender2CheckLowerBounds.set(Calendar.HOUR_OF_DAY, 0);
      calender2CheckLowerBounds.set(Calendar.MINUTE, 0);
      calender2CheckLowerBounds.set(Calendar.SECOND, 0);
      calender2CheckLowerBounds.set(Calendar.MILLISECOND, 0);
      Time time2CheckLowerBounds = TimeFactory.createNew(calender2CheckLowerBounds.getTime());
      return time2Check.isBefore(time2CheckLowerBounds);
   }

   /**
    * Returns the {@link Time} instance of that {@link Time} object which has the maximal amount of miliseconds
    * or <code>null</code> if there is an empty list
    * Null values are going to be ignored
    * 
    * @param times
    *        the {@link Time} objects to test
    * @return the {@link Time} instance of that {@link Time} object which has the maximal amount of miliseconds
    */
   public static Time max(Time... times) {
      Time maxTime = null;
      for (Time time : times) {
         if (isNull(maxTime)
               || currentTimeInstanceIsGreater(maxTime, time)) {
            maxTime = time;
         }
      }
      return maxTime;
   }

   /**
    * Returns the day of the week for the given {@link Time}
    * 
    * @param nextDate
    *        the {@link Time} to check
    * @return the day of the week for the given {@link Time}
    * @see Calendar.DAY_OF_WEEK
    */
   public static int getCalenderOfTheWeek(Time nextDate) {
      Calendar c = Calendar.getInstance();
      c.setTime(new Date(nextDate.getTime()));
      return c.get(Calendar.DAY_OF_WEEK);
   }

   private static boolean currentTimeInstanceIsGreater(Time maxTime, Time time) {
      return nonNull(time)
            && time.getTime() > maxTime.getTime();
   }
}
