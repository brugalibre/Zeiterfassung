package com.adcubum.timerecording.work.date;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.jupiter.api.Test;

class TimeUtilTest {

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
