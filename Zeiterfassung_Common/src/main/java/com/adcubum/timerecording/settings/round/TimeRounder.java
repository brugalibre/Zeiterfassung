/**
 * 
 */
package com.adcubum.timerecording.settings.round;

import static com.adcubum.timerecording.settings.common.Const.ZEITERFASSUNG_PROPERTIES;
import static java.util.Objects.nonNull;

import com.adcubum.timerecording.settings.Settings;

/**
 * The {@link TimeRounder} is responsible for loading and storing of the current {@link RoundMode}
 * 
 * @author Dominic
 *
 */
public class TimeRounder {

   public static final TimeRounder INSTANCE = new TimeRounder();
   private Settings settings;
   private RoundMode roundMode;

   private TimeRounder() {
      this(Settings.INSTANCE);
   }

   /**
    * Constructor only for testing purpose
    * 
    * @param settings
    *        the Settings
    */
   TimeRounder(Settings settings) {
      this.settings = settings;
      init();
   }

   private void init() {
      RoundMode newRoundMode = evalRoundMode();
      setRoundMode(newRoundMode);
   }

   private RoundMode evalRoundMode() {
      String roundModeAsString = settings.getSettingsValue(RoundMode.PROPERTY_KEY);
      return RoundMode.getRoundMode(roundModeAsString);
   }

   public void setRoundMode(RoundMode newRoundMode) {
      RoundMode oldRoundMode = this.roundMode;
      this.roundMode = newRoundMode;
      if (oldRoundMode != newRoundMode && nonNull(oldRoundMode)) {
         saveValueToProperties(newRoundMode);
      }
   }

   private void saveValueToProperties(RoundMode roundMode) {
      settings.saveValueToProperties(RoundMode.PROPERTY_KEY, String.valueOf(roundMode.getAmount()), ZEITERFASSUNG_PROPERTIES);
   }

   public final RoundMode getRoundMode() {
      return this.roundMode;
   }
}
