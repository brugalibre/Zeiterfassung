package com.adcubum.scheduler.startrecordingdobooking;


import static com.adcubum.scheduler.startrecordingdobooking.StartRecordingAndDoBookingReminderImpl.BEGIN_WORK_KEY;
import static com.adcubum.scheduler.startrecordingdobooking.StartRecordingAndDoBookingReminderImpl.END_WORK_KEY;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.BooleanSupplier;
import java.util.function.UnaryOperator;

import org.awaitility.Awaitility;
import org.junit.jupiter.api.Test;

import com.adcubum.timerecording.core.callbackhandler.UiCallbackHandler;
import com.adcubum.timerecording.message.Message;
import com.adcubum.timerecording.settings.round.RoundMode;
import com.adcubum.timerecording.work.date.DateTime;
import com.adcubum.timerecording.work.date.DateTimeFactory;
import com.adcubum.util.parser.DateParser;

class StartRecordingAndDoBookingReminderIntegrationTest {

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
      StartRecordingAndDoBookingReminderImpl startRecordingAndDoBookingReminder =
            new StartRecordingAndDoBookingReminderImpl(key -> key, needsEndReminder, needsEndReminder);

      // Then. For the sake of the test coverage
      assertThat(startRecordingAndDoBookingReminder, is(not(nullValue())));
   }

   private static class TestCaseBuilder {
      private StartRecordingAndDoBookingReminderImpl startRecordingAndDoBookingReminder;
      private TestUiCallbackHandler callbackHandler;
      private String beginTimeValueAsString;
      private String endTimeValueAsString;
      private BooleanSupplier needsStartReminder = () -> true;
      private BooleanSupplier needsEndReminder = () -> true;

      private TestCaseBuilder() {
         this.beginTimeValueAsString = null;
         this.endTimeValueAsString = null;
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
         DateTime nowPlusAdditionallyTime = DateTimeFactory.createNew(System.currentTimeMillis(), RoundMode.SEC).addSeconds(remindTime);
         this.beginTimeValueAsString = DateParser.parse2String(nowPlusAdditionallyTime, DateParser.HOUR_MIN_SEC_PATTERN);
         return this;
      }

      private TestCaseBuilder withEndRemindTime(int remindTime) {
         DateTime nowPlusAdditionallyTime = DateTimeFactory.createNew(System.currentTimeMillis(), RoundMode.SEC).addSeconds(remindTime);
         this.endTimeValueAsString = DateParser.parse2String(nowPlusAdditionallyTime, DateParser.HOUR_MIN_SEC_PATTERN);
         return this;
      }

      private TestCaseBuilder withTestUiCallbackHandler() {
         this.callbackHandler = new TestUiCallbackHandler();
         return this;
      }

      private TestCaseBuilder build() {
         UnaryOperator<String> settingsValueProvider = key -> {
            if (BEGIN_WORK_KEY.equals(key)) {
               return beginTimeValueAsString;
            } else if (END_WORK_KEY.equals(key)) {
               return endTimeValueAsString;
            }
            throw new IllegalStateException("Unknown key '" + key + "'");
         };
         this.startRecordingAndDoBookingReminder =
               new StartRecordingAndDoBookingReminderImpl(settingsValueProvider, needsStartReminder, needsEndReminder, 1);
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
