package com.adcubum.timerecording.work.date;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class TimeBuilder {

   private int year;
   private int month;
   private int day;
   private int minute;
   private int hour;

   private TimeBuilder() {
      this.year = 2020;
      this.month = 1;
      this.day = 1;
   }

   public TimeBuilder withYear(int year) {
      this.year = year;
      return this;
   }

   /**
    * @param month
    *        the value used to set the <code>MONTH</code> calendar field in the {@link Calendar} calendar.
    *        Month value is 0-based. e.g., 0 for January.
    * 
    * @return this {@link TimeBuilder}
    */
   public TimeBuilder withMonth(int month) {
      this.month = month;
      return this;
   }

   public TimeBuilder withDay(int day) {
      this.day = day;
      return this;
   }

   public TimeBuilder withHour(int hour) {
      this.hour = hour;
      return this;
   }

   public TimeBuilder withMinute(int minute) {
      this.minute = minute;
      return this;
   }

   public Time build() {
      // Could also be done with LocalDateTime, but then I've to deal with TimeZones so...
      GregorianCalendar startDate = new GregorianCalendar(year, month, day, hour, minute, 0);// year, month, day, hours, min, second
      return TimeFactory.createNew(startDate.getTimeInMillis());
   }

   public static TimeBuilder of() {
      return new TimeBuilder();
   }
}
