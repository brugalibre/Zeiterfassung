package com.adcubum.timerecording.jira.jiraapi.readresponse.data;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.jupiter.api.Test;

import com.adcubum.timerecording.jira.jiraapi.readresponse.data.jiraissuefields.GenericNameIdObject;

class JiraIssueFieldsTest {

   @Test
   void testIsNotDone_SetNullResolution() {

      // Given
      JiraIssueFields jiraIssueFields = new JiraIssueFields();

      // When
      jiraIssueFields.setResolution(null);

      // Then
      assertThat(jiraIssueFields.getResolution(), is(notNullValue()));
   }

   @Test
   void testIsNotDone_SetNonNullResolution() {

      // Given
      JiraIssueFields jiraIssueFields = new JiraIssueFields();
      GenericNameIdObject newResolutionObject = new GenericNameIdObject();

      // When
      jiraIssueFields.setResolution(newResolutionObject);

      // Then
      assertThat(jiraIssueFields.getResolution(), is(newResolutionObject));
   }
}
