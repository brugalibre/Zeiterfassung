/**
 * 
 */
package com.adcubum.timerecording.application;

import java.util.function.BooleanSupplier;
import java.util.function.Function;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

import com.adcubum.scheduler.startrecordingdobooking.StartRecordingAndDoBookingReminder;
import com.adcubum.scheduler.startrecordingdobooking.StartRecordingAndDoBookingReminderFactory;
import com.adcubum.timerecording.app.TimeRecorder;
import com.adcubum.timerecording.core.callbackhandler.UiCallbackHandler;
import com.adcubum.timerecording.librarys.pictures.PictureLibrary;
import com.adcubum.timerecording.settings.round.TimeRounder;
import com.adcubum.timerecording.settings.round.observable.RoundModeChangedListener;
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
public class TimeRecordingApplication extends Application {

   private ConfigurableApplicationContext applicationContext;

   @Override
   public void init() {
      String[] args = getParameters().getRaw().toArray(new String[0]);
      this.applicationContext = new SpringApplicationBuilder()
            .sources(TimeRecordingApplicationHelper.getSourceClass(args))
            .headless(false)
            .run(args);
   }

   @Override
   public void stop() {
      this.applicationContext.close();
      Platform.exit();
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
      Function<String, String> settingsValueProvider = key -> TimeRecorder.INSTANCE.getSettingsValue(key);
      StartRecordingAndDoBookingReminder startEndReminderHelper =
            StartRecordingAndDoBookingReminderFactory.createNew(settingsValueProvider, needsStartReminder(), needsEndReminder());
      startEndReminderHelper.initializeAndStartReminderContainer(callbackHandler);
   }

   private static BooleanSupplier needsStartReminder() {
      return () -> !TimeRecorder.INSTANCE.isRecordindg() && !TimeRecorder.INSTANCE.hasContent();
   }

   private static BooleanSupplier needsEndReminder() {
      return TimeRecorder.INSTANCE::hasContent;
   }

   private void initApplication() {
      Runtime.getRuntime().addShutdownHook(createShutdownHook());
      PictureLibrary.loadPictures();
      Platform.setImplicitExit(false);

      UiAuthenticationService.prepare();
      TimeRecorder.INSTANCE.init();
      initTimeRounder();
   }

   private static void initTimeRounder() {
      RoundModeChangedListener saveChangedTimeRoundValue =
            (oldVal, newVal) -> TimeRecorder.INSTANCE.saveSettingValue(String.valueOf(newVal.getAmount()), "");
      TimeRounder.INSTANCE.init(TimeRecorder.INSTANCE::getSettingsValue);
      TimeRounder.INSTANCE.addRoundModeChangedListener(saveChangedTimeRoundValue);
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
