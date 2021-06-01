package com.adcubum.scheduler.startrecordingdobooking;


import static com.adcubum.scheduler.startrecordingdobooking.StartRecordingAndDoBookingReminder.BEGIN_WORK_KEY;
import static com.adcubum.scheduler.startrecordingdobooking.StartRecordingAndDoBookingReminder.END_WORK_KEY;
import static java.util.Objects.nonNull;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.BooleanSupplier;

import org.awaitility.Awaitility;
import org.junit.jupiter.api.Test;

import com.adcubum.timerecording.core.callbackhandler.UiCallbackHandler;
import com.adcubum.timerecording.message.Message;
import com.adcubum.timerecording.settings.Settings;
import com.adcubum.timerecording.settings.round.RoundMode;
import com.adcubum.timerecording.test.BaseTestWithSettings;
import com.adcubum.timerecording.work.date.Time;
import com.adcubum.util.parser.DateParser;

class StartRecordingAndDoBookingReminderIntegrationTest extends BaseTestWithSettings {

   @Test
   void testInitializeReminderListener_WithBeginReminder() {
      // Given
      TestCaseBuilder tcb = new TestCaseBuilder()
            .withTestUiCallbackHandler()
            .withBeginRemindTime(2)
            .build();

      // When
      tcb.startRecordingAndDoBookingReminder.initializeAndStartReminderContainer(tcb.callbackHandler);
      Awaitility.await().atMost(3, TimeUnit.SECONDS).until(() -> tcb.callbackHandler.wasInvoked.get());

      // Then
      assertThat(tcb.callbackHandler.wasInvoked.get(), is(true));
   }

   @Test
   void testInitializeReminderListener_WithEndReminder() {
      // Given
      TestCaseBuilder tcb = new TestCaseBuilder()
            .withTestUiCallbackHandler()
            .withEndRemindTime(2)
            .build();

      // When
      tcb.startRecordingAndDoBookingReminder.initializeAndStartReminderContainer(tcb.callbackHandler);
      Awaitility.await().atMost(3, TimeUnit.SECONDS).until(() -> tcb.callbackHandler.wasInvoked.get());

      // Then
      assertThat(tcb.callbackHandler.wasInvoked.get(), is(true));
   }

   @Test
   void testInitializeReminderListener_WithBeginReminderNoRemindNecessary() throws InterruptedException {
      // Given
      TestCaseBuilder tcb = new TestCaseBuilder()
            .withTestUiCallbackHandler()
            .withBeginRemindTime(2)
            .withNeedsBeginReminder(() -> false)
            .build();

      // When
      tcb.startRecordingAndDoBookingReminder.initializeAndStartReminderContainer(tcb.callbackHandler);
      TimeUnit.SECONDS.sleep(3);

      // Then
      assertThat(tcb.callbackHandler.wasInvoked.get(), is(false));
   }

   @Test
   void testInitializeReminderListener_WithEndReminderNoRemindNecessary() throws InterruptedException {
      // Given
      TestCaseBuilder tcb = new TestCaseBuilder()
            .withTestUiCallbackHandler()
            .withEndRemindTime(2)
            .withNeedsEndReminder(() -> false)
            .build();

      // When
      tcb.startRecordingAndDoBookingReminder.initializeAndStartReminderContainer(tcb.callbackHandler);
      TimeUnit.SECONDS.sleep(3);

      // Then
      assertThat(tcb.callbackHandler.wasInvoked.get(), is(false));
   }

   @Test
   void testCreateDefaultStartReordingAndDoBookingReminder() {

      // Given
      BooleanSupplier needsEndReminder = () -> true;

      // When
      StartRecordingAndDoBookingReminder startRecordingAndDoBookingReminder =
            new StartRecordingAndDoBookingReminder(needsEndReminder, needsEndReminder);

      // Then. For the sake of the test coverage
      assertThat(startRecordingAndDoBookingReminder, is(not(nullValue())));
   }

   private static class TestCaseBuilder {
      private StartRecordingAndDoBookingReminder startRecordingAndDoBookingReminder;
      private TestUiCallbackHandler callbackHandler;
      private String beginTimeValueAsString;
      private String endTimeValueAsString;
      private BooleanSupplier needsStartReminder = () -> true;
      private BooleanSupplier needsEndReminder = () -> true;

      private TestCaseBuilder() {
         // private
      }

      private TestCaseBuilder withNeedsBeginReminder(BooleanSupplier needsStartReminder) {
         this.needsStartReminder = needsStartReminder;
         return this;
      }

      private TestCaseBuilder withNeedsEndReminder(BooleanSupplier needsEndReminder) {
         this.needsEndReminder = needsEndReminder;
         return this;
      }

      private TestCaseBuilder withBeginRemindTime(int remindTime) {
         Time nowPlusAdditionallyTime = new Time(System.currentTimeMillis(), RoundMode.SEC).addSeconds(remindTime);
         this.beginTimeValueAsString = DateParser.parse2String(nowPlusAdditionallyTime, DateParser.HOUR_MIN_SEC_PATTERN);
         return this;
      }

      private TestCaseBuilder withEndRemindTime(int remindTime) {
         Time nowPlusAdditionallyTime = new Time(System.currentTimeMillis(), RoundMode.SEC).addSeconds(remindTime);
         this.endTimeValueAsString = DateParser.parse2String(nowPlusAdditionallyTime, DateParser.HOUR_MIN_SEC_PATTERN);
         return this;
      }

      private TestCaseBuilder withTestUiCallbackHandler() {
         this.callbackHandler = new TestUiCallbackHandler();
         return this;
      }

      private TestCaseBuilder build() {
         if (nonNull(beginTimeValueAsString)) {
            saveProperty2Settings(BEGIN_WORK_KEY, beginTimeValueAsString);
         }
         if (nonNull(endTimeValueAsString)) {
            saveProperty2Settings(END_WORK_KEY, endTimeValueAsString);
         }
         this.startRecordingAndDoBookingReminder = new StartRecordingAndDoBookingReminder(Settings.INSTANCE, needsStartReminder, needsEndReminder, 1);
         return this;
      }
   }

   private static class TestUiCallbackHandler implements UiCallbackHandler {

      private AtomicBoolean wasInvoked;

      private TestUiCallbackHandler() {
         wasInvoked = new AtomicBoolean(false);
      }

      @Override
      public void onStop() {}

      @Override
      public void onStart() {}

      @Override
      public void onResume() {}

      @Override
      public void onException(Throwable throwable, Thread t) {}

      @Override
      public void displayMessage(Message message) {
         wasInvoked.set(true);
      }

      @Override
      public void onCome() {}

      @Override
      public void onGo() {
         // TODO Auto-generated method stub

      }

   }
}
