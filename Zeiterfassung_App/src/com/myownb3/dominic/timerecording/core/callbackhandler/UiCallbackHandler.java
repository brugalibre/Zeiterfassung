/**
 * 
 */
package com.myownb3.dominic.timerecording.core.callbackhandler;

import com.myownb3.dominic.timerecording.app.TimeRecorder;
import com.myownb3.dominic.timerecording.core.message.Message;

/**
 * 
 * The {@link UiCallbackHandler} acts as a bridge between the ui-layer resp. the system tray and the {@link TimeRecorder}
 * 
 * @author Dominic
 *
 */
public interface UiCallbackHandler {

   /**
    * Stops a running recording
    */
   public void onStop();

   /**
    * Starts a new recording
    */
   public void onStart();

   /**
    * Resumes a previously stopped recording
    */
   public void onResume();

   /**
    * Is called when ever a caught throwable is propagated to the ui
    * 
    * @param throwable
    *        the caught {@link Throwable}
    * @param t
    *        the {@link Thread} which caught it
    */
   public void onException(Throwable throwable, Thread t);

   /**
    * Shows the given message
    * 
    * @param message
    *        the message to show
    */
   public void displayMessage(Message message);
}
