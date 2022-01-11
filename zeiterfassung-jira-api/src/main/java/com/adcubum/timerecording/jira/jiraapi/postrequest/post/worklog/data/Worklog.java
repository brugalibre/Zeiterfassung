package com.adcubum.timerecording.jira.jiraapi.postrequest.post.worklog.data;

/**
 * The {@link Worklog} defines a jira worklog object
 *
 * @author dstalder
 * @see for details of a worklog https://developer.atlassian.com/cloud/jira/platform/rest/v2/
 * api-group-issue-worklogs/#api-rest-api-2-issue-issueidorkey-worklog-post
 */
public interface Worklog {

   /**
    * @return the optional book comment
    */
   String getComment();

   /**
    * @return the amount of time worked in seconds
    */
   int getTimeSpentSeconds();

   /**
    * @return the date as String when the work on a issue has started
    */
   String getStarted();

   /**
    * @return the unique number of the jira issue for which a worklog is created
    */
   String getIssueNr();
}
