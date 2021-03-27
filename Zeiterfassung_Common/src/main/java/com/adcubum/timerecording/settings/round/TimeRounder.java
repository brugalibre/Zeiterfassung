/**
 * 
 */
package com.adcubum.timerecording.settings.round;

import static com.adcubum.timerecording.settings.common.Const.ZEITERFASSUNG_PROPERTIES;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Properties;

import com.adcubum.timerecording.settings.Settings;
import com.adcubum.timerecording.settings.round.exception.RounderStoreValueException;

/**
 * The {@link TimeRounder} is responsible for loading and storing of the current {@link RoundMode}
 * 
 * @author Dominic
 *
 */
public class TimeRounder {

   public static final TimeRounder INSTANCE = new TimeRounder();
   private RoundMode roundMode;

   private TimeRounder() {
      init();
   }

   public void init() {
      RoundMode newRoundMode = evalRoundMode();
      setRoundMode(newRoundMode);
   }

   private RoundMode evalRoundMode() {
      String roundModeAsString = Settings.INSTANCE.getSettingsValue(RoundMode.PROPERTY_KEY);
      return RoundMode.getRoundMode(roundModeAsString);
   }

   public void setRoundMode(RoundMode roundMode) {
      this.roundMode = roundMode;
      saveValueToProperties(roundMode);
   }

   private void saveValueToProperties(RoundMode roundMode) {
      Properties prop = new Properties();
      try (InputStream resourceStream = new FileInputStream(ZEITERFASSUNG_PROPERTIES)) {
         prop.load(resourceStream);
         prop.put(RoundMode.PROPERTY_KEY, String.valueOf(roundMode.getAmount()));
         try (FileOutputStream out = new FileOutputStream(ZEITERFASSUNG_PROPERTIES)) {
            prop.store(out, null);
         }
      } catch (Exception e) {
         e.printStackTrace();
         throw new RounderStoreValueException(e);
      }
   }

   public final RoundMode getRoundMode() {
      return this.roundMode;
   }
}
