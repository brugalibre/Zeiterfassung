package com.adcubum.scheduler.startrecordingdobooking;

import com.adcubum.scheduler.SchedulerContainer;
import com.adcubum.timerecording.core.callbackhandler.UiCallbackHandler;

/**
 * A helper in order to create and initialize a {@link SchedulerContainer} in order to remind
 * the user in the morning to start the recording and to do the booking in the evening.
 * Additionally this helper suppresses the reminder if a reminding became unnecessary
 * 
 * @author DStalder
 *
 */
public interface StartRecordingAndDoBookingReminder {

   /**
    * Creates and starts a new {@link SchedulerContainerImpl} and registers the given {@link UiCallbackHandler}
    * as callback for the created {@link SchedulerImpl}s
    * 
    * @param uiCallbackHandler
    *        the {@link UiCallbackHandler} as a callback to the ui
    */
   void initializeAndStartReminderContainer(UiCallbackHandler uiCallbackHandler);

}
