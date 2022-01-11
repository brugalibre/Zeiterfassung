package com.adcubum.timerecording.jira.jiraapi.postrequest.post;

import com.adcubum.timerecording.jira.jiraapi.configuration.JiraApiConfiguration;
import com.adcubum.timerecording.jira.jiraapi.http.HttpClient;
import com.adcubum.timerecording.jira.jiraapi.postrequest.post.data.JiraPostResponse;
import com.adcubum.timerecording.jira.jiraapi.postrequest.post.data.WorklogV2ObjectNode;
import com.adcubum.timerecording.jira.jiraapi.postrequest.post.worklog.JiraApiWorklogCreator;
import com.adcubum.timerecording.jira.jiraapi.postrequest.post.worklog.data.Worklog;
import com.adcubum.timerecording.jira.jiraapi.postrequest.response.responsereader.JiraPostResponseReader;
import com.adcubum.timerecording.jira.jiraapi.readresponse.read.JiraApiReaderFactory;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Supplier;

import static com.adcubum.timerecording.jira.jiraapi.configuration.JiraApiConstants.ISSUE_ID_PLACE_HOLDER;

public class JiraApiWorklogCreatorImpl implements JiraApiWorklogCreator {

   private static final Logger LOG = LoggerFactory.getLogger(JiraApiWorklogCreatorImpl.class);
   private HttpClient httpClient;
   private JiraApiConfiguration jiraApiConfiguration;

   /**
    * Private constructor called by the {@link JiraApiReaderFactory}
    *
    * @param jiraApiConfiguration the {@link JiraApiConfiguration}
    * @param username             the username
    * @param userPw               the password supplier
    */
   private JiraApiWorklogCreatorImpl(JiraApiConfiguration jiraApiConfiguration, String username, Supplier<char[]> userPw) {
      this(new HttpClient(), jiraApiConfiguration, username, userPw);
   }

   /**
    * Package private constructor for testing purpose only!
    *
    * @param httpClient           the {@link HttpClient}
    * @param jiraApiConfiguration the {@link JiraApiConfiguration}
    */
   JiraApiWorklogCreatorImpl(HttpClient httpClient, JiraApiConfiguration jiraApiConfiguration, String username, Supplier<char[]> userPw) {
      this.httpClient = httpClient;
      this.jiraApiConfiguration = jiraApiConfiguration;
      this.httpClient.setCredentials(username, String.valueOf(userPw.get()));
   }

   @Override
   public JiraPostResponse createWorklog(Worklog worklog) {
      LOG.info("Create new worklog for input '{}'", worklog);
      RequestBody body = buildCreateWorklogRequestBody(worklog);
      String url = jiraApiConfiguration.getJiraCreateWorkUrl().replace(ISSUE_ID_PLACE_HOLDER, worklog.getIssueNr());
      JiraPostResponse jiraPostResponse = httpClient.callPostRequestAndParse(new JiraPostResponseReader(), url, body);
      LOG.info("New worklog was {}successfully created!", jiraPostResponse.isSuccessful() ? "" : "NOT ");
      return jiraPostResponse;
   }

   @SuppressWarnings("deprecation")
   private static RequestBody buildCreateWorklogRequestBody(Worklog worklog) {
      String json = WorklogV2ObjectNode.buildWorklogJsonV2(worklog.getTimeSpentSeconds(), worklog.getComment(), worklog.getStarted());
      // Use OkHttp v3 signature to ensure binary compatibility between v3 and v4
      // https://github.com/auth0/auth0-java/issues/324
      return RequestBody.create(MediaType.parse("application/json"), json);
   }
}
