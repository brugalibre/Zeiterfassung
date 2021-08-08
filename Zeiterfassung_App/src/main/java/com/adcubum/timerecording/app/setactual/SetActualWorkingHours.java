package com.adcubum.timerecording.app.setactual;

import static java.util.Objects.isNull;

import com.adcubum.librarys.text.res.TextLabel;
import com.adcubum.timerecording.settings.Settings;
import com.adcubum.timerecording.settings.key.ValueKey;
import com.adcubum.timerecording.settings.key.ValueKeyFactory;

/**
 * The {@link SetActualWorkingHours} defines the set amount of hours and knows also the actual working hours
 * It can therefore define the amount of percentage of work which is needs stil to be done
 * 
 * @author dstalder
 *
 */
public class SetActualWorkingHours {

   private static final ValueKey<String> SET_HOURS_KEY = ValueKeyFactory.createNew("setWorkingHours", String.class);
   private String title;
   private String setHoursLabel;
   private String actualHoursLabel;
   private float setHours;
   private float hoursLeft;

   public SetActualWorkingHours() {
      this(Settings.INSTANCE);
   }

   public SetActualWorkingHours(Settings settings) {
      String setHoursAsString = settings.getSettingsValue(SET_HOURS_KEY);
      this.setHours = isNull(setHoursAsString) ? 0 : Float.parseFloat(setHoursAsString);
      this.title = TextLabel.SET_ACTUAL_HOURS_TITLE;
      this.setHoursLabel = TextLabel.SET_HOURS_LABEL;
      this.actualHoursLabel = TextLabel.ACTUAL_HOURS_LABEL;
   }

   public float getSetHours() {
      return setHours;
   }

   public String getTitle() {
      return title;
   }

   public float getHoursLeft(float currentHours) {
      return Math.max(0, setHours - currentHours);
   }

   public String getSetHoursLabel() {
      return setHoursLabel;
   }

   public String getActualHoursLabel() {
      return actualHoursLabel;
   }
}
