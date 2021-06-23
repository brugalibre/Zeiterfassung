package com.adcubum.timerecording.settings;

import com.adcubum.timerecording.core.factory.AbstractFactory;

/**
 * The {@link SettingsFactory} is used to create and instantiate new {@link Settings} instances
 * 
 * @author DStalder
 *
 */
public class SettingsFactory extends AbstractFactory {
   private static final String BEAN_NAME = "settings";
   private static final SettingsFactory INSTANCE = new SettingsFactory();

   private SettingsFactory() {
      super("persistence-module-configuration.xml");
   }

   /**
    * Creates a new Instance of the Settings or returns an already created instance
    * 
    * @return a new Instance of the Settings or returns an already created instance
    */
   public static Settings createNew() {
      return INSTANCE.createNewWithAgruments(BEAN_NAME);
   }
}
