package com.adcubum.timerecording.work.date;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.jupiter.api.Test;

class TimeUtilTest {

   @Test
   void testMax() {

      // Given
      Time nullTime = null;
      Time timeMin = new Time(10);
      Time expectedMaxTime = new Time(1);

      // When
      Time actualMax = TimeUtil.max(timeMin, expectedMaxTime, nullTime);

      // Then
      assertThat(actualMax, is(timeMin));
   }

   @Test
   void testMiddleOneNull() {

      // Given
      Time firstTime = new Time(0);
      Time secondTime = new Time(1);
      Time firstNullTime = null;

      // When
      Time actualMax = TimeUtil.max(firstTime, secondTime, firstNullTime);

      // Then
      assertThat(actualMax, is(new Time(1)));
   }

}
