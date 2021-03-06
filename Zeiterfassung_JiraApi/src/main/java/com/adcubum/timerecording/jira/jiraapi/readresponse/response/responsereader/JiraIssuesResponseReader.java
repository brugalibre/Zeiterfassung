package com.adcubum.timerecording.jira.jiraapi.readresponse.response.responsereader;

import com.adcubum.timerecording.jira.jiraapi.readresponse.data.JiraIssuesResponse;

public class JiraIssuesResponseReader extends AbstractJiraResponseReader<JiraIssuesResponse> {

   @Override
   protected Class<JiraIssuesResponse> getResponseResultClass() {
      return JiraIssuesResponse.class;
   }

   @Override
   public JiraIssuesResponse createErrorResponse(Exception e, String url) {
      return new JiraIssuesResponse(e, url);
   }
}
