package com.adcubum.timerecording.settings.round;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;

import com.adcubum.timerecording.settings.round.observable.RoundModeChangedListener;

class TimeRounderTest {

   @Test
   void testSetRoundMode() {

      // Given
      RoundModeChangedListener roundModeChangedListener = mock(RoundModeChangedListener.class);
      TimeRounder timeRounder = new TimeRounderImpl();
      timeRounder.init(key -> String.valueOf(RoundMode.ONE_MIN.getAmount()));
      timeRounder.addRoundModeChangedListener(roundModeChangedListener);

      // When
      timeRounder.setRoundMode(RoundMode.FIFTEEN_MIN);
      timeRounder.setRoundMode(RoundMode.ONE_MIN);

      // Then
      verify(roundModeChangedListener).onRoundModeChanged(eq(RoundMode.ONE_MIN), eq(RoundMode.FIFTEEN_MIN));
      verify(roundModeChangedListener).onRoundModeChanged(eq(RoundMode.FIFTEEN_MIN), eq(RoundMode.ONE_MIN));
   }

}
