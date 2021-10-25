package com.adcubum.timerecording.core.work.businessday;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.jupiter.api.Test;

import com.adcubum.timerecording.settings.round.RoundMode;
import com.adcubum.timerecording.work.date.DateTime;
import com.adcubum.timerecording.work.date.DateTimeFactory;

class BusinessDayIncrementTest {

   @Test
   void testCalcDurationOfLastIncrement_OneIncrement() {

      // Given
      float expectedDurationOfLastIncrement = 1;
      long startTimeStamp = System.currentTimeMillis();
      int timeBetweenStartAndStop = 3600 * 1000;
      BusinessDayIncrement businessDayIncrement = addTimeSnippet2BDIncrement(startTimeStamp, timeBetweenStartAndStop);

      // When
      float actualDurationOfLastIncrement = businessDayIncrement.getTotalDuration();

      // Then
      assertThat(actualDurationOfLastIncrement, is(expectedDurationOfLastIncrement));
   }

   private static BusinessDayIncrement addTimeSnippet2BDIncrement(long startTimeStamp, int timeBetweenStartAndStop) {
      BusinessDayIncrement businessDayIncrement = new BusinessDayIncrementImpl();
      DateTime startTime = DateTimeFactory.createNew(startTimeStamp, RoundMode.ONE_MIN);
      businessDayIncrement = businessDayIncrement.startCurrentTimeSnippet(startTime);
      long endTimeStamp = startTimeStamp + timeBetweenStartAndStop;// 1h
      DateTime endTime = DateTimeFactory.createNew(endTimeStamp, RoundMode.ONE_MIN);
      return businessDayIncrement.stopCurrentTimeSnippet(endTime);
   }
}
