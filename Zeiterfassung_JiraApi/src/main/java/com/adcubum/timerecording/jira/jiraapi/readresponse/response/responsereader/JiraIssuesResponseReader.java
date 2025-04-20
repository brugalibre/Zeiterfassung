package com.adcubum.timerecording.jira.jiraapi.readresponse.response.responsereader;

import com.adcubum.timerecording.jira.jiraapi.readresponse.data.JiraIssuesResponse;
import com.brugalibre.common.http.model.response.ResponseWrapper;
import com.brugalibre.common.http.service.response.AbstractHttpResponseReader;

public class JiraIssuesResponseReader extends AbstractHttpResponseReader<JiraIssuesResponse> {

   @Override
   protected Class<JiraIssuesResponse> getResponseResultClass() {
      return JiraIssuesResponse.class;
   }

   @Override
   public ResponseWrapper<JiraIssuesResponse> createErrorResponse(Exception e, String url) {
      return new ResponseWrapper<>(new JiraIssuesResponse(), 500, e, url);
   }
}
