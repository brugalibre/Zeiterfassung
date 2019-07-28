/**
 * 
 */
package com.myownb3.dominic.timerecording.callback.handler;

import com.myownb3.dominic.timerecording.app.Message;

/**
 * @author Dominic
 *
 */
public interface CallbackHandler {

    /**
     * 
     */
    public void onStop();

    /**
     * 
     */
    public void onStart();

    /**
     * 
     */
    public void onResume();

    public void onException(Throwable throwable, Thread t);

    /**
     * Shows the given message
     * 
     * @param message the message to show
     */
    public void displayMessage(Message message);

    /**
     * Shows a message dialog to the user
     * 
     * @param message the given message to show
     */
    public void showMessageDialog(Message message);
}
