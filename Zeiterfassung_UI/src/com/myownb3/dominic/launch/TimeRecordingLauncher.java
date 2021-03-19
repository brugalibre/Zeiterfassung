/**
 * 
 */
package com.myownb3.dominic.launch;

import static com.myownb3.dominic.timerecording.settings.common.Const.TURBO_BUCHER_PROPERTIES;
import static com.myownb3.dominic.timerecording.settings.common.Const.ZEITERFASSUNG_PROPERTIES;

import java.io.File;
import java.io.IOException;

import com.myownb3.dominic.launch.exception.ApplicationLaunchException;
import com.myownb3.dominic.librarys.pictures.PictureLibrary;
import com.myownb3.dominic.timerecording.app.TimeRecorder;
import com.myownb3.dominic.timerecording.core.callbackhandler.UiCallbackHandler;
import com.myownb3.dominic.ui.app.TimeRecordingTray;
import com.myownb3.dominic.ui.security.login.auth.UiAuthenticationService;
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
      setSystemProperties();
      launch();
   }

   @Override
   public void start(Stage primaryStage) {
      setSystemProperties();
      initApplication();

      TimeRecordingTray timeRecordingTray = new TimeRecordingTray();
      UiCallbackHandler callbackHandler = timeRecordingTray.getCallbackHandler();

      TimeRecorder.INSTANCE.setCallbackHandler(callbackHandler);
      GlobalExceptionHandler.registerHandler(callbackHandler);

      timeRecordingTray.registerSystemtray(primaryStage);
      UiAuthenticationService.doUserAuthentication(timeRecordingTray);
   }

   private void initApplication() {
      Runtime.getRuntime().addShutdownHook(createShutdownHook());
      PictureLibrary.loadPictures();
      Platform.setImplicitExit(false);
      createPropertieFileIfNotExists(ZEITERFASSUNG_PROPERTIES);
      createPropertieFileIfNotExists(TURBO_BUCHER_PROPERTIES);
   }

   private static void setSystemProperties() {
      System.setProperty("prism.order", "sw");
      System.setProperty("prism.verbose", "true");
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
      return new Thread(() -> {
         if (TimeRecorder.INSTANCE.hasContent()) {
            TimeRecorder.INSTANCE.export();
         }
      });
   }
}
