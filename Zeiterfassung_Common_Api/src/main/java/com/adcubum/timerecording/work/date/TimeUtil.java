package com.adcubum.timerecording.work.date;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import com.adcubum.timerecording.settings.round.RoundMode;

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
    * Returns a List with {@link Time} instances which are between (inclusive) the given bounds.
    * Note that this Method only works as long as lower- and upper bounds are within the same year.
    * 
    * @param lowerBounds
    *        the lower bounds
    * @param upperBounds
    *        the upper bounds
    * @return a List with {@link Time} instances which are between (inclusive) the first and the last date
    */
   public static List<LocalDate> getDatesInBetween(Time lowerBounds, Time upperBounds) {
      if (isNull(lowerBounds) || isNull(upperBounds)) {
         return Collections.emptyList();
      }
      List<LocalDate> timeDurationInBetween = new ArrayList<>();
      LocalDate currentLocalDate = lowerBounds.getLocalDate();
      LocalDate upperBoundsLocalDate = upperBounds.getLocalDate();
      while (currentLocalDate.isBefore(upperBoundsLocalDate)
            || currentLocalDate.isEqual(upperBoundsLocalDate)) {

         timeDurationInBetween.add(currentLocalDate);
         // increment day/month
         if (currentLocalDate.getDayOfMonth() == currentLocalDate.lengthOfMonth()) {
            // first reset days to zero, then shift the month
            currentLocalDate = currentLocalDate.minusDays(currentLocalDate.getDayOfMonth() - 1);
            currentLocalDate = currentLocalDate.plusMonths(1);
         } else {
            currentLocalDate = currentLocalDate.plusDays(1);
         }
      }
      return timeDurationInBetween;
   }

   /**
    * @return a {@link Time} instance representing the first day of the current month
    */
   public static Time getFirstDayOfCurrentMonth() {
      Calendar firstOfMonth = getFirstCalendarOfCurrentMonth();
      return TimeFactory.createNew(firstOfMonth.getTime());
   }

   private static Calendar getFirstCalendarOfCurrentMonth() {
      Calendar firstOfMonth = new GregorianCalendar();
      firstOfMonth.set(Calendar.DAY_OF_MONTH, 1);
      firstOfMonth.set(Calendar.HOUR_OF_DAY, 0);
      firstOfMonth.set(Calendar.MINUTE, 0);
      firstOfMonth.set(Calendar.SECOND, 0);
      firstOfMonth.set(Calendar.MILLISECOND, 0);
      return firstOfMonth;
   }

   /**
    * @return a {@link Time} instance representing the last day of the current month
    */
   public static Time getLastDayOfCurrentMonth() {
      Calendar now = new GregorianCalendar();
      int lastDayInMonth = now.getActualMaximum(Calendar.DAY_OF_MONTH);

      now.set(Calendar.DAY_OF_MONTH, lastDayInMonth);
      now.set(Calendar.HOUR_OF_DAY, 1);
      now.set(Calendar.MINUTE, 0);
      now.set(Calendar.SECOND, 0);
      now.set(Calendar.MILLISECOND, 0);
      return TimeFactory.createNew(now.getTimeInMillis(), RoundMode.ONE_MIN);
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
      Date resetedReferenceDate = setHoursMinSecondsAndMillisToMin(referenceDate.getTime());
      Time time2CheckLowerBounds = TimeFactory.createNew(resetedReferenceDate);
      return time2Check.isBefore(time2CheckLowerBounds);
   }

   /**
    * Creates a new {@link Time} for the given time and sets maximal values for
    * hour, minute, seconds and milliseconds
    * 
    * @param time
    *        the given {@link Time} instance
    * @return a new {@link Time} with maximal hours, minutes, seconds and milliseconds
    */
   public static Time getEndOfDay(Time time) {
      Date date = setHoursMinSecondsAndMillisToMax(time.getTime());
      return createNewTime(date.getTime());
   }


   /**
    * Creates a new {@link Time} for the given time and sets minimal values for
    * hour, minute, seconds and milliseconds
    * 
    * @param time
    *        the given {@link Time} instance
    * @return a new {@link Time} with maximal hours, minutes, seconds and milliseconds
    */
   public static Time getBeginOfDay(Time time) {
      Date date = setHoursMinSecondsAndMillisToMin(time.getTime());
      return createNewTime(date.getTime());
   }

   private static Date setHoursMinSecondsAndMillisToMax(long timeValue) {
      Calendar calender2CheckLowerBounds = new GregorianCalendar();
      calender2CheckLowerBounds.setTime(new Date(timeValue));
      calender2CheckLowerBounds.set(Calendar.HOUR_OF_DAY, 23);
      calender2CheckLowerBounds.set(Calendar.MINUTE, 59);
      calender2CheckLowerBounds.set(Calendar.SECOND, 59);
      calender2CheckLowerBounds.set(Calendar.MILLISECOND, 0);
      return calender2CheckLowerBounds.getTime();
   }

   private static Date setHoursMinSecondsAndMillisToMin(long timeValue) {
      Calendar calender2CheckLowerBounds = new GregorianCalendar();
      calender2CheckLowerBounds.setTime(new Date(timeValue));
      calender2CheckLowerBounds.set(Calendar.HOUR_OF_DAY, 0);
      calender2CheckLowerBounds.set(Calendar.MINUTE, 0);
      calender2CheckLowerBounds.set(Calendar.SECOND, 0);
      calender2CheckLowerBounds.set(Calendar.MILLISECOND, 0);
      return calender2CheckLowerBounds.getTime();
   }

   private static Time createNewTime(long timeValue) {
      return TimeFactory.createNew(timeValue, RoundMode.SEC);
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
