package com.adcubum.timerecording.app;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.adcubum.timerecording.core.book.adapter.BookerAdapter;
import com.adcubum.timerecording.core.callbackhandler.UiCallbackHandler;
import com.adcubum.timerecording.core.work.businessday.BusinessDay;
import com.adcubum.timerecording.core.work.businessday.comeandgo.ComeAndGo;
import com.adcubum.timerecording.core.work.businessday.comeandgo.ComeAndGoes;

class TimeRecorderImplTest {

   @Test
   void testNeedsStartBookingReminder_NoContent_NoStartBookingNecessary() {
      // Given
      boolean expectedNeedsStartBookingReminder = false;
      TestCaseBuilder tcb = new TestCaseBuilder()
            .build();

      // When
      boolean actualNeedsStartBookingReminder = tcb.timeRecorder.needsStartBookingReminder();

      // Then
      assertThat(actualNeedsStartBookingReminder, is(expectedNeedsStartBookingReminder));
   }

   @Test
   void testNeedsStartBookingReminder_WithContent_StartBookingReminderNecessary() {
      // Given
      boolean expectedNeedsStartBookingReminder = true;
      TestCaseBuilder tcb = new TestCaseBuilder()
            .withTotalDuration(10)
            .build();

      // When
      boolean actualNeedsStartBookingReminder = tcb.timeRecorder.needsStartBookingReminder();

      // Then
      assertThat(actualNeedsStartBookingReminder, is(expectedNeedsStartBookingReminder));
   }

   @Test
   void testNeedsStartBookingReminder_ComeAndGo_StartBookingReminderNotNecessary() {
      // Given
      boolean expectedNeedsStartBookingReminder = true;
      TestCaseBuilder tcb = new TestCaseBuilder()
            .withComeAndGo(mock(ComeAndGo.class))
            .build();

      // When
      boolean actualNeedsStartBookingReminder = tcb.timeRecorder.needsStartBookingReminder();

      // Then
      assertThat(actualNeedsStartBookingReminder, is(expectedNeedsStartBookingReminder));
   }

   @Test
   void testNeedsStartRecordingReminder_Idle_ReminderNecessary() {
      // Given
      boolean expectedNeedsStartRecordingReminder = true;
      TestCaseBuilder tcb = new TestCaseBuilder()
            .build();

      // When
      boolean actualNeedsStartRecordingReminder = tcb.timeRecorder.needsStartRecordingReminder();

      // Then
      assertThat(actualNeedsStartRecordingReminder, is(expectedNeedsStartRecordingReminder));
   }

   @Test
   void testNeedsStartRecordingReminder_IdleButWithContent_NotRecordingReminderNecessary() {
      // Given
      boolean expectedNeedsStartRecordingReminder = false;
      TestCaseBuilder tcb = new TestCaseBuilder()
            .withTotalDuration(10)
            .build();

      // When
      boolean actualNeedsStartRecordingReminder = tcb.timeRecorder.needsStartRecordingReminder();

      // Then
      assertThat(actualNeedsStartRecordingReminder, is(expectedNeedsStartRecordingReminder));
   }

   @Test
   void testNeedsStartRecordingReminder_AlreadyRecording_NoReminderNecessary() {
      // Given
      boolean expectedNeedsStartRecordingReminder = false;
      TestCaseBuilder tcb = new TestCaseBuilder()
            .build();
      tcb.timeRecorder.handleUserInteraction(false);

      // When
      boolean actualNeedsStartRecordingReminder = tcb.timeRecorder.needsStartRecordingReminder();

      // Then
      assertThat(actualNeedsStartRecordingReminder, is(expectedNeedsStartRecordingReminder));
   }

   @Test
   void testNeedsStartRecordingReminder_AlreadyComeAndGoActive_NoReminderNecessary() {
      // Given
      boolean expectedNeedsStartRecordingReminder = false;
      TestCaseBuilder tcb = new TestCaseBuilder()
            .build();
      tcb.timeRecorder.handleUserInteraction(true);

      // When
      boolean actualNeedsStartRecordingReminder = tcb.timeRecorder.needsStartRecordingReminder();

      // Then
      assertThat(actualNeedsStartRecordingReminder, is(expectedNeedsStartRecordingReminder));
   }

   private static class TestCaseBuilder {
      private TimeRecorderImpl timeRecorder;
      private BusinessDay businessDay;
      private List<ComeAndGo> comeAndGoEntries;
      private ComeAndGoes comeAndGoes;

      private TestCaseBuilder() {
         this.businessDay = mock(BusinessDay.class);
         this.comeAndGoEntries = new ArrayList<>();
         this.comeAndGoes = mock(ComeAndGoes.class);
         when(comeAndGoes.getComeAndGoEntries()).thenReturn(comeAndGoEntries);
      }

      public TestCaseBuilder withComeAndGo(ComeAndGo comeAndGo) {
         this.comeAndGoEntries.add(comeAndGo);
         return this;
      }

      private TestCaseBuilder withTotalDuration(float totalDuration) {
         when(businessDay.getTotalDuration()).thenReturn(totalDuration);
         return this;
      }

      private TestCaseBuilder build() {
         when(businessDay.getComeAndGoes()).thenReturn(comeAndGoes);
         this.timeRecorder = new TimeRecorderImpl(mock(BookerAdapter.class), businessDay);
         this.timeRecorder.setCallbackHandler(mock(UiCallbackHandler.class));
         return this;
      }
   }

}
