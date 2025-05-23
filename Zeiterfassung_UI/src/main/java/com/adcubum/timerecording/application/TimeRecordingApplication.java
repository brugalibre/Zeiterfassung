/**
 * 
 */
package com.adcubum.timerecording.application;

import com.adcubum.scheduler.startrecordingdobooking.StartRecordingAndDoBookingReminder;
import com.adcubum.scheduler.startrecordingdobooking.StartRecordingAndDoBookingReminderFactory;
import com.adcubum.timerecording.app.TimeRecorder;
import com.adcubum.timerecording.core.callbackhandler.UiCallbackHandler;
import com.adcubum.timerecording.librarys.pictures.PictureLibrary;
import com.adcubum.timerecording.settings.round.TimeRounder;
import com.adcubum.timerecording.settings.round.constant.RoundModeConst;
import com.adcubum.timerecording.settings.round.observable.RoundModeChangedListener;
import com.adcubum.timerecording.ui.app.TimeRecordingTray;
import com.adcubum.timerecording.ui.security.login.auth.UiAuthenticationService;
import com.adcubum.util.exception.GlobalExceptionHandler;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

import java.io.File;
import java.io.IOException;
import java.util.function.BooleanSupplier;
import java.util.function.UnaryOperator;

import static com.adcubum.timerecording.settings.common.Const.ZEITERFASSUNG_PROPERTIES;

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
      UnaryOperator<String> settingsValueProvider = TimeRecorder.INSTANCE::getSettingsValue;
      StartRecordingAndDoBookingReminder startEndReminderHelper =
            StartRecordingAndDoBookingReminderFactory.createNew(settingsValueProvider, needsStartReminder(), needsEndReminder());
      startEndReminderHelper.initializeAndStartReminderContainer(callbackHandler);
   }

   private static BooleanSupplier needsStartReminder() {
      return TimeRecorder.INSTANCE::needsStartRecordingReminder;
   }

   private static BooleanSupplier needsEndReminder() {
      return TimeRecorder.INSTANCE::needsStartBookingReminder;
   }

   private void initApplication() {
      PictureLibrary.loadPictures();
      Platform.setImplicitExit(false);
      createPropertieFileIfNotExists(ZEITERFASSUNG_PROPERTIES);

      UiAuthenticationService.prepare();
      TimeRecorder.INSTANCE.init();
      initTimeRounder();
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

   private static void initTimeRounder() {
      RoundModeChangedListener saveChangedTimeRoundValue =
            (oldVal, newVal) -> TimeRecorder.INSTANCE.saveSettingValue(String.valueOf(newVal.getAmount()), RoundModeConst.SETTINGS_ROUND_KEY);
      TimeRounder.INSTANCE.init(TimeRecorder.INSTANCE::getSettingsValue);
      TimeRounder.INSTANCE.addRoundModeChangedListener(saveChangedTimeRoundValue);
   }
}
