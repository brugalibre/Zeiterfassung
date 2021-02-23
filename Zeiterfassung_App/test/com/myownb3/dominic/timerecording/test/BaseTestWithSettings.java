package com.myownb3.dominic.timerecording.test;

import static com.myownb3.dominic.timerecording.settings.common.Const.TURBO_BUCHER_PROPERTIES;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.Properties;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

public class BaseTestWithSettings {

   @BeforeEach
   public void setUp() throws IOException {
      File file = new File(TURBO_BUCHER_PROPERTIES);
      file.createNewFile();
      if (!file.exists()) {
         throw new IllegalStateException();
      }
   }

   @AfterEach
   public void cleanUp() throws IOException {
      File file = new File(TURBO_BUCHER_PROPERTIES);
      Files.delete(file.toPath());
   }

   protected static void saveProperty2Settings(String propertyName, String propertyValue) {
      Properties prop = new Properties();
      try (InputStream resourceStream = new FileInputStream(TURBO_BUCHER_PROPERTIES)) {
         prop.load(resourceStream);
         prop.put(propertyName, propertyValue);
         try (FileOutputStream out = new FileOutputStream(TURBO_BUCHER_PROPERTIES)) {
            prop.store(out, null);
         }
      } catch (Exception e) {
         fail(e);
      }
   }
}
