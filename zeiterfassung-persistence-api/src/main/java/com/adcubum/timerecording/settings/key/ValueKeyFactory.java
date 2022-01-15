package com.adcubum.timerecording.settings.key;

import com.adcubum.timerecording.core.factory.AbstractFactory;

/**
 * The {@link ValueKeyFactory} is used to create and instantiate new {@link ValueKey} instances
 * 
 * @author DStalder
 *
 */
public class ValueKeyFactory extends AbstractFactory {
   private static final String BEAN_NAME = "valuekey";
   private static final ValueKeyFactory INSTANCE = new ValueKeyFactory();

   private ValueKeyFactory() {
      super("persistence-module-configuration.xml");
   }

   /**
    * Creates a new Instance of a new {@link ValueKey} with the given arguments
    * 
    * @return a new Instance of a new {@link ValueKey} with the given arguments
    */
   public static <T> ValueKey<T> createNew(String name, Class<T> clazz) {
      return INSTANCE.createNewWithAgruments(BEAN_NAME, name, clazz);
   }

   /**
    * Creates a new Instance of a new {@link ValueKey} with the given arguments
    *
    * @return a new Instance of a new {@link ValueKey} with the given arguments
    */
   public static <T> ValueKey<T> createNew(String name, String resourceName, Class<T> clazz) {
      return INSTANCE.createNewWithAgruments(BEAN_NAME, name, resourceName, clazz);
   }
}
