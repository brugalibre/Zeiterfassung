/**
 * 
 */
package com.adcubum.timerecording.work.date;

import static com.adcubum.timerecording.work.date.Time.getTimeRefactorValue;
import static java.time.temporal.ChronoUnit.DAYS;
import static java.util.Objects.requireNonNull;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.joda.time.Duration;
import org.joda.time.Period;
import org.joda.time.PeriodType;

import com.adcubum.timerecording.settings.round.RoundMode;
import com.adcubum.timerecording.work.date.TimeType.TIME_TYPE;
import com.adcubum.util.parser.DateParser;

/**
 * The {@link TimeImpl} class represents the time, e.g. 18:55:45 It is used to add
 * additionally features such as add a amount of time to an existing
 * 
 * {@link TimeImpl} object
 * 
 * @author Dominic
 */
public class TimeImpl implements Time {

   private Duration duration;
   private RoundMode roundMode;

   /**
    * Create a new {@link TimeImpl} object according to the given {@link TimeImpl} object and the default {@link RoundMode#ONE_MIN}
    * 
    * @param time
    */
   private TimeImpl(Date date) {
      this(date.getTime());
   }

   /**
    * Create a new {@link TimeImpl} object according to the given {@link TimeImpl} object
    * 
    * @param time
    */
   private TimeImpl(Time time) {
      this(time.getTime(), time.getRoundMode());
   }

   /**
    * Creates a MessageFactory.createNew started right now a
    */
   private TimeImpl() {
      this(System.currentTimeMillis());
   }

   /**
    * Creates a new {@link TimeImpl} with the amount of milliseconds and the default {@link RoundMode#ONE_MIN}
    * 
    * @param time
    *        the actual time
    */
   private TimeImpl(long time) {
      this(time, RoundMode.ONE_MIN);
   }

   /**
    * Creates a new {@link TimeImpl} with the amount of milliseconds and the given {@link RoundMode}
    * 
    * @param time
    *        the actual time
    * @param roundMode
    *        the {@link RoundMode}
    */
   private TimeImpl(long time, RoundMode roundMode) {
      duration = new Duration(time);
      this.roundMode = requireNonNull(roundMode);
      round(roundMode);
   }

   /**
    * Adds the given amount of seconds to this {@link TimeImpl} and returns a new instance
    * 
    * @param seconds2Add
    * @return a new {@link TimeImpl} instance which has added the given amount of seconds
    */
   @Override
   public Time addSeconds(long seconds2Add) {
      return new TimeImpl(this.duration.getMillis() + seconds2Add * 1000, this.roundMode);
   }

   private void round(RoundMode roundMode) {

      switch (roundMode) {
         case ONE_MIN:
            roundSeconds();
            break;
         case FIVE_MIN:
            // Fall through
         case TEN_MIN:
            // Fall through
         case FIFTEEN_MIN:
            roundSecondsAndMinutes(roundMode);
            break;

         default:
            break;
      }
   }

   private void roundSecondsAndMinutes(RoundMode roundMode) {
      roundSeconds();
      roundMinutes(roundMode);
   }

   private void roundMinutes(RoundMode roundMode) {

      int amount = roundMode.getAmount();
      Period period = duration.toPeriod(PeriodType.dayTime());
      float modulo = period.getMinutes() % amount;
      if (modulo >= amount / 2f) {
         duration = duration.plus((amount - (int) modulo) * getTimeRefactorValue(roundMode.getTimeType()));
      } else {
         duration = duration.minus((int) modulo * getTimeRefactorValue(roundMode.getTimeType()));
      }
   }

   private void roundSeconds() {
      Period period = duration.toPeriod(PeriodType.dayTime());
      if (period.getSeconds() >= 30) {
         duration = duration.plus((60 - period.getSeconds()) * getTimeRefactorValue(TIME_TYPE.SEC));
      } else {
         duration = duration.minus(period.getSeconds() * getTimeRefactorValue(TIME_TYPE.SEC));
      }
   }

   @Override
   public boolean isBefore(Time time2Check) {
      long daysBetween = DAYS.between(getLocalDate(), time2Check.getLocalDate());
      // attention: when calculating the days between using Time::getDays does not work in all situations!
      return daysBetween > 0;
   }

   @Override
   public int hashCode() {
      final int prime = 31;
      int result = 1;
      return prime * result + ((duration == null) ? 0 : duration.hashCode());
   }

   @Override
   public boolean equals(Object obj) {
      if (this == obj) {
         return true;
      }
      if (obj == null) {
         return false;
      }
      if (getClass() != obj.getClass()) {
         return false;
      }
      TimeImpl other = (TimeImpl) obj;
      return duration.getStandardSeconds() == other.duration.getStandardSeconds();
   }

   @Override
   public String toString() {
      return DateParser.parse2String(this);
   }

   @Override
   public int compareTo(Time otherTime) {
      return duration.compareTo(otherTime.getDuration());
   }

   @Override
   public long getTime() {
      return duration.getMillis();
   }

   @Override
   public long getMinutes() {
      return duration.getStandardMinutes();
   }

   @Override
   public long getDays() {
      return duration.getStandardDays();
   }

   @Override
   public Duration getDuration() {
      return duration;
   }

   @Override
   public LocalDate getLocalDate() {
      Calendar calendarDay = getCalendarFromTime();
      int currentDay = calendarDay.get(Calendar.DAY_OF_MONTH);
      int currentMonth = calendarDay.get(Calendar.MONTH) + 1;// month starts at 0
      int currentYear = calendarDay.get(Calendar.YEAR);
      return LocalDate.of(currentYear, currentMonth, currentDay);
   }

   public Calendar getCalendarFromTime() {
      Calendar calendarDay = new GregorianCalendar();
      calendarDay.setTimeInMillis(getTime());
      calendarDay.set(Calendar.HOUR, 1);
      calendarDay.set(Calendar.MINUTE, 0);
      calendarDay.set(Calendar.SECOND, 0);
      calendarDay.set(Calendar.MILLISECOND, 0);
      return calendarDay;
   }

   @Override
   public RoundMode getRoundMode() {
      return roundMode;
   }
}
