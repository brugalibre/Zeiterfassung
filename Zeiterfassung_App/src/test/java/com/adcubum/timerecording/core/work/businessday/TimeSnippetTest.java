package com.adcubum.timerecording.core.work.businessday;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.Date;

import org.junit.jupiter.api.Test;

import com.adcubum.timerecording.work.date.Time;

class TimeSnippetTest {

   @Test
   void testGetDurationRepEmptyTimeSnippted() {
      // Given
      TimeSnippet timeSnippet = new TimeSnippet(new Time());
      String expectedDurationRep = "0";

      // When
      String actualDurationRep = timeSnippet.getDurationRep();

      // Then
      assertThat(actualDurationRep, is(expectedDurationRep));
   }

   @Test
   void testGetDurationRepTimeSnipptetWithoutEnd() {
      // Given
      TimeSnippet timeSnippet = new TimeSnippet(new Date());
      timeSnippet.setBeginTimeStamp(new Time());
      String expectedDurationRep = "0";

      // When
      String actualDurationRep = timeSnippet.getDurationRep();

      // Then
      assertThat(actualDurationRep, is(expectedDurationRep));
   }

}
