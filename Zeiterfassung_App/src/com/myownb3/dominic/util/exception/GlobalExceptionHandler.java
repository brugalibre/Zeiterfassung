/**
 * 
 */
package com.myownb3.dominic.util.exception;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import com.myownb3.dominic.timerecording.callback.actions.CallbackActions;
import com.myownb3.dominic.timerecording.callback.handler.CallbackHandler;

/**
 * @author Dominic
 * 
 */
public class GlobalExceptionHandler implements Thread.UncaughtExceptionHandler {

    public static final String THREAD_NAME = "THREAD_NAME";
    public static final String THROWABLE = "THROWABLE";
    private static CallbackHandler callbackHandler;

    public void handle(Throwable thrown) {
	// for EDT exceptions
	uncaughtException(Thread.currentThread(), thrown);
    }

    @Override
    public void uncaughtException(Thread t, Throwable e) {
	handleGlobalException(t, e);
    }

    /**
     * Handles an uncaught Exception thrown within the JVM or the EDT
     * 
     * @param Thread
     *            , thread which is has thrown the Exception
     * @param thrown
     *            , thrown object (an {@link Exception} or a {@link Throwable})
     * @param callbackHandler
     */
    public static void handleGlobalException(Thread thread, Throwable thrown) {
	Objects.requireNonNull(callbackHandler,
		"We need a CallbackHandler in order to propage the error '" + thrown + "'");
	Map<String, Object> infoMap = new HashMap<>();
	infoMap.put(THREAD_NAME, thread);
	infoMap.put(CallbackActions.CALLBACK_TYPE, CallbackActions.SHOW_EXCEPTION);
	infoMap.put(THROWABLE, thrown);
	callbackHandler.handleCallbacks(() -> infoMap);
    }

    /**
    * 
    */
    public static void registerHandler(CallbackHandler callbackHandler) {
	GlobalExceptionHandler.callbackHandler = callbackHandler;
	Thread.setDefaultUncaughtExceptionHandler(new GlobalExceptionHandler());
	System.setProperty("sun.awt.exception.handler", GlobalExceptionHandler.class.getName());
    }
}
