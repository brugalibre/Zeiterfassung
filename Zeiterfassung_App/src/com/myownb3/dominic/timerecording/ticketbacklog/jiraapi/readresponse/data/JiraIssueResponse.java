package com.myownb3.dominic.timerecording.ticketbacklog.jiraapi.readresponse.data;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.myownb3.dominic.timerecording.ticketbacklog.jiraapi.readresponse.response.error.JiraErrorResponse;

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
