package com.adcubum.timerecording.settings.key;

import com.adcubum.timerecording.settings.common.Const;

public class ValueKeyImpl<T> implements ValueKey<T> {

   private final String resourceName;
   private Class<T> clazz;
   private String name;
   private T defaultValue;

   private ValueKeyImpl(String name, Class<T> clazz, T defaultValue) {
      this(name, Const.ZEITERFASSUNG_PROPERTIES, clazz, defaultValue);
   }

   private ValueKeyImpl(String name, String resourceName, Class<T> clazz, T defaultValue) {
      this.name = name;
      this.resourceName = resourceName;
      this.clazz = clazz;
      this.defaultValue = defaultValue;
   }

   @Override
   public String getResourceName() {
      return resourceName;
   }

   @Override
   public Class<T> getType() {
      return clazz;
   }

   @Override
   public String getName() {
      return name;
   }

   @Override
   public T getDefault() {
      return defaultValue;
   }
}
