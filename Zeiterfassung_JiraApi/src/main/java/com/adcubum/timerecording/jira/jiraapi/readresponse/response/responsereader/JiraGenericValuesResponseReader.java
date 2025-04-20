package com.adcubum.timerecording.jira.jiraapi.readresponse.response.responsereader;

import com.adcubum.timerecording.jira.jiraapi.readresponse.data.generic.JiraGenericValuesResponse;
import com.brugalibre.common.http.model.response.ResponseWrapper;
import com.brugalibre.common.http.service.response.AbstractHttpResponseReader;

public class JiraGenericValuesResponseReader extends AbstractHttpResponseReader<JiraGenericValuesResponse> {

   @Override
   protected Class<JiraGenericValuesResponse> getResponseResultClass() {
      return JiraGenericValuesResponse.class;
   }

   @Override
   public ResponseWrapper<JiraGenericValuesResponse> createErrorResponse(Exception e, String url) {
      return new ResponseWrapper<>(new JiraGenericValuesResponse(), 500, e, url);
   }
}
