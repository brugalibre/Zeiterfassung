package com.adcubum.timerecording.work.date;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.Test;

class TimeUtilTest {

   @Test
   void getTimeListInDuration() {

      // Given
      int firstDayInPeriod = 3;
      int lastDayInPeriod = 15;
      Time lowerBounds = TimeBuilder.of()
            .withDay(firstDayInPeriod)
            .withMonth(1)
            .withYear(2020)
            .withHour(15)
            .withMinute(0)
            .build();
      Time upperBounds = TimeBuilder.of()
            .withDay(lastDayInPeriod)
            .withMonth(1)
            .withYear(2020)
            .withHour(1)
            .withMinute(0)
            .build();

      // When
      List<LocalDate> datesInPeriod = TimeUtil.getDatesInBetween(lowerBounds, upperBounds);

      // Then
      assertThat(datesInPeriod.size(), is(lastDayInPeriod - firstDayInPeriod + 1));
      LocalDate firstDate = datesInPeriod.get(0);
      assertThat(firstDate.getDayOfMonth(), is(firstDayInPeriod));

      LocalDate lastDate = datesInPeriod.get(datesInPeriod.size() - 1);
      assertThat(lastDate.getDayOfMonth(), is(lastDayInPeriod));
   }

   @Test
   void getTimeListInDurationOverTwoMonth() {

      // Given
      int firstDayInPeriod = 3;
      int lastDayInPeriod = 28;
      int expectedDaysBetween = 57;
      Time lowerBounds = TimeBuilder.of()
            .withDay(firstDayInPeriod)
            .withMonth(1)
            .withYear(2020)
            .withHour(15)
            .withMinute(0)
            .build();
      Time upperBounds = TimeBuilder.of()
            .withDay(lastDayInPeriod)
            .withMonth(2)
            .withYear(2020)
            .withHour(1)
            .withMinute(0)
            .build();

      // When
      List<LocalDate> datesInPeriod = TimeUtil.getDatesInBetween(lowerBounds, upperBounds);

      // Then
      assertThat(datesInPeriod.size(), is(expectedDaysBetween));
      LocalDate firstDate = datesInPeriod.get(0);
      assertThat(firstDate.getDayOfMonth(), is(firstDayInPeriod));

      LocalDate lastDate = datesInPeriod.get(datesInPeriod.size() - 1);
      assertThat(lastDate.getDayOfMonth(), is(lastDayInPeriod));
   }

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
