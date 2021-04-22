package com.adcubum.timerecording.work.businessday;

import java.util.Date;
import java.util.GregorianCalendar;

import com.adcubum.timerecording.core.work.businessday.TimeSnippet;
import com.adcubum.timerecording.work.date.Time;

public class TimeSnippetBuilder {

   private TimeSnippet timeSnippet;

   private TimeSnippetBuilder() {
      // privé
   }

   public TimeSnippetBuilder withStartHourAndDuration(int hour, int timeBetweenBeginAndEnd) {
      GregorianCalendar startDate = new GregorianCalendar(2020, 1, 1, hour, 0, 0);// year, month, day, hours, min, second
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
