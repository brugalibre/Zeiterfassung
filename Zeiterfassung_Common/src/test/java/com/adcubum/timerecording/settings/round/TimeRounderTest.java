package com.adcubum.timerecording.settings.round;

import static com.adcubum.timerecording.settings.common.Const.ZEITERFASSUNG_PROPERTIES;
import static com.adcubum.timerecording.settings.round.RoundMode.PROPERTY_KEY;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;

import com.adcubum.timerecording.settings.Settings;
import com.adcubum.timerecording.test.BaseTestWithSettings;

class TimeRounderTest extends BaseTestWithSettings{

   @Test
   void testSetRoundMode() {

      // Given
      Settings settings = spy(Settings.INSTANCE);
      settings.saveValueToProperties(PROPERTY_KEY, String.valueOf(RoundMode.FIFTEEN_MIN.getAmount()), ZEITERFASSUNG_PROPERTIES);
      TimeRounder timeRounder = new TimeRounder(settings);

      // When
      timeRounder.setRoundMode(RoundMode.FIFTEEN_MIN);
      timeRounder.setRoundMode(RoundMode.ONE_MIN);

      // Then
      verify(settings).saveValueToProperties(PROPERTY_KEY, String.valueOf(RoundMode.FIFTEEN_MIN.getAmount()), ZEITERFASSUNG_PROPERTIES);
      verify(settings).saveValueToProperties(PROPERTY_KEY, String.valueOf(RoundMode.ONE_MIN.getAmount()), ZEITERFASSUNG_PROPERTIES);
   }

}
