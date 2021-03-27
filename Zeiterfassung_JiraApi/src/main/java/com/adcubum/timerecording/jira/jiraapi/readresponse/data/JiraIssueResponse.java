package com.adcubum.timerecording.jira.jiraapi.readresponse.data;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

import com.adcubum.timerecording.jira.jiraapi.readresponse.response.error.JiraErrorResponse;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class JiraIssueResponse extends JiraErrorResponse {

   private String id;
   private String key;
   private JiraIssueFields fields;

   /**
    * Default constructor needed by jackson
    */
   public JiraIssueResponse() {
      this(null, null);
   }

   /**
    * Constructor needed when something went south
    */
   public JiraIssueResponse(Exception e, String url) {
      super(e, url);
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
      return isNull(getException()) && nonNull(fields) && nonNull(key);
   }
}
