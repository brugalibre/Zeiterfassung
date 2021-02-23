package com.myownb3.dominic.timerecording.ticketbacklog.jiraapi.readresponse.data;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.myownb3.dominic.timerecording.ticketbacklog.jiraapi.constant.JiraApiConstants;
import com.myownb3.dominic.timerecording.ticketbacklog.jiraapi.readresponse.response.error.JiraErrorResponse;

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
