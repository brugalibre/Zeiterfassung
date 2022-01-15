package com.adcubum.timerecording.settings.key;

import com.adcubum.timerecording.settings.common.Const;

public class ValueKeyImpl<T> implements ValueKey<T> {

   private final String resourceName;
   private Class<T> clazz;
   private String name;

   private ValueKeyImpl(String name, Class<T> clazz) {
      this(name, Const.ZEITERFASSUNG_PROPERTIES, clazz);
   }

   private ValueKeyImpl(String name, String resourceName, Class<T> clazz) {
      this.name = name;
      this.resourceName = resourceName;
      this.clazz = clazz;
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
}
