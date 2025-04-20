package com.adcubum.timerecording.jira.jiraapi.readresponse.data;

import com.adcubum.timerecording.jira.jiraapi.readresponse.response.JiraResponse;

public abstract class AbstractJiraResponse implements JiraResponse {
   private boolean isSuccessful;

   @Override
   public boolean isSuccessful() {
      return isSuccessful;
   }

   @Override
   public void setIsSuccessful(boolean isSuccessful) {
      this.isSuccessful = isSuccessful;
   }
}
