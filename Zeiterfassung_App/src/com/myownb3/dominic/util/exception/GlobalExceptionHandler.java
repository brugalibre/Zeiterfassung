/**
 * 
 */
package com.myownb3.dominic.util.exception;

import java.util.Objects;

import com.myownb3.dominic.timerecording.callback.handler.CallbackHandler;

/**
 * @author Dominic
 * 
 */
public class GlobalExceptionHandler implements Thread.UncaughtExceptionHandler {

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
     * @param Thread          , thread which is has thrown the Exception
     * @param thrown          , thrown object (an {@link Exception} or a
     *                        {@link Throwable})
     * @param callbackHandler
     */
    public static void handleGlobalException(Thread thread, Throwable thrown) {
	Objects.requireNonNull(callbackHandler,
		"We need a CallbackHandler in order to propage the error '" + thrown + "'");
	callbackHandler.onException(thrown, thread);
    }

    /**
     * Registers the given {@link CallbackHandler} as the handler which is used to
     * do call backs if any error occurs
     * 
     * @param callbackHandler the callback handler
     */
    public static void registerHandler(CallbackHandler callbackHandler) {
	GlobalExceptionHandler.callbackHandler = callbackHandler;
	Thread.setDefaultUncaughtExceptionHandler(new GlobalExceptionHandler());
	System.setProperty("sun.awt.exception.handler", GlobalExceptionHandler.class.getName());
    }
}
