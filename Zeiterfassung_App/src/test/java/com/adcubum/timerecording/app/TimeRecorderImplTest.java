package com.adcubum.timerecording.app;

import static com.adcubum.timerecording.core.work.businessday.repository.BusinessDayRepositoryMockUtil.mockBusinessDayRepository;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import com.adcubum.timerecording.messaging.send.BookBusinessDayMessageSender;
import org.junit.jupiter.api.Test;

import com.adcubum.timerecording.core.book.adapter.BookerAdapter;
import com.adcubum.timerecording.core.callbackhandler.UiCallbackHandler;
import com.adcubum.timerecording.core.work.businessday.BusinessDay;
import com.adcubum.timerecording.core.work.businessday.BusinessDayImpl;
import com.adcubum.timerecording.core.work.businessday.comeandgo.impl.ComeAndGoesImpl;
import com.adcubum.timerecording.core.work.businessday.repository.BusinessDayRepository;

class TimeRecorderImplTest {

   @Test
   void testNeedsStartBookingReminder_NoContent_StartBookingReminderNotNecessary() {
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
            .withNotChargedElement(true)
            .build();

      // When
      boolean actualNeedsStartBookingReminder = tcb.timeRecorder.needsStartBookingReminder();

      // Then
      assertThat(actualNeedsStartBookingReminder, is(expectedNeedsStartBookingReminder));
   }

   @Test
   void testNeedsStartBookingReminder_ComeAndGoAlreadyRecorded_StartBookingReminderNotNecessary() {
      // Given
      boolean expectedNeedsStartBookingReminder = false;
      TestCaseBuilder tcb = new TestCaseBuilder()
            .withComeAndGo()
            .withComeAndGo()
            .withFlagComeAndGoAsRecorded()
            .build();

      // When
      boolean actualNeedsStartBookingReminder = tcb.timeRecorder.needsStartBookingReminder();

      // Then
      assertThat(actualNeedsStartBookingReminder, is(expectedNeedsStartBookingReminder));
   }

   @Test
   void testNeedsStartBookingReminder_ComeAndGoNotRecorded_StartBookingReminderNecessary() {
      // Given
      boolean expectedNeedsStartBookingReminder = true;
      TestCaseBuilder tcb = new TestCaseBuilder()
            .withComeAndGo()
            .withComeAndGo()
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

      private TestCaseBuilder() {
         this.businessDay = spy(new BusinessDayImpl(ComeAndGoesImpl.of()));
      }

      private TestCaseBuilder withComeAndGo() {
         businessDay = businessDay.comeOrGo();
         return this;
      }

      private TestCaseBuilder withTotalDuration(float totalDuration) {
         when(businessDay.getTotalDuration()).thenReturn(totalDuration);
         return this;
      }

      private TestCaseBuilder withFlagComeAndGoAsRecorded() {
         businessDay = businessDay.flagComeAndGoesAsRecorded();
         return this;
      }

      private TestCaseBuilder withNotChargedElement(boolean hasNotChargedElements) {
         when(businessDay.hasNotChargedElements()).thenReturn(hasNotChargedElements);
         return this;
      }

      private TestCaseBuilder build() {
         BusinessDayRepository businessDayRepository = mockBusinessDayRepository(businessDay);
         this.timeRecorder = new TimeRecorderImpl(mock(BookerAdapter.class), businessDayRepository, mock(BookBusinessDayMessageSender.class));
         this.timeRecorder.setCallbackHandler(mock(UiCallbackHandler.class));
         this.timeRecorder.init();
         return this;
      }
   }
}
