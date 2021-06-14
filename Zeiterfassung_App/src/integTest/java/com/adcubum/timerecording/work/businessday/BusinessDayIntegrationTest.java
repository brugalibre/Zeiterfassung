
package com.adcubum.timerecording.work.businessday;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Test;

import com.adcubum.librarys.text.res.TextLabel;
import com.adcubum.timerecording.core.work.businessday.BusinessDayImpl;
import com.adcubum.timerecording.core.work.businessday.BusinessDayIncrement;
import com.adcubum.timerecording.core.work.businessday.TimeSnippet;
import com.adcubum.timerecording.test.BaseTestWithSettings;

class BusinessDayIntegrationTest extends BaseTestWithSettings {
   @Test
   void testGetCapturingSinceMsgWithFinishedIncrement() throws InterruptedException {
      // Given
      BusinessDayImpl businessDay = new BusinessDayImpl();
      businessDay.startNewIncremental();
      TimeUnit.MINUTES.sleep(1);// hihihi sonar doesn't get it, that this will also call Thread.sleep
      businessDay.stopCurrentIncremental();
      BusinessDayIncrement currentBussinessDayIncremental = businessDay.getCurrentBussinessDayIncremental();
      TimeSnippet currentTimeSnippet = currentBussinessDayIncremental.getCurrentTimeSnippet();
      String amountOfHours = " (0.02h)";

      // When
      String actualCapturingInactiveSinceMsg = businessDay.getCapturingActiveSinceMsg();

      // Then
      assertThat(actualCapturingInactiveSinceMsg,
            is(TextLabel.CAPTURING_ACTIVE_SINCE + " " + currentTimeSnippet.getBeginTimeStamp() + amountOfHours));
   }
}
