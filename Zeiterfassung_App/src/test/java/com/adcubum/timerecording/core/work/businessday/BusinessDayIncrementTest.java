package com.adcubum.timerecording.core.work.businessday;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.Date;

import org.junit.jupiter.api.Test;

import com.adcubum.timerecording.settings.round.RoundMode;
import com.adcubum.timerecording.work.date.Time;
import com.adcubum.timerecording.work.date.TimeFactory;

class BusinessDayIncrementTest {

   @Test
   void testCalcDurationOfLastIncrement_OneIncrement() {

      // Given
      float expectedDurationOfLastIncrement = 1;
      BusinessDayIncrement businessDayIncrement = new BusinessDayIncrementImpl(new Date());
      long startTimeStamp = System.currentTimeMillis();
      int timeBetweenStartAndStop = 3600 * 1000;
      addTimeSnippet2BDIncrement(businessDayIncrement, startTimeStamp, timeBetweenStartAndStop);

      // When
      float actualDurationOfLastIncrement = businessDayIncrement.getTotalDuration();

      // Then
      assertThat(actualDurationOfLastIncrement, is(expectedDurationOfLastIncrement));
   }

   private static void addTimeSnippet2BDIncrement(BusinessDayIncrement businessDayIncrement, long startTimeStamp, int timeBetweenStartAndStop) {
      Time startTime = TimeFactory.createNew(startTimeStamp, RoundMode.ONE_MIN);
      businessDayIncrement.startCurrentTimeSnippet(startTime);
      long endTimeStamp = startTimeStamp + timeBetweenStartAndStop;// 1h
      Time endTime = TimeFactory.createNew(endTimeStamp, RoundMode.ONE_MIN);
      businessDayIncrement.stopCurrentTimeSnippet(endTime);
   }
}
