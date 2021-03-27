package com.adcubum.timerecording.jira.jiraapi.readresponse.response.responsereader;

import com.adcubum.timerecording.jira.jiraapi.readresponse.data.JiraIssueResponse;

public class JiraIssueResponseReader extends AbstractJiraResponseReader<JiraIssueResponse> {

   @Override
   protected Class<JiraIssueResponse> getResponseResultClass() {
      return JiraIssueResponse.class;
   }

   @Override
   public JiraIssueResponse createErrorResponse(Exception e, String url) {
      return new JiraIssueResponse(e, url);
   }
}
