package com.adcubum.timerecording.settings.key;

public class ValueKeyImpl<T> implements ValueKey<T> {

   private Class<T> clazz;
   private String name;

   private ValueKeyImpl(String name, Class<T> clazz) {
      this.name = name;
      this.clazz = clazz;
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
