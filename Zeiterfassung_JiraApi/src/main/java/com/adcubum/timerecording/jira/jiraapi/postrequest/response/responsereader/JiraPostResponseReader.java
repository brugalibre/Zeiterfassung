package com.adcubum.timerecording.jira.jiraapi.postrequest.response.responsereader;

import com.adcubum.timerecording.jira.jiraapi.postrequest.post.data.JiraPostResponse;
import com.adcubum.timerecording.jira.jiraapi.postrequest.post.data.JiraPostResponseImpl;
import com.adcubum.timerecording.jira.jiraapi.readresponse.response.responsereader.AbstractJiraResponseReader;

public class JiraPostResponseReader extends AbstractJiraResponseReader<JiraPostResponse> {

   @Override
   protected Class<JiraPostResponse> getResponseResultClass() {
      return (Class) JiraPostResponseImpl.class;
   }

   @Override
   public JiraPostResponse createErrorResponse(Exception e, String url) {
      return new JiraPostResponseImpl(e, url);
   }
}
