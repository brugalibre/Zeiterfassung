package com.adcubum.timerecording.work.businessday;

import java.util.Date;
import java.util.GregorianCalendar;

import com.adcubum.timerecording.core.work.businessday.TimeSnippet;
import com.adcubum.timerecording.work.date.Time;

public class TimeSnippetBuilder {

   private TimeSnippet timeSnippet;
   private int year;
   private int month;
   private int day;

   private TimeSnippetBuilder() {
      this.year = 2020;
      this.month = 1;
      this.day = 1;
   }

   public TimeSnippetBuilder withYear(int year) {
      this.year = year;
      return this;
   }

   public TimeSnippetBuilder withMonth(int month) {
      this.month = month;
      return this;
   }

   public TimeSnippetBuilder withDay(int day) {
      this.day = day;
      return this;
   }

   public TimeSnippetBuilder withStartHourAndDuration(int hour, int timeBetweenBeginAndEnd) {
      GregorianCalendar startDate = new GregorianCalendar(year, month, day, hour, 0, 0);// year, month, day, hours, min, second
      Time beginTimeStamp = new Time(startDate.getTimeInMillis());
      timeSnippet = new TimeSnippet(new Date(beginTimeStamp.getTime()));
      timeSnippet.setBeginTimeStamp(beginTimeStamp);
      timeSnippet.setEndTimeStamp(new Time(startDate.getTimeInMillis() + timeBetweenBeginAndEnd));
      return this;
   }

   public TimeSnippetBuilder withStartAndStopTime(long startTime, long stopTime) {
      Time beginTimeStamp = new Time(startTime);
      Time endTimeStamp = new Time(stopTime);
      timeSnippet = new TimeSnippet(new Date(beginTimeStamp.getTime()));
      timeSnippet.setBeginTimeStamp(beginTimeStamp);
      timeSnippet.setEndTimeStamp(endTimeStamp);
      return this;
   }

   public TimeSnippet build() {
      return timeSnippet;
   }

   public static TimeSnippetBuilder of() {
      return new TimeSnippetBuilder();
   }
}
