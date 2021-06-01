package com.adcubum.timerecording.test;

import static com.adcubum.timerecording.settings.common.Const.ZEITERFASSUNG_PROPERTIES;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import com.adcubum.timerecording.settings.Settings;

public class BaseTestWithSettings {

   @BeforeEach
   public void setUp() throws IOException {
      File file = new File(ZEITERFASSUNG_PROPERTIES);
      file.createNewFile();
      if (!file.exists()) {
         throw new IllegalStateException();
      }
   }

   @AfterEach
   public void cleanUp() throws IOException {
      File file = new File(ZEITERFASSUNG_PROPERTIES);
      Files.delete(file.toPath());
   }

   protected static void saveProperty2Settings(String propertyName, String propertyValue) {
      try {
         Settings.INSTANCE.saveValueToProperties(propertyName, propertyValue, ZEITERFASSUNG_PROPERTIES);
      } catch (IllegalStateException e) {
         fail(e);
      }
   }
}
