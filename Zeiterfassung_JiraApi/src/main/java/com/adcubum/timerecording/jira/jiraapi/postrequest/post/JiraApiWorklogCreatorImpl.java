package com.adcubum.timerecording.jira.jiraapi.postrequest.post;

import com.adcubum.timerecording.jira.jiraapi.configuration.JiraApiConfiguration;
import com.adcubum.timerecording.jira.jiraapi.postrequest.post.data.JiraPostResponse;
import com.adcubum.timerecording.jira.jiraapi.postrequest.post.data.WorklogV2ObjectNode;
import com.adcubum.timerecording.jira.jiraapi.postrequest.post.worklog.JiraApiWorklogCreator;
import com.adcubum.timerecording.jira.jiraapi.postrequest.post.worklog.data.Worklog;
import com.adcubum.timerecording.jira.jiraapi.postrequest.response.responsereader.JiraPostResponseReader;
import com.adcubum.timerecording.jira.jiraapi.readresponse.read.JiraApiReaderFactory;
import com.adcubum.timerecording.security.login.auth.AuthenticationContext;
import com.adcubum.timerecording.security.login.auth.usercredentials.JiraBasicCredentialsProvider;
import com.adcubum.timerecording.security.login.auth.usercredentials.JiraCredentialsProvider;
import com.brugalibre.common.http.model.method.HttpMethod;
import com.brugalibre.common.http.model.request.HttpRequest;
import com.brugalibre.common.http.service.HttpService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Supplier;

import static com.adcubum.timerecording.jira.jiraapi.configuration.JiraApiConstants.ISSUE_ID_PLACE_HOLDER;

public class JiraApiWorklogCreatorImpl implements JiraApiWorklogCreator {

   private static final Logger LOG = LoggerFactory.getLogger(JiraApiWorklogCreatorImpl.class);
   private final HttpService httpService;
   private final JiraApiConfiguration jiraApiConfiguration;
   private final JiraCredentialsProvider jiraCredentialsProvider;

   /**
    * Private constructor called by the {@link JiraApiReaderFactory}
    *
    * @param jiraApiConfiguration the {@link JiraApiConfiguration}
    */
   private JiraApiWorklogCreatorImpl(JiraApiConfiguration jiraApiConfiguration,
                                     String username, Supplier<char[]> userPw) {
      this(new HttpService(30), jiraApiConfiguration, username, userPw);
   }

   /**
    * Package private constructor for testing purpose only!
    *
    * @param httpService           the {@link HttpService}
    * @param jiraApiConfiguration the {@link JiraApiConfiguration}
    */
   JiraApiWorklogCreatorImpl(HttpService httpService, JiraApiConfiguration jiraApiConfiguration,
                             String username, Supplier<char[]> userPw) {
      this.httpService = httpService;
      this.jiraApiConfiguration = jiraApiConfiguration;
      this.jiraCredentialsProvider = new JiraBasicCredentialsProvider();
      jiraCredentialsProvider.userAuthenticated(new AuthenticationContext(username, userPw));
   }

   @Override
   public JiraPostResponse createWorklog(Worklog worklog) {
      LOG.info("Create new worklog for input '{}'", worklog);
      String body = buildCreateWorklogJsonBody(worklog);
      String url = jiraApiConfiguration.getJiraCreateWorkUrl().replace(ISSUE_ID_PLACE_HOLDER, worklog.getIssueNr());
      HttpRequest request = HttpRequest.getHttpRequest(HttpMethod.POST, url)
              .withJsonBody(body)
              .withAuthorization(jiraCredentialsProvider.getCredentials());
      JiraPostResponse jiraPostResponse = httpService.callRequestParseAndUnwrap(new JiraPostResponseReader(), request);
      LOG.info("New worklog was {}successfully created!", jiraPostResponse.isSuccessful() ? "" : "NOT ");
      return jiraPostResponse;
   }

   private static String buildCreateWorklogJsonBody(Worklog worklog) {
      return WorklogV2ObjectNode.buildWorklogJsonV2(worklog.getTimeSpentSeconds(), worklog.getComment(), worklog.getStarted());
   }
}
