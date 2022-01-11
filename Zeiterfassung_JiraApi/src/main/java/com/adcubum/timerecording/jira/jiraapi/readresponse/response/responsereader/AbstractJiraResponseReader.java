package com.adcubum.timerecording.jira.jiraapi.readresponse.response.responsereader;

import java.io.IOException;

import com.adcubum.timerecording.jira.jiraapi.readresponse.response.JiraResponse;
import com.fasterxml.jackson.databind.ObjectMapper;

import okhttp3.Response;

/**
 * Base implementation of a {@link ResponseReader}. Takes the body and maps it into any sub type of {@link JiraResponse}
 * Only this very subtype has to be provided by any subclass of this {@link AbstractJiraResponseReader}
 *
 * @param <T>
 * @author Dominic
 */
public abstract class AbstractJiraResponseReader<T extends JiraResponse> implements ResponseReader<T> {

   @Override
   public T readResponse(Response response) throws IOException {
      String bodyAsString = response.body().string();
      T jiraResponse = new ObjectMapper().readValue(bodyAsString, getResponseResultClass());
      setResponsValue(response, jiraResponse);
      return jiraResponse;
   }

   private static <T extends JiraResponse> void setResponsValue(Response response, T jiraResponse) {
      jiraResponse.setIsSuccessful(response.isSuccessful());
   }

   protected abstract Class<T> getResponseResultClass();
}
