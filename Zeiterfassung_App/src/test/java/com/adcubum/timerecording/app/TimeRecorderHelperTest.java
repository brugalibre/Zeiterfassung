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

   @Test
   void testEvalWorkingState4BusinessDay_Come_ComeAndGo() {

      // Given
      TestCaseBuilder tcb = new TestCaseBuilder()
            .withCome()
            .build();

      // When
      WorkStates actualWorkingState = TimeRecorderHelper.evalWorkingState4BusinessDay(tcb.businessDay);

      // Then
      assertThat(actualWorkingState, is(WorkStates.COME_AND_GO));
   }

   @Test
   void testEvalWorkingState4BusinessDay_ComeAndGo_NotWorking() {

      // Given
      TestCaseBuilder tcb = new TestCaseBuilder()
            .withCome()
            .withGo()
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

      private TestCaseBuilder withCome() {
         businessDay = businessDay.comeOrGo();
         return this;
      }

      private TestCaseBuilder withGo() {
         businessDay = businessDay.comeOrGo();
         return this;
      }

      private TestCaseBuilder withCurrentBusinessIncrementStart() {
         businessDay = businessDay.startNewIncremental();
         return this;
      }

      private TestCaseBuilder withCurrentBusinessIncrementStop() {
         businessDay = businessDay.stopCurrentIncremental();
         return this;
      }

      private TestCaseBuilder build() {
         return this;
      }
   }

}
