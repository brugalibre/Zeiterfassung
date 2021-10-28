package com.adcubum.timerecording.core.work.businessday;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.jupiter.api.Test;

import com.adcubum.timerecording.core.work.businessday.TimeSnippetImpl.TimeSnippetBuilder;
import com.adcubum.timerecording.work.date.DateTimeBuilder;

class TimeSnippetImplTest {

   @Test
   void testSetBeginTimeStamp() {

      // Given
      int hour = 3;
      int minute = 0;
      int endTimeStampHour = hour + 10;
      String expectedBeginTimeStampRep = "10:15";
      String newInput = "10:15";
      String expectedCurrentBeginTimeStampRep = "0" + String.valueOf(hour) + ":" + "0" + minute;

      TimeSnippet timeSnippet = TimeSnippetBuilder.of()
            .withBeginTime(DateTimeBuilder.of()
                  .withDay(1)
                  .withMonth(1)
                  .withYear(2021)
                  .withHour(hour)
                  .withMinute(minute)
                  .build())
            .withEndTime(DateTimeBuilder.of()
                  .withDay(1)
                  .withMonth(1)
                  .withYear(2021)
                  .withHour(endTimeStampHour)
                  .withMinute(minute)
                  .build())
            .build();

      // When
      TimeSnippet updatedTimeStamp = timeSnippet.updateAndSetBeginTimeStamp(newInput, false);

      // Then
      assertThat(updatedTimeStamp.getBeginTimeStampRep(), is(expectedBeginTimeStampRep));
      assertThat(timeSnippet.getBeginTimeStampRep(), is(expectedCurrentBeginTimeStampRep));
   }

   @Test
   void testSetBeginTimeStamp_negativeDuration_ButItsNotOk() {

      // Given
      int hour = 3;
      int minute = 0;
      int endTimeStampHour = hour + 1;
      String newInput = "10:15";
      String expectedCurrentBeginTimeStampRep = "04:00";

      TimeSnippet timeSnippet = TimeSnippetBuilder.of()
            .withBeginTime(DateTimeBuilder.of()
                  .withDay(1)
                  .withMonth(1)
                  .withYear(2021)
                  .withHour(hour)
                  .withMinute(minute)
                  .build())
            .withEndTime(DateTimeBuilder.of()
                  .withDay(1)
                  .withMonth(1)
                  .withYear(2021)
                  .withHour(endTimeStampHour)
                  .withMinute(minute)
                  .build())
            .build();

      // When
      TimeSnippet updatedTimeStamp = timeSnippet.updateAndSetBeginTimeStamp(newInput, false);

      // Then
      assertThat(updatedTimeStamp.getEndTimeStampRep(), is(expectedCurrentBeginTimeStampRep));
   }

   void testSetBeginTimeStamp_negativeDuration_butItsOk() {

      // Given
      int hour = 3;
      int minute = 0;
      int endTimeStampHour = hour + 1;
      String newInput = "10:15";
      String expectedCurrentBeginTimeStampRep = "04:00";

      TimeSnippet timeSnippet = TimeSnippetBuilder.of()
            .withBeginTime(DateTimeBuilder.of()
                  .withDay(1)
                  .withMonth(1)
                  .withYear(2021)
                  .withHour(hour)
                  .withMinute(minute)
                  .build())
            .withEndTime(DateTimeBuilder.of()
                  .withDay(1)
                  .withMonth(1)
                  .withYear(2021)
                  .withHour(endTimeStampHour)
                  .withMinute(minute)
                  .build())
            .build();

      // When
      TimeSnippet updatedTimeStamp = timeSnippet.updateAndSetBeginTimeStamp(newInput, true);

      // Then
      assertThat(updatedTimeStamp, is(timeSnippet));
   }

   @Test
   void testSetEndTimeStamp() {

      // Given
      int hour = 3;
      int minute = 0;
      int endTimeStampHour = hour + 5;
      String expectedEndTimeStampRep = "10:15";
      String newInput = "10:15";
      String expectedCurrentEndTimeStampRep = "0" + String.valueOf(endTimeStampHour) + ":" + "0" + minute;

      TimeSnippet timeSnippet = TimeSnippetBuilder.of()
            .withBeginTime(DateTimeBuilder.of()
                  .withDay(1)
                  .withMonth(1)
                  .withYear(2021)
                  .withHour(hour)
                  .withMinute(minute)
                  .build())
            .withEndTime(DateTimeBuilder.of()
                  .withDay(1)
                  .withMonth(1)
                  .withYear(2021)
                  .withHour(endTimeStampHour)
                  .withMinute(minute)
                  .build())
            .build();

      // When
      TimeSnippet updatedTimeStamp = timeSnippet.updateAndSetEndTimeStamp(newInput, false);

      // Then
      assertThat(updatedTimeStamp.getEndTimeStampRep(), is(expectedEndTimeStampRep));
      assertThat(timeSnippet.getEndTimeStampRep(), is(expectedCurrentEndTimeStampRep));
   }

}
