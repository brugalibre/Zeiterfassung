package com.adcubum.timerecording.work.date;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.Date;

import org.junit.jupiter.api.Test;

class TimeUtilTest {

   @Test
   void isBefore() {

      // Given
      Time time2Check = TimeBuilder.of()
            .withDay(1)
            .withMonth(1)
            .withYear(2020)
            .withHour(15)
            .withMinute(0)
            .build();

      // When
      boolean isActualBeforOrAfter = TimeUtil.isTimeBeforeOrAfterMidnightOfGivenDate(time2Check, new Date());

      // Then
      assertThat(isActualBeforOrAfter, is(true));
   }

   @Test
   void isAfter() {

      // Given
      Time time2Check = TimeBuilder.of()
            .withDay(1)
            .withMonth(1)
            .withYear(2021)
            .withHour(15)
            .withMinute(0)
            .build();
      Time referenceTime = TimeBuilder.of()
            .withDay(1)
            .withMonth(1)
            .withYear(2020)
            .withHour(15)
            .withMinute(0)
            .build();

      // When
      boolean isActualBeforOrAfter = TimeUtil.isTimeBeforeOrAfterMidnightOfGivenDate(time2Check, new Date(referenceTime.getTime()));

      // Then
      assertThat(isActualBeforOrAfter, is(true));
   }

   @Test
   void isEqual() {

      // Given
      Time time2Check = TimeBuilder.of()
            .withDay(1)
            .withMonth(1)
            .withYear(2020)
            .withHour(15)
            .withMinute(0)
            .build();
      Time referenceTime = TimeBuilder.of()
            .withDay(1)
            .withMonth(1)
            .withYear(2020)
            .withHour(15)
            .withMinute(0)
            .build();

      // When
      boolean isActualBeforOrAfter = TimeUtil.isTimeBeforeOrAfterMidnightOfGivenDate(time2Check, new Date(referenceTime.getTime()));

      // Then
      assertThat(isActualBeforOrAfter, is(false));
   }

   @Test
   void testMax() {

      // Given
      Time nullTime = null;
      Time timeMin = TimeFactory.createNew(10);
      Time expectedMaxTime = TimeFactory.createNew(1);

      // When
      Time actualMax = TimeUtil.max(timeMin, expectedMaxTime, nullTime);

      // Then
      assertThat(actualMax, is(timeMin));
   }

   @Test
   void testMiddleOneNull() {

      // Given
      Time firstTime = TimeFactory.createNew(0);
      Time secondTime = TimeFactory.createNew(1);
      Time firstNullTime = null;

      // When
      Time actualMax = TimeUtil.max(firstTime, secondTime, firstNullTime);

      // Then
      assertThat(actualMax, is(TimeFactory.createNew(1)));
   }

}
