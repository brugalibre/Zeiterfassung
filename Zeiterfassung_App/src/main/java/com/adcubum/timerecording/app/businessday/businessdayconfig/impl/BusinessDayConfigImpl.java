package com.adcubum.timerecording.app.businessday.businessdayconfig.impl;

import static java.util.Objects.isNull;

import com.adcubum.timerecording.app.businessday.businessdayconfig.BusinessDayConfig;
import com.adcubum.timerecording.settings.Settings;
import com.adcubum.timerecording.settings.key.ValueKey;
import com.adcubum.timerecording.settings.key.ValueKeyFactory;

public class BusinessDayConfigImpl implements BusinessDayConfig {

   private static final ValueKey<Float> SET_HOURS_KEY = ValueKeyFactory.createNew("setWorkingHours", Float.class, 8.5f);
   private float setHours;

   public BusinessDayConfigImpl() {
      this(Settings.INSTANCE);
   }

   public BusinessDayConfigImpl(Settings settings) {
      this.setHours = settings.getSettingsValue(SET_HOURS_KEY);
   }

   @Override
   public float getSetHours() {
      return setHours;
   }

   @Override
   public float getHoursLeft(float currentHours) {
      return Math.max(0, setHours - currentHours);
   }
}
