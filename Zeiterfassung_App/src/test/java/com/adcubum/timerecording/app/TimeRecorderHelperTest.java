package com.adcubum.timerecording.app;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.jupiter.api.Test;

import com.adcubum.timerecording.core.work.WorkStates;
import com.adcubum.timerecording.core.work.businessday.BusinessDay;
import com.adcubum.timerecording.core.work.businessday.BusinessDayImpl;

class TimeRecorderHelperTest {

   @Test
   void testEvalWorkingState4BusinessDay_NotWorking() {

      // Given
      TestCaseBuilder tcb = new TestCaseBuilder()
            .build();

      // When
      WorkStates actualWorkingState = TimeRecorderHelper.evalWorkingState4BusinessDay(tcb.businessDay);

      // Then
      assertThat(actualWorkingState, is(WorkStates.NOT_WORKING));
   }

   @Test
   void testEvalWorkingState4BusinessDay_Working() {

      // Given
      TestCaseBuilder tcb = new TestCaseBuilder()
            .withCurrentBusinessIncrementStart()
            .build();

      // When
      WorkStates actualWorkingState = TimeRecorderHelper.evalWorkingState4BusinessDay(tcb.businessDay);

      // Then
      assertThat(actualWorkingState, is(WorkStates.WORKING));
   }

   @Test
   void testEvalWorkingState4BusinessDay_StartAndStop_NotWorking() {

      // Given
      TestCaseBuilder tcb = new TestCaseBuilder()
            .withCurrentBusinessIncrementStart()
            .withCurrentBusinessIncrementStop()
            .build();

      // When
      WorkStates actualWorkingState = TimeRecorderHelper.evalWorkingState4BusinessDay(tcb.businessDay);

      // Then
      assertThat(actualWorkingState, is(WorkStates.NOT_WORKING));
   }

   private static class TestCaseBuilder {

      private BusinessDay businessDay;

      private TestCaseBuilder() {
         this.businessDay = new BusinessDayImpl();
      }

      private TestCaseBuilder withCurrentBusinessIncrementStart() {
         businessDay.startNewIncremental();
         return this;
      }

      private TestCaseBuilder withCurrentBusinessIncrementStop() {
         businessDay.stopCurrentIncremental();
         return this;
      }

      private TestCaseBuilder build() {
         return this;
      }
   }

}
