package com.adcubum.timerecording.jira.jiraapi.readresponse.read;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.jupiter.api.Test;

import com.adcubum.timerecording.jira.jiraapi.readresponse.data.JiraGenericValuesResponse;
import com.adcubum.timerecording.jira.jiraapi.readresponse.read.JiraApiReaderImplIntegTest.HttpClientInterceptor;
import com.adcubum.timerecording.jira.jiraapi.readresponse.response.responsereader.JiraGenericValuesResponseReader;

class HttpClientIntegTest {

   @Test
   void test_Failed() {
      // Given
      HttpClient httpWrapper = new HttpClient();

      // When
      JiraGenericValuesResponse jiraGetBoardsResponse =
            httpWrapper.callRequestAndParse(new JiraGenericValuesResponseReader(), "https://bli.bla.blubb");

      // Then
      assertThat(jiraGetBoardsResponse.getException(), is(notNullValue()));
   }

   @Test
   void test_Success() {
      // Given
      int portNumber = 8080;
      String host = "127.0.0.1";
      String path = "test";
      DummyHttpGetServerTestCaseBuilder tcb = new DummyHttpGetServerTestCaseBuilder(portNumber)
            .withHost(host + ":" + 8080)
            .withHeaderAndResponse(path, JiraApiTestReadConst.READ_BOARD_SUCCESSFULL_RESPONSE)
            .withHttpWrapper(new HttpClientInterceptor(host, host, portNumber))
            .build();

      // When
      JiraGenericValuesResponse jiraGetBoardsResponse =
            tcb.httpWrapper.callRequestAndParse(new JiraGenericValuesResponseReader(), "http://" + host + "//" + path);

      // Then
      assertThat(jiraGetBoardsResponse.getException(), is(nullValue()));
      assertThat(jiraGetBoardsResponse.getValues().size(), is(2));
   }
}
