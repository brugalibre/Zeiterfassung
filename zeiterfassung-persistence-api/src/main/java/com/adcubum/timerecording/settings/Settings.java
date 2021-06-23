package com.adcubum.timerecording.settings;

import com.adcubum.timerecording.settings.key.ValueKey;

public interface Settings {

   /** The singleton instance of a {@link Settings} */
   public static final Settings INSTANCE = SettingsFactory.createNew();

   /**
    * Returns the value associated with the given key
    * 
    * @param settingValueKey
    *        the key to a properties
    * @return the value associated with the given key
    */
   <T> T getSettingsValue(ValueKey<T> settingValueKey);

   /**
    * Saves a new value
    * 
    * @param key
    *        the key
    * @param value
    *        the value
    */
   <T> void saveValueToProperties(ValueKey<T> settingValueKey, String value);

   /**
    * Initializes this {@link Settings}
    */
   void init();

}
