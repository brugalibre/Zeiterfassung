package com.adcubum.timerecording.jira.jiraapi.readresponse.data;

import static java.util.Objects.nonNull;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class JiraIssueResponse extends AbstractJiraResponse {

   private String id;
   private String key;
   private JiraIssueFields fields;

   /**
    * Default constructor needed by jackson
    */
   public JiraIssueResponse() {

   }


   public String getId() {
      return id;
   }

   public void setId(String id) {
      this.id = id;
   }

   public String getKey() {
      return key;
   }

   public void setKey(String key) {
      this.key = key;
   }

   public JiraIssueFields getFields() {
      return fields;
   }

   public void setFields(JiraIssueFields fields) {
      this.fields = fields;
   }

   public boolean isSuccess() {
      return nonNull(fields) && nonNull(key);
   }
}
