package com.myownb3.dominic.timerecording.ticketbacklog.jiraapi.readresponse.response.responsereader;

import com.myownb3.dominic.timerecording.ticketbacklog.jiraapi.readresponse.data.JiraIssueResponse;

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
