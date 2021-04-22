package com.adcubum.timerecording.core.work.date;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.jupiter.api.Test;

import com.adcubum.timerecording.settings.round.RoundMode;
import com.adcubum.timerecording.work.date.Time;

class TimeTest {

   @Test
   void testIsNotEqual() {

      // Given
      Time timeOne = new Time(10000000, RoundMode.ONE_MIN);
      Time timeTwo = new Time(40000000, RoundMode.ONE_MIN);

      // When
      boolean isActualEqual = timeOne.equals(timeTwo);

      // Then
      assertThat(isActualEqual, is(false));
   }

   @Test
   void testIsEqual() {

      // Given
      int fifteenMinInMiliSec = 3600 * 60 * 15;
      int timeValue = fifteenMinInMiliSec + 1000;// 15' + 30''
      Time timeOne = new Time(timeValue, RoundMode.ONE_MIN);
      Time timeTwo = new Time(timeValue, RoundMode.ONE_MIN);

      // When
      boolean isActualEqual = timeOne.equals(timeTwo);

      // Then
      assertThat(isActualEqual, is(true));
   }

   @Test
   void testTimeLong() {
      // Given
      RoundMode roundMode = RoundMode.FIFTEEN_MIN;
      int fifteenMinInMiliSec = 3600 * 60 * 15;
      int timeValue = fifteenMinInMiliSec + 1000;// 15' + 30''
      long expectedAmoundMiliSec = 3600000;

      // When
      Time time = new Time(timeValue, roundMode);

      // Then
      assertThat(time.getTime(), is(expectedAmoundMiliSec));//3600000
   }
}
