package com.adcubum.timerecording.jira.jiraapi.readresponse.read;

import com.adcubum.timerecording.jira.jiraapi.readresponse.data.generic.JiraGenericValuesResponse;
import com.adcubum.timerecording.jira.jiraapi.readresponse.response.responsereader.JiraGenericValuesResponseReader;
import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

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
      int portNumber = 8081; // Anpassung port auf 8081, damit sich Springboot & dieser Test nicht stören
      String host = "127.0.0.1";
      String path = "test";
      DummyHttpGetServerTestCaseBuilder tcb = new DummyHttpGetServerTestCaseBuilder(portNumber)
            .withHost(host + ":" + portNumber)
            .withHeaderAndResponse(path, JiraApiTestReadConst.READ_BOARD_SUCCESSFULL_RESPONSE)
            .withHttpWrapper(new HttpClient())
            .build();

      // When
      JiraGenericValuesResponse jiraGetBoardsResponse =
              tcb.httpWrapper.callRequestAndParse(new JiraGenericValuesResponseReader(), "http://" + host + ":" + portNumber + "//" + path);

      // Then
      assertThat(jiraGetBoardsResponse.getException(), is(nullValue()));
      assertThat(jiraGetBoardsResponse.getValues().size(), is(2));

      // finally
      tcb.clientServer.stop();
   }
}
