package com.adcubum.timerecording.jira.jiraapi.readresponse.response.responsereader;

import com.adcubum.timerecording.jira.jiraapi.readresponse.data.JiraIssueResponse;
import com.brugalibre.common.http.model.response.ResponseWrapper;
import com.brugalibre.common.http.service.response.AbstractHttpResponseReader;

public class JiraIssueResponseReader extends AbstractHttpResponseReader<JiraIssueResponse> {

   @Override
   protected Class<JiraIssueResponse> getResponseResultClass() {
      return JiraIssueResponse.class;
   }

   @Override
   public ResponseWrapper<JiraIssueResponse> createErrorResponse(Exception e, String url) {
      return new ResponseWrapper<>(new JiraIssueResponse(), 500, e, url);
   }
}
