package com.adcubum.timerecording.jira.jiraapi.readresponse.read;

import static java.util.Objects.requireNonNull;

import org.mockserver.integration.ClientAndServer;
import org.mockserver.model.Header;
import org.mockserver.model.HttpRequest;
import org.mockserver.model.HttpResponse;
import org.mockserver.model.HttpStatusCode;

public class DummyHttpGetServerTestCaseBuilder {

   ClientAndServer clientServer;
   HttpClient httpWrapper;
   JiraApiReader jiraApiReader;
   String host;

   DummyHttpGetServerTestCaseBuilder(int port) {
      this.clientServer = ClientAndServer.startClientAndServer(port);
   }

   DummyHttpGetServerTestCaseBuilder withHttpWrapper(HttpClient httpWrapper) {
      this.httpWrapper = httpWrapper;
      return this;
   }

   public DummyHttpGetServerTestCaseBuilder withJiraApiReader() {
      this.jiraApiReader = new JiraApiReader(httpWrapper);
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
