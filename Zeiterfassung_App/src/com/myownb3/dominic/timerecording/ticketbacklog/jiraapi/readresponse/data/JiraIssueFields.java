package com.myownb3.dominic.timerecording.ticketbacklog.jiraapi.readresponse.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class JiraIssueField {
   private String summary;
   private JiraIssueType issuetype;
   private JiraIssueAssignee assignee;
   private JiraIssueProjektInfos customfield_10060;

   public JiraIssueProjektInfos getCustomfield_10060() {
      return customfield_10060;
   }

   public void setCustomfield_10060(JiraIssueProjektInfos customfield_10060) {
      this.customfield_10060 = customfield_10060;
   }

   public JiraIssueType getIssuetype() {
      return issuetype;
   }

   public void setIssuetype(JiraIssueType issueType) {
      this.issuetype = issueType;
   }

   String getSummary() {
      return summary;
   }

   void setSummary(String summary) {
      this.summary = summary;
   }

   public boolean isSubTask() {
      return issuetype != null && Boolean.valueOf(issuetype.subtask);
   }

   public JiraIssueAssignee getAssignee() {
      return assignee;
   }

   public void setAssignee(JiraIssueAssignee assignee) {
      this.assignee = assignee;
   }

   @JsonIgnoreProperties(ignoreUnknown = true)
   private static class JiraIssueType {

      String subtask;

      @SuppressWarnings("unused")
      private String getSubtask() {
         return subtask;
      }

      @SuppressWarnings("unused")
      private void setSubtask(String subtask) {
         this.subtask = subtask;
      }
   }
}
