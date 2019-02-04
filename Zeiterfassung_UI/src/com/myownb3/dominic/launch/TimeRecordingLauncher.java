/**
 * 
 */
package com.myownb3.dominic.launch;

import javax.swing.SwingUtilities;

import com.myownb3.dominic.librarys.PictureLibrary;
import com.myownb3.dominic.timerecording.app.TimeRecorder;
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

	return new CallbackHandler() {

	    @Override
	    public void onStop() {
		timeRecordingTray.stopWorking();
	    }

	    @Override
	    public void onStart() {
		timeRecordingTray.startWorking();
	    }

	    @Override
	    public void onResume() {
		timeRecordingTray.startWorking();
	    }

	    @Override
	    public void onException(Throwable thrown, Thread thread) {
		timeRecordingTray.showException(thread, thrown);
	    }

	    @Override
	    public void onRefresh() {
		// TODO Auto-generated method stub

	    }
	};
    }

    /*
     * create a shutdown hook whose export the current and maybe not yet exported
     * BusinessDay in case the application is shutdown by accident
     */
    private static Thread createShutdownHook() {
	return new Thread(() -> {
	    if (TimeRecorder.hasContent()) {
		TimeRecorder.export();
	    }
	});
    }
}
