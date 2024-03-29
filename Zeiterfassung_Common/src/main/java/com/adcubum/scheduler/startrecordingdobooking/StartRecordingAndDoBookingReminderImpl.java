package com.adcubum.scheduler.startrecordingdobooking;

import java.util.concurrent.TimeUnit;
import java.util.function.BooleanSupplier;
import java.util.function.UnaryOperator;

import com.adcubum.librarys.text.res.TextLabel;
import com.adcubum.scheduler.Scheduler;
import com.adcubum.scheduler.SchedulerContainer;
import com.adcubum.scheduler.SchedulerContainerImpl;
import com.adcubum.scheduler.SchedulerImpl;
import com.adcubum.timerecording.core.callbackhandler.UiCallbackHandler;
import com.adcubum.timerecording.message.Message;
import com.adcubum.timerecording.message.MessageFactory;
import com.adcubum.timerecording.message.MessageType;

/**
 * A helper in order to create and initialize a {@link SchedulerContainerImpl} in order to remind
 * the user in the morning to start the recording and to do the booking in the evening.
 * Additionally this helper suppresses the reminder if a reminding became unnecessary
 * 
 * @author DStalder
 *
 */
public class StartRecordingAndDoBookingReminderImpl implements StartRecordingAndDoBookingReminder {

   static final String BEGIN_WORK_KEY = "beginWork";
   static final String END_WORK_KEY = "endWork";
   private BooleanSupplier needsStartReminder;
   private BooleanSupplier needsEndReminder;
   private UnaryOperator<String> settingsValueProvider;
   private TimeUnit timeUnits;
   private long reminderSleepInterval;

   /**
    * Creates a new {@link StartRecordingAndDoBookingReminderImpl} with two {@link BooleanSupplier} which determines
    * if a reminder is effectively necessary at the time the {@link Scheduler} is done
    * 
    * @param settingsValueProvider
    *        a interface in order to retrieve a stored value for a given key
    * @param needsStartReminder
    *        defines if a reminder to start is still necessary
    * @param needsEndReminder
    *        defines if a reminder to end is still necessary
    */
   public StartRecordingAndDoBookingReminderImpl(UnaryOperator<String> settingsValueProvider, BooleanSupplier needsStartReminder,
         BooleanSupplier needsEndReminder) {
      this(settingsValueProvider, needsStartReminder, needsEndReminder, 30);
   }

   /**
    * Creates a new {@link StartRecordingAndDoBookingReminderImpl} with two {@link BooleanSupplier} which determines
    * if a reminder is effectively necessary at the time the {@link Scheduler} is done
    * 
    * @param settingsValueProvider
    *        a interface in order to retrieve a stored value for a given key
    * @param needsStartReminder
    *        defines if a reminder to start is still necessary
    * @param needsEndReminder
    *        defines if a reminder to end is still necessary
    * @param reminderSleepInterval
    *        the sleeping interval of the created {@link SchedulerImpl}s
    */
   StartRecordingAndDoBookingReminderImpl(UnaryOperator<String> settingsValueProvider, BooleanSupplier needsStartReminder,
         BooleanSupplier needsEndReminder, long reminderSleepInterval) {
      this.needsStartReminder = needsStartReminder;
      this.needsEndReminder = needsEndReminder;
      this.settingsValueProvider = settingsValueProvider;
      this.timeUnits = TimeUnit.SECONDS;
      this.reminderSleepInterval = reminderSleepInterval;
   }

   /**
    * Creates and starts a new {@link SchedulerContainerImpl} and registers the given {@link UiCallbackHandler}
    * as callback for the created {@link SchedulerImpl}s
    * 
    * @param uiCallbackHandler
    *        the {@link UiCallbackHandler} as a callback to the ui
    */
   @Override
   public void initializeAndStartReminderContainer(UiCallbackHandler uiCallbackHandler) {
      String beginTimeAsString = settingsValueProvider.apply(BEGIN_WORK_KEY);
      String endTimeAsString = settingsValueProvider.apply(END_WORK_KEY);
      SchedulerContainer startBookingAndDoBookingReminder = new SchedulerContainerImpl(timeUnits, reminderSleepInterval);
      Runnable displayReminder2StartMsg =
            () -> displayMessage(uiCallbackHandler, TextLabel.REMINDER_TO_START_RECORDING_MSG, TextLabel.REMINDER_TO_START_RECORDING_TITLE, true);
      Runnable displayReminder2EndMsg =
            () -> displayMessage(uiCallbackHandler, TextLabel.REMINDER_TO_BOOK_MSG, TextLabel.REMINDER_TO_BOOK_TITLE, false);
      startBookingAndDoBookingReminder.addScheduler(displayReminder2StartMsg, beginTimeAsString);
      startBookingAndDoBookingReminder.addScheduler(displayReminder2EndMsg, endTimeAsString);
      startBookingAndDoBookingReminder.start();
   }

   private void displayMessage(UiCallbackHandler callbackHandler, String msg, String msgTitle, boolean reminder2StartOrStop) {
      if (isReminder2StartAndHasStarted(reminder2StartOrStop)
            || isReminder2BookAndHasNoContent(reminder2StartOrStop)) {
         return;// no reminder to start, if we are already recording or has started to or to book, if there is no content to book
      }
      Message reminderMsg = MessageFactory.createNew(MessageType.INFORMATION, msg, msgTitle);
      callbackHandler.displayMessage(reminderMsg);
   }

   private boolean isReminder2StartAndHasStarted(boolean reminder2StartOrStop) {
      return reminder2StartOrStop && !needsStartReminder.getAsBoolean();
   }

   private boolean isReminder2BookAndHasNoContent(boolean reminder2StartOrStop) {
      return !reminder2StartOrStop && !needsEndReminder.getAsBoolean();
   }
}
