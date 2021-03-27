package com.adcubum.timerecording.jira.jiraapi.readresponse.read;

import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import com.adcubum.timerecording.jira.jiraapi.readresponse.response.JiraResponse;
import com.adcubum.timerecording.jira.jiraapi.readresponse.response.responsereader.ResponseReader;

import okhttp3.Credentials;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * {@link HttpClient} acts as a wrapper to an actual implementation of a http-client. So all http-calls are delegated to this
 * implementation. Currently a {@link OkHttpClient} is used for calling any http- {@link Request}s
 * 
 * @author Dominic
 *
 */
public class HttpClient {

   private static final Logger LOG = Logger.getLogger(HttpClient.class);
   private String credentials;
   private OkHttpClient okHttpClient;

   /**
    * Creates a new {@link HttpClient} without any credentials
    */
   HttpClient() {
      this.okHttpClient = buildOkHttpClient(20);
      this.credentials = "";
   }

   /**
    * Defines the credentials for this {@link HttpClient} with the provided username and password
    * 
    * @param username
    *        the username
    * @param pw
    *        the password
    */
   public void setCredentials(String username, String pw) {
      this.credentials = Credentials.basic(username, pw);
   }

   /**
    * Makes a get request to the given url and reads the result with the given {@link ResponseReader}
    * Any Exception which occurrs will also be mapped by the provided {@link ResponseReader}
    * 
    * @param reader
    *        the {@link ResponseReader} which reads and maps the received result
    * @param url
    *        the url which is called
    * @param <T>
    *        defines the exact type of result
    * @return a {@link JiraResponse}
    */
   public <T extends JiraResponse> T callRequestAndParse(ResponseReader<T> reader, String url) {
      Request request = buildRequest(url);
      try (Response response = okHttpClient.newCall(request).execute()) {
         return reader.readResponse(response);
      } catch (Exception e) {
         LOG.error("Unable to read from url '" + url + "'!");
         return reader.createErrorResponse(e, url);
      }
   }

   private static Request buildRequest(String url) {
      return new Request.Builder()
            .url(url)
            .build(); // defaults to GET
   }

   private OkHttpClient buildOkHttpClient(int timeOut) {
      return new OkHttpClient().newBuilder()
            .readTimeout(timeOut, TimeUnit.SECONDS)
            .addInterceptor(getAuthorizationInterceptor())
            .build();
   }

   private Interceptor getAuthorizationInterceptor() {
      return chain -> {
         Request request = chain.request();
         return chain.proceed(request.newBuilder()
               .header("Authorization", credentials)
               .build());
      };
   }
}
