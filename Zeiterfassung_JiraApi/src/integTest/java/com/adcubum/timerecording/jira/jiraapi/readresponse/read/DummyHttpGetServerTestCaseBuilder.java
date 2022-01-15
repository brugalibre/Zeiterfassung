package com.adcubum.timerecording.jira.jiraapi.readresponse.read;

import com.adcubum.timerecording.jira.jiraapi.configuration.JiraApiConfiguration;
import com.adcubum.timerecording.jira.jiraapi.configuration.JiraApiConfigurationProvider;
import com.adcubum.timerecording.jira.jiraapi.http.HttpClient;
import org.mockserver.integration.ClientAndServer;
import org.mockserver.model.Header;
import org.mockserver.model.HttpRequest;
import org.mockserver.model.HttpResponse;
import org.mockserver.model.HttpStatusCode;

import static java.util.Objects.requireNonNull;

public class DummyHttpGetServerTestCaseBuilder {

   ClientAndServer clientServer;
   HttpClient httpWrapper;
   JiraApiReaderImpl jiraApiReader;
   String host;
   private JiraApiConfiguration jiraApiConfiguration;

   DummyHttpGetServerTestCaseBuilder(int port) {
      this.clientServer = ClientAndServer.startClientAndServer(port);
      this.jiraApiConfiguration = JiraApiConfigurationProvider.INSTANCE.getJiraApiConfiguration();
   }

   DummyHttpGetServerTestCaseBuilder withHttpWrapper(HttpClient httpWrapper) {
      this.httpWrapper = httpWrapper;
      return this;
   }

   public DummyHttpGetServerTestCaseBuilder withJiraApiReader() {
      this.jiraApiReader = new JiraApiReaderImpl(httpWrapper, jiraApiConfiguration);
      return this;
   }

   DummyHttpGetServerTestCaseBuilder withHost(String host) {
      this.host = host;
      return this;
   }

   DummyHttpGetServerTestCaseBuilder withHeaderAndResponse(String path, String expectedReceivedBody) {
      requireNonNull(host);
      clientServer.when(new HttpRequest()
            .withMethod("GET")
            .withPath(path)
            .withHeader(new Header("Host", host)))
            .respond(new HttpResponse()
                  .withStatusCode(HttpStatusCode.OK_200.code())
                  .withBody(expectedReceivedBody));
      return this;
   }

   DummyHttpGetServerTestCaseBuilder build() {
      return this;
   }
}
