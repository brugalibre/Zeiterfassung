package com.adcubum.timerecording.core.work.businessday;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.jupiter.api.Test;

import com.adcubum.timerecording.work.date.DateTime;
import com.adcubum.timerecording.work.date.DateTimeFactory;

class TimeSnippetTest {

   @Test
   void testGetDurationRepEmptyTimeSnipptet() {
      // Given
      TimeSnippet timeSnippet = TimeSnippetFactory.createNew();
      String expectedDurationRep = "0";

      // When
      String actualDurationRep = timeSnippet.getDurationRep();

      // Then
      assertThat(actualDurationRep, is(expectedDurationRep));
   }

   @Test
   void testGetDate_EmptyTimeSnipptet() {
      // Given
      TimeSnippet timeSnippet = TimeSnippetFactory.createNew();

      // When
      DateTime actualTime = timeSnippet.getDateTime();

      // Then
      assertThat(actualTime, is(DateTimeFactory.createNew()));
   }

   @Test
   void testGetDurationRepTimeSnipptetWithoutEnd() {
      // Given
      TimeSnippet timeSnippet = TimeSnippetFactory.createNew()
            .setBeginTimeStamp(DateTimeFactory.createNew());
      String expectedDurationRep = "0";

      // When
      String actualDurationRep = timeSnippet.getDurationRep();

      // Then
      assertThat(actualDurationRep, is(expectedDurationRep));
   }

}
