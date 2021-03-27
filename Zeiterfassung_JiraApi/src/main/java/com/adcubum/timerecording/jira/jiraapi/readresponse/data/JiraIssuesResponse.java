package com.adcubum.timerecording.jira.jiraapi.readresponse.data;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.adcubum.timerecording.jira.jiraapi.constant.JiraApiConstants;
import com.adcubum.timerecording.jira.jiraapi.readresponse.response.error.JiraErrorResponse;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class JiraIssuesResponse extends JiraErrorResponse {

   private List<JiraIssue> issues;

   /**
    * Default constructor needed by jackson
    */
   public JiraIssuesResponse() {
      this(null, null);
   }

   /**
    * Constructor needed when something went south
    */
   public JiraIssuesResponse(Exception e, String url) {
      super(e, url);
      this.issues = new ArrayList<>();
   }

   /**
    * Removes all {@link JiraIssue} which are a subtask
    * 
    * @return the {@link JiraIssuesResponse}
    */
   public JiraIssuesResponse filterDoneTasks() {
      this.issues = issues.stream()
            .filter(JiraIssue::isNotDone)
            .collect(Collectors.toList());
      return this;
   }

   public List<JiraIssue> getIssues() {
      return issues;
   }

   public List<JiraIssue> getIssuesNotSubtask() {
      return issues.stream()
            .filter(JiraIssue::isNotSubtask)
            .collect(Collectors.toList());
   }

   public void setIssues(List<JiraIssue> issues) {
      this.issues = issues;
   }

   /**
    * @return <code>true</code> if this {@link JiraIssuesResponse} contains more or equal than JiraApiConstants.JIRA_MAX_RESULTS_RETURNED
    *         entries
    */
   public boolean hasMaxResults() {
      return issues.size() >= JiraApiConstants.JIRA_MAX_RESULTS_RETURNED;
   }

   /**
    * Applies the issues of this {@link JiraIssuesResponse} and the other given {@link JiraIssueResponse}
    * 
    * @param otherParseIssuesFromJira
    *        the other {@link JiraIssuesResponse}
    */
   public void applyFromOther(JiraIssuesResponse otherParseIssuesFromJira) {
      List<JiraIssue> allIssues = new ArrayList<>(issues);
      allIssues.addAll(otherParseIssuesFromJira.getIssues());
      setIssues(allIssues);
   }
}
