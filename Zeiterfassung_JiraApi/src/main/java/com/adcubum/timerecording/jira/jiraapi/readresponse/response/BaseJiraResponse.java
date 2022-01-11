package com.adcubum.timerecording.jira.jiraapi.readresponse.response;

public class BaseJiraResponse implements JiraResponse {

   private boolean successful;

   @Override
   public void setIsSuccessful(boolean successful) {
      this.successful = successful;
   }

   @Override
   public boolean isSuccessful() {
      return successful;
   }
}
