/**
 * 
 */
package com.myownb3.dominic.launch;

import java.util.Map;

import javax.swing.SwingUtilities;

import com.myownb3.dominic.librarys.PictureLibrary;
import com.myownb3.dominic.timerecording.app.TimeRecorder;
import com.myownb3.dominic.timerecording.callback.Callback;
import com.myownb3.dominic.timerecording.callback.actions.CallbackActions;
import com.myownb3.dominic.timerecording.callback.handler.CallbackHandler;
import com.myownb3.dominic.ui.app.TimeRecordingTray;
import com.myownb3.dominic.util.exception.GlobalExceptionHandler;

/**
 * @author Dominic
 * @version {@value TimeRecorder#VERSION}
 */
public class TimeRecordingLauncher {
    /**
     * @param args
     */
    public static void main(String[] args) {
	Runtime.getRuntime().addShutdownHook(createShutdownHook());

	TimeRecordingTray timeRecordingTray = new TimeRecordingTray();
	CallbackHandler callbackHandler = getCallbackHandler(timeRecordingTray);

	TimeRecorder.setCallbackHandler(callbackHandler);
	GlobalExceptionHandler.registerHandler(callbackHandler);

	PictureLibrary.loadPictures();
	SwingUtilities.invokeLater(() -> timeRecordingTray.registerSystemtray());
    }

    /**
     * @param timeRecordingTray
     * @return
     */
    private static CallbackHandler getCallbackHandler(TimeRecordingTray timeRecordingTray) {
	return callbacks -> {

	    for (Callback callback : callbacks) {

		Map<String, Object> callbackInfos = callback.getCallbackInfos();
		String callbackType = (String) callbackInfos.get(CallbackActions.CALLBACK_TYPE);

		if (callbackType == CallbackActions.START || callbackType == CallbackActions.RESUME) {
		    timeRecordingTray.startWorking();
		} else if (callbackType == CallbackActions.STOP) {
		    timeRecordingTray.stopWorking();
		} else if (callbackType == CallbackActions.SHOW_EXCEPTION) {
		    Thread thread = (Thread) callbackInfos.get(GlobalExceptionHandler.THREAD_NAME);
		    Throwable thrown = (Throwable) callbackInfos.get(GlobalExceptionHandler.THROWABLE);
		    timeRecordingTray.showException(thread, thrown);
		} else {
		    throw new IllegalArgumentException("Unknown CallbackAction '" + callbackType);
		}
	    }
	};
    }

    /*
     * create a shutdown hook whose export the current and maybe not yet
     * exported BusinessDay in case the application is shutdown by accident
     */
    private static Thread createShutdownHook() {
	return new Thread(() -> {
	    if (TimeRecorder.hasContent()) {
		TimeRecorder.export();
	    }
	});
    }
}
