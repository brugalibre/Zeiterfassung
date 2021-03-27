package com.adcubum.timerecording.jira.jiraapi.readresponse.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class JiraIssueAssignee {
   private String key;

   public String getKey() {
      return key;
   }

   public void setKey(String key) {
      this.key = key;
   }
}
