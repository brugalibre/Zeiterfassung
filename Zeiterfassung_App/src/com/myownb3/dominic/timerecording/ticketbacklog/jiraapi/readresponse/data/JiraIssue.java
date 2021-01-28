package com.myownb3.dominic.timerecording.ticketbacklog.jiraapi.readresponse.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class JiraIssue {
   private String key;

   private JiraIssueFields fields;

   JiraIssueFields getFields() {
      return fields;
   }

   void setFields(JiraIssueFields fields) {
      this.fields = fields;
   }

   void setKey(String key) {
      this.key = key;
   }

   public String getKey() {
      return key;
   }

   public boolean isNotSubtask() {
      return fields == null || !fields.isSubTask();
   }

   public String getTitle() {
      return fields != null ? fields.getSummary() : null;
   }

   public String getAssignee() {
      JiraIssueAssignee assignee = fields != null ? fields.getAssignee() : null;
      return assignee != null ? assignee.getKey() : null;
   }

   public String getProjektNrAndBez() {
      JiraIssueProjektInfos projektInfos = fields != null ? fields.getCustomfield_10060() : null;
      return projektInfos != null ? projektInfos.getValue() : null;
   }

   @Override
   public String toString() {
      return key + ", titel " + getTitle() + " is subtask: " + !isNotSubtask();
   }
}
