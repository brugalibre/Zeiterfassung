package com.adcubum.timerecording.jira.jiraapi.postrequest.post.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class JiraPostResponseImpl implements JiraPostResponse {

   private boolean successful;

   @Override
   public boolean isSuccessful() {
      return successful;
   }

   @Override
   public void setIsSuccessful(boolean successful) {
      this.successful = successful;
   }
}
