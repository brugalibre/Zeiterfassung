/**
 * 
 */
package com.adcubum.timerecording.launch;

import static com.adcubum.timerecording.settings.common.Const.TURBO_BUCHER_PROPERTIES;
import static com.adcubum.timerecording.settings.common.Const.ZEITERFASSUNG_PROPERTIES;

import java.io.File;
import java.io.IOException;
import java.util.function.BooleanSupplier;

import com.adcubum.scheduler.startrecordingdobooking.StartRecordingAndDoBookingReminder;
import com.adcubum.timerecording.app.TimeRecorder;
import com.adcubum.timerecording.core.callbackhandler.UiCallbackHandler;
import com.adcubum.timerecording.launch.exception.ApplicationLaunchException;
import com.adcubum.timerecording.librarys.pictures.PictureLibrary;
import com.adcubum.timerecording.ui.app.TimeRecordingTray;
import com.adcubum.timerecording.ui.security.login.auth.UiAuthenticationService;
import com.adcubum.util.exception.GlobalExceptionHandler;
import com.adcubum.util.utils.FileSystemUtil;

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
   public void start(Stage primaryStage) {
      initApplication();

      TimeRecordingTray timeRecordingTray = new TimeRecordingTray();
      UiCallbackHandler callbackHandler = timeRecordingTray.getCallbackHandler();
      TimeRecorder.INSTANCE.setCallbackHandler(callbackHandler);
      GlobalExceptionHandler.registerHandler(callbackHandler);

      timeRecordingTray.registerSystemtray(primaryStage);
      registerReminderListener(callbackHandler);
      UiAuthenticationService.doUserAuthentication(timeRecordingTray);
   }

   private static void registerReminderListener(UiCallbackHandler callbackHandler) {
      StartRecordingAndDoBookingReminder startEndReminderHelper =
            new StartRecordingAndDoBookingReminder(needsStartReminder(), TimeRecorder.INSTANCE::hasContent);
      startEndReminderHelper.initializeAndStartReminderContainer(callbackHandler);
   }

   private static BooleanSupplier needsStartReminder() {
      return () -> !TimeRecorder.INSTANCE.isRecordindg() && !TimeRecorder.INSTANCE.hasContent();
   }

   private void initApplication() {
      Runtime.getRuntime().addShutdownHook(createShutdownHook());
      PictureLibrary.loadPictures();
      Platform.setImplicitExit(false);
      createPropertieFileIfNotExists(TURBO_BUCHER_PROPERTIES);
      createPropertieFileIfNotExists(ZEITERFASSUNG_PROPERTIES);
   }

   private void createPropertieFileIfNotExists(String propertiesFileName) {
      File file = new File(propertiesFileName);
      if (!file.exists()) {
         try {
            if (!file.createNewFile()) {
               throw new ApplicationLaunchException("Unable to create the '" + propertiesFileName + "' file!");
            }
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
      String homeDir = FileSystemUtil.getHomeDir();
      return new Thread(() -> {
         if (TimeRecorder.INSTANCE.hasContent()) {
            TimeRecorder.INSTANCE.exportSilently(homeDir);
         }
      });
   }
}
