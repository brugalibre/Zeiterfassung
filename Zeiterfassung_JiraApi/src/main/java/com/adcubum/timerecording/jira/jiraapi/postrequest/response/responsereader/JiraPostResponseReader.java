package com.adcubum.timerecording.jira.jiraapi.postrequest.response.responsereader;

import com.adcubum.timerecording.jira.jiraapi.postrequest.post.data.JiraPostResponse;
import com.adcubum.timerecording.jira.jiraapi.postrequest.post.data.JiraPostResponseImpl;
import com.brugalibre.common.http.model.response.ResponseWrapper;
import com.brugalibre.common.http.service.response.AbstractHttpResponseReader;

public class JiraPostResponseReader extends AbstractHttpResponseReader<JiraPostResponse> {

   @Override
   protected Class<JiraPostResponse> getResponseResultClass() {
      return (Class) JiraPostResponseImpl.class;
   }

   @Override
   public ResponseWrapper<JiraPostResponse> createErrorResponse(Exception e, String url) {
      return new ResponseWrapper<>(new JiraPostResponseImpl(), 500, e, url);
   }
}
