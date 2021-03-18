package com.myownb3.dominic.timerecording.core.work.date;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.jupiter.api.Test;

import com.myownb3.dominic.timerecording.settings.round.RoundMode;

class TimeTest {

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
