package com.adcubum.timerecording.jira.jiraapi.readresponse.response.responsereader;

import com.adcubum.timerecording.jira.jiraapi.readresponse.data.JiraGenericValuesResponse;

public class JiraGenericValuesResponseReader extends AbstractJiraResponseReader<JiraGenericValuesResponse> {

   @Override
   protected Class<JiraGenericValuesResponse> getResponseResultClass() {
      return JiraGenericValuesResponse.class;
   }

   @Override
   public JiraGenericValuesResponse createErrorResponse(Exception e, String url) {
      return new JiraGenericValuesResponse(e, url);
   }
}
