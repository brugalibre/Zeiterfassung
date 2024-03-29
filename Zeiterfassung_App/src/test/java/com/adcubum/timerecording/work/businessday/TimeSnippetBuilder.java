package com.adcubum.timerecording.work.businessday;

import java.util.GregorianCalendar;

import com.adcubum.timerecording.core.work.businessday.TimeSnippet;
import com.adcubum.timerecording.core.work.businessday.TimeSnippetImpl;
import com.adcubum.timerecording.work.date.DateTime;
import com.adcubum.timerecording.work.date.DateTimeFactory;

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
      DateTime beginTimeStamp = DateTimeFactory.createNew(startDate.getTimeInMillis());
      timeSnippet = TimeSnippetImpl.TimeSnippetBuilder.of()
            .withBeginTime(beginTimeStamp)
            .withEndTime(DateTimeFactory.createNew(startDate.getTimeInMillis() + timeBetweenBeginAndEnd))
            .build();
      return this;
   }

   public TimeSnippetBuilder withStartAndStopTime(long startTime, long stopTime) {
      DateTime beginTimeStamp = DateTimeFactory.createNew(startTime);
      DateTime endTimeStamp = DateTimeFactory.createNew(stopTime);
      timeSnippet = TimeSnippetImpl.TimeSnippetBuilder.of()
            .withBeginTime(beginTimeStamp)
            .withEndTime(endTimeStamp)
            .build();
      return this;
   }

   public TimeSnippet build() {
      return timeSnippet;
   }

   public static TimeSnippetBuilder of() {
      return new TimeSnippetBuilder();
   }
}
