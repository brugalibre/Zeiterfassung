package com.adcubum.timerecording.settings;

import static com.adcubum.timerecording.settings.common.Const.ZEITERFASSUNG_PROPERTIES;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Settings {

   public static final Settings INSTANCE = new Settings();

   private Settings() {
      // private
   }

   /**
    * Returns the value associated with the given key
    * 
    * @param settingValueKey
    *        the key to a properties
    * @return the value associated with the given key
    */
   public String getSettingsValue(String settingValueKey) {
      return getSettingsValue(settingValueKey, ZEITERFASSUNG_PROPERTIES);
   }

   public String getSettingsValue(String settingValueKey, String propFile) {
      try (InputStream resourceStream = new FileInputStream(propFile)) {
         return evalSettingValueForKey(resourceStream, settingValueKey);
      } catch (IOException e) {
         throw new IllegalStateException(e);
      }
   }

   /**
    * Saves a new value
    * 
    * @param key
    *        the key
    * @param value
    *        the value
    * @param propertiesFile
    *        the name of the properties file
    */
   public void saveValueToProperties(String key, String value, String propertiesFile) {
      Properties prop = new Properties();
      try (InputStream resourceStream = new FileInputStream(propertiesFile)) {
         prop.load(resourceStream);
         prop.put(key, value);
         try (FileOutputStream out = new FileOutputStream(propertiesFile)) {
            prop.store(out, null);
         }
      } catch (Exception e) {
         throw new IllegalStateException(e);
      }
   }

   private String evalSettingValueForKey(InputStream resourceStream, String settingValueKey) throws IOException {
      Properties prop = new Properties();
      prop.load(resourceStream);
      return (String) prop.get(settingValueKey);
   }
}
