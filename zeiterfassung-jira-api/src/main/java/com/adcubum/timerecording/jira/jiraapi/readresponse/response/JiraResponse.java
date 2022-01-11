package com.adcubum.timerecording.jira.jiraapi.readresponse.response;

/**
 * Common interface for all responses from jira
 *
 * @author Dominic
 */
public interface JiraResponse {

   /**
    * @return <code>true</code> if the request was posted successfully or <code>false</code> if not
    */
   boolean isSuccessful();

   /**
    * sets the http-status of the response
    *
    * @param successful @return <code>true</code> if the request was posted successfully or <code>false</code> if not
    */
   void setIsSuccessful(boolean successful);
}
