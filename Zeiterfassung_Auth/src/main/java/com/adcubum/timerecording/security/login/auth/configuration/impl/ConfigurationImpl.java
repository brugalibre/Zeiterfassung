package com.adcubum.timerecording.security.login.auth.configuration.impl;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.adcubum.timerecording.security.login.auth.configuration.Configuration;
import com.adcubum.timerecording.security.login.auth.configuration.constant.AuthenticationConst;

public class ConfigurationImpl implements Configuration {

   private String propertiesName;

   public ConfigurationImpl() {
      this(AuthenticationConst.AUTH_CONFIGURATION_PROPERTIES);
   }

   /**
    * Constructor used only for testing purpose!
    */
   public ConfigurationImpl(String propertiesName) {
      this.propertiesName = propertiesName;
   }

   @Override
   public String getValue(String key) {
      return getSettingsValue(key, propertiesName);
   }

   private static String getSettingsValue(String settingValueKey, String propFile) {
      try (InputStream resourceStream = new FileInputStream(propFile)) {
         return evalSettingValueForKey(resourceStream, settingValueKey);
      } catch (IOException e) {
         throw new IllegalStateException(e);
      }
   }

   private static String evalSettingValueForKey(InputStream resourceStream, String settingValueKey) throws IOException {
      Properties prop = new Properties();
      prop.load(resourceStream);
      return (String) prop.get(settingValueKey);
   }
}
