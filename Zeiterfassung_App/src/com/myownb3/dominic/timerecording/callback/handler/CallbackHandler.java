/**
 * 
 */
package com.myownb3.dominic.timerecording.callback.handler;

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
}
