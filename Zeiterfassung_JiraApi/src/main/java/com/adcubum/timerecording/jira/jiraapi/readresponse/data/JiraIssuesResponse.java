package com.adcubum.timerecording.jira.jiraapi.readresponse.data;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.adcubum.timerecording.jira.jiraapi.configuration.JiraApiConstants;
import com.adcubum.timerecording.jira.jiraapi.readresponse.response.error.JiraErrorResponse;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Contains the json return element with all the jira issues
 * Note that this {@link JiraIssueResponse} filters all done {@link JiraIssue}s by default
 * 
 * @author domin
 *
 */
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

   /*
    * Removes all {@link JiraIssue} which are a subtask
    */
   private List<JiraIssue> filterDoneTasks(List<JiraIssue> issues) {
      return issues.stream()
            .filter(JiraIssue::isNotDone)
            .collect(Collectors.toList());
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
      this.issues = filterDoneTasks(issues);
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
    * @return this {@link JiraIssueResponse}
    * 
    * @param otherParseIssuesFromJira
    *        the other {@link JiraIssuesResponse}
    */
   public JiraIssuesResponse applyFromOther(JiraIssuesResponse otherParseIssuesFromJira) {
      List<JiraIssue> allIssues = new ArrayList<>(issues);
      allIssues.addAll(otherParseIssuesFromJira.getIssues());
      setIssues(allIssues);
      return this;
   }
}
