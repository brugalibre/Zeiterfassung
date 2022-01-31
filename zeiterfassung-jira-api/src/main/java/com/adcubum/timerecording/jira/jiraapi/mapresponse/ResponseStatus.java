package com.adcubum.timerecording.jira.jiraapi.mapresponse;

public enum ResponseStatus {

   /** The response is 100% successfull*/
   SUCCESS,

   /** The response is only partial successfull, this means: We got some results, but not 100% since an exception occurred*/
   PARTIAL_SUCCESS,

   /** The response failed, no results or anything similar received*/
   FAILURE
}
