package com.adcubum.timerecording.jira.jiraapi.readresponse.response.error;

import com.adcubum.timerecording.jira.jiraapi.readresponse.response.BaseJiraResponse;

public class JiraErrorResponse extends BaseJiraResponse {

   private String url;
   private Exception exception;

   public JiraErrorResponse(Exception exception, String url) {
      this.exception = exception;
      this.url = url;
   }

   public String getUrl() {
      return url;
   }

   public Exception getException() {
      return exception;
   }
}
