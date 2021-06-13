package com.adcubum.timerecording.settings;

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
   String getSettingsValue(String settingValueKey);

   /**
    * Returns the value associated with the given key stored within a specific properties file
    * 
    * @param settingValueKey
    *        the key to a properties
    * @param propFile
    *        the name of the specific properties-file
    * @return the value associated with the given key
    */
   String getSettingsValue(String settingValueKey, String propFile);

   /**
    * Saves a new value in the given properties-file
    * 
    * @param key
    *        the key
    * @param value
    *        the value
    * @param propertiesFile
    *        the name of the properties file
    */
   void saveValueToProperties(String key, String value, String propertiesFile);

}
