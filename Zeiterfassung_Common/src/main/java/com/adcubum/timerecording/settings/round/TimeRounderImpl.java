/**
 * 
 */
package com.adcubum.timerecording.settings.round;

import static com.adcubum.timerecording.settings.common.Const.ZEITERFASSUNG_PROPERTIES;
import static java.util.Objects.nonNull;

import com.adcubum.timerecording.settings.Settings;

/**
 * The {@link TimeRounderImpl} is responsible for loading and storing of the current {@link RoundMode}
 * 
 * @author Dominic
 *
 */
public class TimeRounderImpl implements TimeRounder {

   private Settings settings;
   private RoundMode roundMode;

   /**
    * Constructor only for testing purpose
    * 
    * @param settings
    *        the Settings
    */
   TimeRounderImpl(Settings settings) {
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

   @Override
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

   @Override
   public final RoundMode getRoundMode() {
      return this.roundMode;
   }
}
