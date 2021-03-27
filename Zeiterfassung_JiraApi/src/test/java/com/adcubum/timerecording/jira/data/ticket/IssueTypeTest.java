package com.adcubum.timerecording.jira.data.ticket;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.jupiter.api.Test;

class IssueTypeTest {

   @Test
   void testOfUnknown() {
      // Given
      String issueTypeName = "schnappi";

      // When
      IssueType actualIssueType = IssueType.of(issueTypeName);

      // Then
      assertThat(actualIssueType, is(IssueType.UNKNOWN));
   }
}
