/**
 * 
 */
package com.myownb3.dominic.launch;

import com.myownb3.dominic.launch.exception.ApplicationLaunchException;
import com.myownb3.dominic.librarys.pictures.PictureLibrary;
import com.myownb3.dominic.timerecording.app.TimeRecorder;
import com.myownb3.dominic.timerecording.callback.handler.CallbackHandler;
import com.myownb3.dominic.ui.app.TimeRecordingTray;
import com.myownb3.dominic.util.exception.GlobalExceptionHandler;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;

/**
 * @author Dominic
 * @version {@value TimeRecorder#VERSION}
 */
public class TimeRecordingLauncher extends Application {

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage primaryStage) throws ApplicationLaunchException {
	Runtime.getRuntime().addShutdownHook(createShutdownHook());

	TimeRecordingTray timeRecordingTray = new TimeRecordingTray();
	CallbackHandler callbackHandler = getCallbackHandler(timeRecordingTray);

	TimeRecorder.setCallbackHandler(callbackHandler);
	GlobalExceptionHandler.registerHandler(callbackHandler);

	PictureLibrary.loadPictures();
	Platform.setImplicitExit(false);
	Platform.runLater(() -> timeRecordingTray.registerSystemtray(primaryStage));
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

//	    @Override
//	    public void onException(Throwable thrown, Thread thread) {
//		timeRecordingTray.showException(thread, thrown);
//	    }
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
