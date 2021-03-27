package com.adcubum.timerecording.jira.jiraapi.readresponse.response.responsereader;

import java.io.IOException;

import com.adcubum.timerecording.jira.jiraapi.readresponse.read.JiraApiReader;
import com.adcubum.timerecording.jira.jiraapi.readresponse.response.JiraResponse;

import okhttp3.Response;

/**
 * The {@link ResponseReader} reads the json response the {@link JiraApiReader} has recieved and maps
 * it in any subtype of {@link JiraResponse}
 * 
 * @author Dominic
 *
 */
public interface ResponseReader<T extends JiraResponse> {

   /**
    * Reads the given {@link ResponseReader} and maps it into a {@link JiraResponse}
    * 
    * @param response
    *        the Response to read
    * @return the {@link JiraResponse} as a parsed result
    * @throws IOException
    */
   T readResponse(Response response) throws IOException;

   /**
    * Creates an error response which contains further information about the failed call
    * 
    * @param e
    *        the exception which occurred
    * @param url
    *        the url which was posted in order to get a result
    * @return an error response which contains further information about the failed call
    */
   T createErrorResponse(Exception e, String url);
}
