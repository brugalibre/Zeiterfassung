package com.adcubum.timerecording.jira.jiraapi.readresponse.response.responsereader;

import java.io.IOException;

import com.adcubum.timerecording.jira.jiraapi.readresponse.response.JiraResponse;
import com.fasterxml.jackson.databind.ObjectMapper;

import okhttp3.Response;

/**
 * Base implementation of a {@link ResponseReader}. Takes the body and maps it into any sub type of {@link JiraResponse}
 * Only this very sub type has to be provided by any subclass of this {@link AbstractJiraResponseReader}
 * 
 * @author Dominic
 *
 * @param <T>
 */
public abstract class AbstractJiraResponseReader<T extends JiraResponse> implements ResponseReader<T> {

   @Override
   public T readResponse(Response response) throws IOException {
      String bodyAsString = response.body().string();
      return new ObjectMapper().readValue(bodyAsString, getResponseResultClass());
   }

   protected abstract Class<T> getResponseResultClass();
}
