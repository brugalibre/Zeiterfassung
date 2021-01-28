package com.myownb3.dominic.timerecording.ticketbacklog.jiraapi.readresponse.response.error;

import com.myownb3.dominic.timerecording.ticketbacklog.jiraapi.readresponse.response.JiraResponse;

public class JiraErrorResponse implements JiraResponse {

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
