package com.adcubum.timerecording.settings;

import com.adcubum.timerecording.settings.common.Const;
import com.adcubum.timerecording.settings.key.ValueKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Properties;

import static com.adcubum.timerecording.settings.common.Const.ZEITERFASSUNG_PROPERTIES;
import static java.util.Objects.isNull;

public class SettingsImpl implements Settings {

   private static final Logger LOG = LoggerFactory.getLogger(SettingsImpl.class);
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
      try (InputStream resourceStream = getInputStream(propertiesName)) {
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

   private static InputStream getInputStream(String propertiesName) throws FileNotFoundException {
      InputStream resourceStreamFromResource = SettingsImpl.class.getClassLoader().getResourceAsStream(propertiesName);
      if (isNull(resourceStreamFromResource)){
         return new FileInputStream(propertiesName);
      }
      return resourceStreamFromResource;
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
