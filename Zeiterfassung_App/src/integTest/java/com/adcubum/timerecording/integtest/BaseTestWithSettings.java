package com.adcubum.timerecording.integtest;

import static com.adcubum.timerecording.settings.common.Const.ZEITERFASSUNG_PROPERTIES;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

//"use test resources!"
@Deprecated
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
}
