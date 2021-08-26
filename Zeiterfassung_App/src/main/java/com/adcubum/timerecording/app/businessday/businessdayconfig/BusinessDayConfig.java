package com.adcubum.timerecording.app.businessday.businessdayconfig;

import static java.util.Objects.isNull;

import com.adcubum.timerecording.settings.Settings;
import com.adcubum.timerecording.settings.key.ValueKey;
import com.adcubum.timerecording.settings.key.ValueKeyFactory;

/**
 * The {@link BusinessDayConfig} defines the set amount of hours and knows also the actual working hours
 * It can therefore define the amount of percentage of work which is needs stil to be done
 * 
 * @author dstalder
 *
 */
public class BusinessDayConfig {

   private static final ValueKey<String> SET_HOURS_KEY = ValueKeyFactory.createNew("setWorkingHours", String.class);
   private float setHours;

   public BusinessDayConfig() {
      this(Settings.INSTANCE);
   }

   public BusinessDayConfig(Settings settings) {
      String setHoursAsString = settings.getSettingsValue(SET_HOURS_KEY);
      this.setHours = isNull(setHoursAsString) ? 8.5f : Float.parseFloat(setHoursAsString);
   }

   public float getSetHours() {
      return setHours;
   }

   public float getHoursLeft(float currentHours) {
      return Math.max(0, setHours - currentHours);
   }
}
