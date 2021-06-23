package com.adcubum.timerecording.settings;

import static com.adcubum.timerecording.settings.common.Const.ZEITERFASSUNG_PROPERTIES;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.adcubum.timerecording.settings.common.Const;
import com.adcubum.timerecording.settings.key.ValueKey;

public class SettingsImpl implements Settings {

   private String propertiesName;

   /**
    * Default Constructor used by the {@link SettingsFactory}
    */
   @SuppressWarnings("unused")
   private SettingsImpl() {
      this(ZEITERFASSUNG_PROPERTIES);
   }

   /**
    * Constructor used only for testing purpose!
    */
   SettingsImpl(String propertiesName) {
      this.propertiesName = propertiesName;
   }

   @SuppressWarnings("unchecked")
   @Override
   public <T> T getSettingsValue(ValueKey<T> settingValueKey) {
      return (T) getSettingsValue(settingValueKey.getName(), propertiesName);
   }

   private static String getSettingsValue(String settingValueKey, String propFile) {
      try (InputStream resourceStream = new FileInputStream(propFile)) {
         return evalSettingValueForKey(resourceStream, settingValueKey);
      } catch (IOException e) {
         throw new IllegalStateException(e);
      }
   }

   @Override
   public <T> void saveValueToProperties(ValueKey<T> key, String value) {
      Properties prop = new Properties();
      try (InputStream resourceStream = new FileInputStream(propertiesName)) {
         prop.load(resourceStream);
         prop.put(key.getName(), value);
         try (FileOutputStream out = new FileOutputStream(propertiesName)) {
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

   @Override
   public void init() {
      createPropertieFileIfNotExists(ZEITERFASSUNG_PROPERTIES);
      createPropertieFileIfNotExists(Const.TURBO_BUCHER_PROPERTIES);
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
