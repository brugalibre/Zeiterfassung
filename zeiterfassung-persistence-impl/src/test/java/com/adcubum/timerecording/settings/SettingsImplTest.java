package com.adcubum.timerecording.settings;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import com.adcubum.timerecording.settings.key.ValueKey;
import com.adcubum.timerecording.settings.key.ValueKeyFactory;

class SettingsImplTest {

   @Test
   public void testUnknownPropertieFile() {

      // Given
      String propertyName = "key";
      String propertyValue = "value";
      ValueKey<String> key = ValueKeyFactory.createNew(propertyName, String.class);

      // When
      Executable exec = () -> new SettingsImpl("dontExist").saveValueToProperties(key, propertyValue);
      // Then
      assertThrows(IllegalStateException.class, exec);
   }
}
