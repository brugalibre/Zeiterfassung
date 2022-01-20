/**
 * 
 */
package com.adcubum.timerecording.core.callbackhandler;

import com.adcubum.timerecording.message.Message;

/**
 * 
 * The {@link UiCallbackHandler} acts as a bridge between the ui-layer resp. the system tray and the TimeRecorder-Application
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

   /**
    * Is called whenever a user has 'come'. This means, the user is marked as present at work
    */
   public void onCome();

   /**
    * Is called whenever a user has 'gone'. This means, the user is marked as afk
    */
   public void onGo();

   /**
    * Is called whenever the state of the BusinessDas has changed
    */
   default void onBusinessDayChanged(){
      // empty
   }
}
