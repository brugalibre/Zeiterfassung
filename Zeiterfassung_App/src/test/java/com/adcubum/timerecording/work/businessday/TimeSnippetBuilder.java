package com.adcubum.timerecording.work.businessday;

import java.util.GregorianCalendar;

import com.adcubum.timerecording.core.work.businessday.TimeSnippet;
import com.adcubum.timerecording.core.work.businessday.TimeSnippetFactory;
import com.adcubum.timerecording.work.date.Time;
import com.adcubum.timerecording.work.date.TimeFactory;

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
      Time beginTimeStamp = TimeFactory.createNew(startDate.getTimeInMillis());
      timeSnippet = TimeSnippetFactory.createNew();
      timeSnippet.setBeginTimeStamp(beginTimeStamp);
      timeSnippet.setEndTimeStamp(TimeFactory.createNew(startDate.getTimeInMillis() + timeBetweenBeginAndEnd));
      return this;
   }

   public TimeSnippetBuilder withStartAndStopTime(long startTime, long stopTime) {
      Time beginTimeStamp = TimeFactory.createNew(startTime);
      Time endTimeStamp = TimeFactory.createNew(stopTime);
      timeSnippet = TimeSnippetFactory.createNew();
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
