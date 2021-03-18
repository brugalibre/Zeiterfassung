package com.myownb3.dominic.timerecording.ticketbacklog.jiraapi.readresponse.data;

import static java.util.Objects.requireNonNull;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.myownb3.dominic.timerecording.ticketbacklog.jiraapi.readresponse.data.parser.JiraSyrReleaseParser;
import com.myownb3.dominic.timerecording.ticketbacklog.jiraapi.readresponse.data.parser.JiraThemaParser;
import com.myownb3.dominic.timerecording.ticketbacklog.jiraapi.readresponse.data.parser.ProjectnummerParser;

@JsonIgnoreProperties(ignoreUnknown = true)
public class JiraIssue {
   private String key;
   private String id;
   private JiraIssueFields fields;

   public JiraIssue() {
      this.fields = new JiraIssueFields();
   }

   /**
    * Creates a {@link JiraIssue} of the given {@link JiraIssueResponse}
    * 
    * @param jiraIssueResponse
    *        the given {@link JiraIssueResponse}
    * @return a {@link JiraIssue}
    */
   public static JiraIssue of(JiraIssueResponse jiraIssueResponse) {
      JiraIssue jiraIssue = new JiraIssue();
      jiraIssue.setFields(jiraIssueResponse.getFields());
      jiraIssue.setKey(jiraIssueResponse.getKey());
      jiraIssue.setId(jiraIssueResponse.getId());
      return jiraIssue;
   }

   void setFields(JiraIssueFields fields) {
      this.fields = requireNonNull(fields);
   }

   public void setKey(String key) {
      this.key = key;
   }

   public String getKey() {
      return key;
   }

   public void setId(String id) {
      this.id = id;
   }

   public String getSprintId() {
      return fields.getSprint().getId();
   }

   public String getSprintName() {
      return fields.getSprint().getName();
   }

   public String getId() {
      return id;
   }

   public String getExternalNr() {
      return fields.getCustomfield_10000();
   }

   public String getEpicNr() {
      return fields.getCustomfield_11516();
   }

   public String getIssueType() {
      return fields.getIssuetype().getName();
   }

   public String getThema() {
      JiraIssueThemaInfos jiraIssueThemaInfos = fields.getCustomfield_11310();
      return new JiraThemaParser().getThema(jiraIssueThemaInfos.getValue());
   }

   public String getProjectCostUnit() {
      JiraIssueThemaInfos jiraIssueThemaInfos = fields.getCustomfield_11310();
      return new JiraThemaParser().getProjectCostUnit(jiraIssueThemaInfos.getValue());
   }

   public String getProjectDesc() {
      JiraIssueGenericValueObject projectInfos = fields.getCustomfield_10060();
      return new ProjectnummerParser().getProjectDesc(projectInfos.getValue());
   }

   public String getProjectNr() {
      JiraIssueGenericValueObject projectInfos = fields.getCustomfield_10060();
      return new ProjectnummerParser().getProjectNr(projectInfos.getValue());
   }

   public String getImplementationPackage() {
      return "";//TODO
   }

   public String getSubthema() {
      JiraIssueGenericValueObject child = fields.getCustomfield_11310().getChild();
      return child.getValue();
   }

   public String getBusinessTeamPlaning() {
      return fields.getCustomfield_10510().getValue();
   }

   public String getPlaningId() {
      return fields.getCustomfield_10550();
   }

   public String getSyriusExtension() {
      return fields.getCustomfield_10200().getValue();
   }

   public String getSyriusRelease() {
      return new JiraSyrReleaseParser().getSyriusRelease(fields.getFixVersions());
   }

   public boolean isNotSubtask() {
      return !fields.isSubTask();
   }

   public boolean isNotDone() {
      return fields.isNotDone();
   }

   public String getTitle() {
      return fields.getSummary();
   }

   public String getAssignee() {
      JiraIssueAssignee assignee = fields.getAssignee();
      return assignee != null ? assignee.getKey() : null;
   }

   @Override
   public String toString() {
      return key + ", titel " + getTitle() + " is subtask: " + !isNotSubtask();
   }
}
