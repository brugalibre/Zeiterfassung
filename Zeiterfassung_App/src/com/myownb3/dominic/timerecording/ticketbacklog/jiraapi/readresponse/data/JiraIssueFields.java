package com.myownb3.dominic.timerecording.ticketbacklog.jiraapi.readresponse.data;

import static java.util.Objects.nonNull;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * The class {@link JiraIssueFields} contains all information and fields which can read from a jira-Ticket using the jira-api
 * 
 * @author Dominic
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class JiraIssueFields {
   private String summary;
   private JiraIssueType issuetype;
   private JiraIssueAssignee assignee;
   private JiraIssueGenericValueObject customfield_10060;
   private JiraIssueThemaInfos customfield_11310;
   private JiraIssueGenericValueObject customfield_10510;
   private JiraIssueGenericValueObject customfield_10200;
   private JiraFixVersionsType[] fixVersions;
   private String customfield_10000;
   private String customfield_11516;
   private String customfield_10550;

   public JiraIssueFields() {
      customfield_10060 = new JiraIssueGenericValueObject();
      customfield_10200 = new JiraIssueGenericValueObject();
      customfield_10510 = new JiraIssueGenericValueObject();
      issuetype = new JiraIssueType();
      customfield_11310 = new JiraIssueThemaInfos();
      fixVersions = new JiraFixVersionsType[] {};
   }

   public JiraIssueThemaInfos getCustomfield_11310() {
      return customfield_11310;
   }

   public void setCustomfield_11310(JiraIssueThemaInfos customfield_11310) {
      if (nonNull(customfield_11310)) {
         // the ObjectMapper may call this with null
         this.customfield_11310 = customfield_11310;
      }
   }

   public JiraIssueGenericValueObject getCustomfield_10510() {
      return customfield_10510;
   }

   public void setCustomfield_10510(JiraIssueGenericValueObject customfield_10510) {
      if (nonNull(customfield_10510)) {
         // the ObjectMapper may call this with null
         this.customfield_10510 = customfield_10510;
      }
   }

   public String getCustomfield_10000() {
      return customfield_10000;
   }

   public void setCustomfield_10000(String customfield_10000) {
      this.customfield_10000 = customfield_10000;
   }

   public String getCustomfield_11516() {
      return customfield_11516;
   }

   public void setCustomfield_11516(String customfield_11516) {
      this.customfield_11516 = customfield_11516;
   }

   public JiraIssueGenericValueObject getCustomfield_10060() {
      return customfield_10060;
   }

   public void setCustomfield_10060(JiraIssueGenericValueObject customfield_10060) {
      if (nonNull(customfield_10060)) {
         // the ObjectMapper may call this with null
         this.customfield_10060 = customfield_10060;
      }
   }

   public JiraIssueType getIssuetype() {
      return issuetype;
   }

   public void setIssuetype(JiraIssueType issueType) {
      if (nonNull(issueType)) {
         // the ObjectMapper may call this with null
         this.issuetype = issueType;
      }
   }

   String getSummary() {
      return summary;
   }

   void setSummary(String summary) {
      this.summary = summary;
   }

   public boolean isSubTask() {
      return Boolean.valueOf(issuetype.subtask);
   }

   public JiraIssueAssignee getAssignee() {
      return assignee;
   }

   public void setAssignee(JiraIssueAssignee assignee) {
      this.assignee = assignee;
   }

   public String getCustomfield_10550() {
      return customfield_10550;
   }

   public void setCustomfield_10550(String customfield_10550) {
      this.customfield_10550 = customfield_10550;
   }

   public JiraIssueGenericValueObject getCustomfield_10200() {
      return customfield_10200;
   }

   public void setCustomfield_10200(JiraIssueGenericValueObject customfield_10200) {
      if (nonNull(customfield_10200)) {
         // the ObjectMapper may call this with null
         this.customfield_10200 = customfield_10200;
      }
   }

   public JiraFixVersionsType[] getFixVersions() {
      return fixVersions;
   }

   public void setFixVersions(JiraFixVersionsType[] fixVersions) {
      if (nonNull(fixVersions)) {
         // the ObjectMapper may call this with null
         this.fixVersions = fixVersions;
      }
   }

   @JsonIgnoreProperties(ignoreUnknown = true)
   static class JiraIssueType {

      String subtask;
      private String name;

      @SuppressWarnings("unused")
      private String getSubtask() {
         return subtask;
      }

      @SuppressWarnings("unused")
      private void setSubtask(String subtask) {
         this.subtask = subtask;
      }

      public String getName() {
         return name;
      }

      public void setName(String name) {
         this.name = name;
      }
   }
   @JsonIgnoreProperties(ignoreUnknown = true)
   public static class JiraFixVersionsType {

      private String name;

      public String getName() {
         return name;
      }

      public void setName(String name) {
         this.name = name;
      }
   }
}
