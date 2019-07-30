/**
 * 
 */
package com.myownb3.dominic.launch;

import static com.myownb3.dominic.timerecording.settings.common.Const.TURBO_BUCHER_PROPERTIES;

import java.io.File;
import java.io.IOException;

import com.myownb3.dominic.launch.exception.ApplicationLaunchException;
import com.myownb3.dominic.librarys.pictures.PictureLibrary;
import com.myownb3.dominic.timerecording.app.TimeRecorder;
import com.myownb3.dominic.timerecording.core.callbackhandler.CallbackHandler;
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
	CallbackHandler callbackHandler = timeRecordingTray.getCallbackHandler();

	TimeRecorder.INSTANCE.setCallbackHandler(callbackHandler);
	GlobalExceptionHandler.registerHandler(callbackHandler);

	initApplication();
	Platform.runLater(() -> timeRecordingTray.registerSystemtray(primaryStage));
    }

    private void initApplication() {
	PictureLibrary.loadPictures();
	Platform.setImplicitExit(false);
	createTurboBucherPropertiesFileIfNotExists();
    }

    private void createTurboBucherPropertiesFileIfNotExists() {
	File file = new File(TURBO_BUCHER_PROPERTIES);
	if (!file.exists()) {
	    try {
		file.createNewFile();
	    } catch (IOException e) {
		throw new ApplicationLaunchException(e);
	    }
	}
    }

    /*
     * create a shutdown hook whose export the current and maybe not yet exported
     * BusinessDay in case the application is shutdown by accident
     */
    private static Thread createShutdownHook() {
	return new Thread(() -> {
	    if (TimeRecorder.INSTANCE.hasContent()) {
		TimeRecorder.INSTANCE.export();
	    }
	});
    }
}
