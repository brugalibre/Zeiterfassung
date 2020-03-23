/**
 * 
 */
package com.myownb3.dominic.timerecording.core.callbackhandler;

import com.myownb3.dominic.timerecording.core.message.Message;

/**
 * @author Dominic
 *
 */
public interface CallbackHandler {

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

   public void onException(Throwable throwable, Thread t);

   /**
    * Shows the given message
    * 
    * @param message
    *        the message to show
    */
   public void displayMessage(Message message);
}
