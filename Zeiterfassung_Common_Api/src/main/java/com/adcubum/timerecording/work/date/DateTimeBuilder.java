package com.adcubum.timerecording.work.date;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class DateTimeBuilder {

   private int year;
   private int month;
   private int day;
   private int minute;
   private int hour;

   private DateTimeBuilder() {
      this.year = 2020;
      this.month = 1;
      this.day = 1;
   }

   public DateTimeBuilder withYear(int year) {
      this.year = year;
      return this;
   }

   /**
    * @param month
    *        the value used to set the <code>MONTH</code> calendar field in the {@link Calendar} calendar.
    *        Month value is 0-based. e.g., 0 for January.
    * 
    * @return this {@link DateTimeBuilder}
    */
   public DateTimeBuilder withMonth(int month) {
      this.month = month;
      return this;
   }

   public DateTimeBuilder withDay(int day) {
      this.day = day;
      return this;
   }

   public DateTimeBuilder withHour(int hour) {
      this.hour = hour;
      return this;
   }

   public DateTimeBuilder withMinute(int minute) {
      this.minute = minute;
      return this;
   }

   public DateTime build() {
      // Could also be done with LocalDateTime, but then I've to deal with TimeZones so...
      GregorianCalendar startDate = new GregorianCalendar(year, month, day, hour, minute, 0);// year, month, day, hours, min, second
      return DateTimeFactory.createNew(startDate.getTimeInMillis());
   }

   public static DateTimeBuilder of() {
      return new DateTimeBuilder();
   }
}
