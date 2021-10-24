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

public class DateTimeUtil {

   private DateTimeUtil() {
      // private
   }

   /**
    * Verifies if the given {@link DateTime} instance is before or after midnight of the given reference {@link Date}
    * 
    * @param time2Check
    *        the {@link DateTime} which is checked
    * @param referenceTime
    *        the reference time
    * @return <code>true</code> if the given {@link DateTime} instance is before or after midnight of the given reference {@link Date}.
    *         Otherwise this returns <code>false</code>
    */
   public static boolean isTimeBeforeOrAfterMidnightOfGivenDate(DateTime time2Check, DateTime referenceTime) {
      return isTimeBeforeMidnightOfGivenDate(time2Check, referenceTime)
            || isTimeAfterMidnightOfGivenDate(time2Check, referenceTime);
   }

   private static boolean isTimeAfterMidnightOfGivenDate(DateTime time2Check, DateTime referenceTime) {
      Calendar calender2CheckUpperBounds = new GregorianCalendar();
      calender2CheckUpperBounds.setTimeInMillis(referenceTime.getTime());
      calender2CheckUpperBounds.set(Calendar.HOUR_OF_DAY, 23);
      calender2CheckUpperBounds.set(Calendar.MINUTE, 59);
      calender2CheckUpperBounds.set(Calendar.SECOND, 59);
      calender2CheckUpperBounds.set(Calendar.MILLISECOND, 0);
      DateTime time2CheckUpperBounds = DateTimeFactory.createNew(calender2CheckUpperBounds.getTime());
      return time2CheckUpperBounds.isBefore(time2Check);
   }

   /**
    * Returns a List with {@link DateTime} instances which are between (inclusive) the given bounds.
    * Note that this Method only works as long as lower- and upper bounds are within the same year.
    * 
    * @param lowerBounds
    *        the lower bounds
    * @param upperBounds
    *        the upper bounds
    * @return a List with {@link DateTime} instances which are between (inclusive) the first and the last date
    */
   public static List<LocalDate> getDatesInBetween(DateTime lowerBounds, DateTime upperBounds) {
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
    * @return a {@link DateTime} instance representing the first day of the current month
    */
   public static DateTime getFirstDayOfCurrentMonth() {
      Calendar firstOfMonth = getFirstCalendarOfCurrentMonth();
      return DateTimeFactory.createNew(firstOfMonth.getTime());
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
    * @return a {@link DateTime} instance representing the last day of the current month
    */
   public static DateTime getLastDayOfCurrentMonth() {
      Calendar now = new GregorianCalendar();
      int lastDayInMonth = now.getActualMaximum(Calendar.DAY_OF_MONTH);

      now.set(Calendar.DAY_OF_MONTH, lastDayInMonth);
      now.set(Calendar.HOUR_OF_DAY, 1);
      now.set(Calendar.MINUTE, 0);
      now.set(Calendar.SECOND, 0);
      now.set(Calendar.MILLISECOND, 0);
      return DateTimeFactory.createNew(now.getTimeInMillis(), RoundMode.ONE_MIN);
   }

   /**
    * @return a {@link DateTime} instance representing the last day of the previous month
    */
   public static DateTime getLastOfPrevMonth() {
      return getLastOfPrevMonth(new GregorianCalendar());
   }

   /**
    * @return a {@link DateTime} instance representing the last day of the previous month
    */
   public static DateTime getLastOfPrevMonth(Calendar calendar) {
      int month = calendar.get(Calendar.MONTH);
      int year = calendar.get(Calendar.YEAR);
      if (month == 0) {
         month = 11;
         year--;
      } else {
         month--;
      }
      calendar.set(Calendar.YEAR, year);
      calendar.set(Calendar.MONTH, month);
      int lastDayInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
      calendar.set(Calendar.DAY_OF_MONTH, lastDayInMonth);
      calendar.set(Calendar.HOUR_OF_DAY, 1);
      calendar.set(Calendar.MINUTE, 0);
      calendar.set(Calendar.SECOND, 0);
      calendar.set(Calendar.MILLISECOND, 0);
      return DateTimeFactory.createNew(calendar.getTimeInMillis(), RoundMode.ONE_MIN);
   }

   /**
    * Verifies if the given {@link DateTime} instance is before midnight of the given reference {@link Date}
    * 
    * @param time2Check
    *        the {@link DateTime} which is checked
    * @param referenceTime
    *        the reference time
    * @return <code>true</code> if the given {@link DateTime} instance is before midnight of the given reference {@link Date}.
    *         Otherwise this returns <code>false</code>
    */
   public static boolean isTimeBeforeMidnightOfGivenDate(DateTime time2Check, DateTime referenceTime) {
      Date resetedReferenceDate = setHoursMinSecondsAndMillisToMin(referenceTime.getTime());
      DateTime time2CheckLowerBounds = DateTimeFactory.createNew(resetedReferenceDate);
      return time2Check.isBefore(time2CheckLowerBounds);
   }

   /**
    * Creates a new {@link DateTime} for the given time and sets maximal values for
    * hour, minute, seconds and milliseconds
    * 
    * @param time
    *        the given {@link DateTime} instance
    * @return a new {@link DateTime} with maximal hours, minutes, seconds and milliseconds
    */
   public static DateTime getEndOfDay(DateTime time) {
      Date date = setHoursMinSecondsAndMillisToMax(time.getTime());
      return createNewTime(date.getTime());
   }


   /**
    * Creates a new {@link DateTime} for the given time and sets minimal values for
    * hour, minute, seconds and milliseconds
    * 
    * @param time
    *        the given {@link DateTime} instance
    * @return a new {@link DateTime} with maximal hours, minutes, seconds and milliseconds
    */
   public static DateTime getBeginOfDay(DateTime time) {
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

   private static DateTime createNewTime(long timeValue) {
      return DateTimeFactory.createNew(timeValue, RoundMode.SEC);
   }

   /**
    * Returns the {@link DateTime} instance of that {@link DateTime} object which has the maximal amount of miliseconds
    * or <code>null</code> if there is an empty list
    * Null values are going to be ignored
    * 
    * @param times
    *        the {@link DateTime} objects to test
    * @return the {@link DateTime} instance of that {@link DateTime} object which has the maximal amount of miliseconds
    */
   public static DateTime max(DateTime... times) {
      DateTime maxTime = null;
      for (DateTime time : times) {
         if (isNull(maxTime)
               || currentTimeInstanceIsGreater(maxTime, time)) {
            maxTime = time;
         }
      }
      return maxTime;
   }

   /**
    * Returns the day of the week for the given {@link DateTime}
    * 
    * @param nextDate
    *        the {@link DateTime} to check
    * @return the day of the week for the given {@link DateTime}
    * @see Calendar.DAY_OF_WEEK
    */
   public static int getCalenderOfTheWeek(DateTime nextDate) {
      Calendar c = Calendar.getInstance();
      c.setTime(new Date(nextDate.getTime()));
      return c.get(Calendar.DAY_OF_WEEK);
   }

   private static boolean currentTimeInstanceIsGreater(DateTime maxTime, DateTime time) {
      return nonNull(time)
            && time.getTime() > maxTime.getTime();
   }
}
