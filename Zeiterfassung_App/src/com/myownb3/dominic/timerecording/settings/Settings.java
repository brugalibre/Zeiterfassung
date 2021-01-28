package com.myownb3.dominic.timerecording.settings;

import static com.myownb3.dominic.timerecording.settings.common.Const.TURBO_BUCHER_PROPERTIES;

import java.io.FileInputStream;
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
    *        the key to a propertie
    * @return the value asociated with the given key
    */
   public String getSettingsValue(String settingValueKey) {
      try (InputStream resourceStream = new FileInputStream(TURBO_BUCHER_PROPERTIES)) {
         return evalSettingValueForKey(resourceStream, settingValueKey);
      } catch (IOException e) {
         throw new IllegalStateException(e);
      }
   }

   private String evalSettingValueForKey(InputStream resourceStream, String settingValueKey) throws IOException {
      Properties prop = new Properties();
      prop.load(resourceStream);
      return (String) prop.get(settingValueKey);
   }
}
