package com.adcubum.timerecording.settings;

import com.adcubum.timerecording.settings.key.ValueKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Properties;

import static com.adcubum.timerecording.settings.common.Const.TICKET_SYSTEM_PROPERTIES;
import static com.adcubum.timerecording.settings.common.Const.ZEITERFASSUNG_PROPERTIES;
import static java.util.Objects.isNull;

public class SettingsImpl implements Settings {

   private static final Logger LOG = LoggerFactory.getLogger(SettingsImpl.class);

   /**
    * Constructor used only for testing purpose!
    */
   SettingsImpl() {
      // empty
   }

   @SuppressWarnings("unchecked")
   @Override
   public <T> T getSettingsValue(ValueKey<T> settingValueKey) {
      String settingsValue = getSettingsValue(settingValueKey.getName(), settingValueKey.getResourceName());
      return (T) map2TargetClass(settingsValue, settingValueKey);
   }

   private static <T> Object map2TargetClass(String settingsValue, ValueKey<T> settingValueKey) {
      Class<?> clazz = settingValueKey.getType();
      if (isNull(settingsValue)) {
         return settingValueKey.getDefault();
      } else if (clazz.isAssignableFrom(String.class)) {
         return settingsValue;
      } else if (clazz.isAssignableFrom(Integer.class)) {
         return Integer.parseInt(settingsValue);
      } else if (clazz.isAssignableFrom(Float.class)) {
         return Float.parseFloat(settingsValue);
      } else if (clazz.isAssignableFrom(Boolean.class)) {
         return Boolean.valueOf(settingsValue);
      } else if (clazz.isEnum()) {
         return Enum.valueOf((Class<? extends Enum>) clazz, settingsValue.toUpperCase());
      }
      throw new IllegalStateException("SettingKey-Type '" + clazz + "' not implemented!");
   }

   private static String getSettingsValue(String settingValueKey, String propFile) {
      try (InputStream resourceStream = getInputStream(propFile)) {
         return evalSettingValueForKey(resourceStream, settingValueKey);
      } catch (IOException e) {
         LOG.error("Unable to read value for key '{}' in property file '{}'", settingValueKey, propFile, e);
      }
      return null;
   }

   @Override
   public <T> void saveValueToProperties(ValueKey<T> key, String value) {
      Properties prop = new Properties();
      try (InputStream resourceStream = getInputStream(key.getResourceName())) {
         prop.load(resourceStream);
         prop.put(key.getName(), value);
         try (FileOutputStream out = new FileOutputStream(key.getResourceName())) {
            prop.store(out, null);
         }
      } catch (Exception e) {
         throw new IllegalStateException(e);
      }
   }

   private static String evalSettingValueForKey(InputStream resourceStream, String settingValueKey) throws IOException {
      Properties prop = new Properties();
      prop.load(resourceStream);
      return (String) prop.get(settingValueKey);
   }

   private static InputStream getInputStream(String propertiesName) throws FileNotFoundException {
      InputStream resourceStreamFromResource = SettingsImpl.class.getClassLoader().getResourceAsStream(propertiesName);
      if (isNull(resourceStreamFromResource)) {
         return new FileInputStream(propertiesName);
      }
      return resourceStreamFromResource;
   }

   @Override
   public void init() {
      createPropertieFileIfNotExists(ZEITERFASSUNG_PROPERTIES);
      createPropertieFileIfNotExists(TICKET_SYSTEM_PROPERTIES);
   }

   private void createPropertieFileIfNotExists(String propertiesFileName) {
      File file = new File(propertiesFileName);
      if (!file.exists()) {
         try {
            if (!file.createNewFile()) {
               throw new SettingsInitException("Unable to create the '" + propertiesFileName + "' file!");
            }
         } catch (IOException e) {
            throw new SettingsInitException(e);
         }
      }
   }

}
