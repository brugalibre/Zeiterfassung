package com.adcubum.timerecording.jira.jiraapi.readresponse.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;

/**
 * Contains the json return element with all the jira issues
 * Note that this {@link JiraIssueResponse} filters all done {@link JiraIssue}s by default
 * 
 * @author domin
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class JiraIssuesResponse extends AbstractJiraResponse {

   private List<JiraIssue> issues;
   private int total;

   /**
    * Default constructor needed by jackson
    */
   public JiraIssuesResponse() {
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

   public int getTotal() {
      return total;
   }

   public void setTotal(int total) {
      this.total = total;
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
      if (isNull(otherParseIssuesFromJira)) {
         return this;
      }
      List<JiraIssue> allIssues = new ArrayList<>(issues);
      allIssues.addAll(otherParseIssuesFromJira.getIssues());
      setIssues(allIssues);
      return this;
   }
}
