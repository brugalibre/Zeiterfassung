package com.adcubum.timerecording.jira.jiraapi.postrequest.post.worklog;

import com.adcubum.timerecording.jira.jiraapi.postrequest.post.data.JiraPostResponse;
import com.adcubum.timerecording.jira.jiraapi.postrequest.post.worklog.data.Worklog;

/**
 * The {@link JiraApiWorklogCreator} servers as api for the jira 'Log Work' plugin
 *
 * @author dstalder
 */
public interface JiraApiWorklogCreator {

   /**
    * Creates a new {@link Worklog} in the jira-api
    *
    * @param worklog the worklog to create
    * @return a {@link JiraPostResponse}
    */
   JiraPostResponse createWorklog(Worklog worklog);

}
