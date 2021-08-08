package com.adcubum.timerecording.jira.jiraapi.readresponse.read;

import static com.adcubum.timerecording.jira.jiraapi.constant.JiraApiConstants.ADC_JIRA_BOARD_BASE_URL;
import static com.adcubum.timerecording.jira.jiraapi.constant.JiraApiConstants.ADC_JIRA_HOST;
import static com.adcubum.timerecording.jira.jiraapi.readresponse.read.JiraApiTestReadConst.ACTIVE_SPRINT_ID_1_FOR_BOARD;
import static com.adcubum.timerecording.jira.jiraapi.readresponse.read.JiraApiTestReadConst.ACTIVE_SPRINT_ID_2_FOR_BOARD;
import static com.adcubum.timerecording.jira.jiraapi.readresponse.read.JiraApiTestReadConst.BOARD_ID_FOR_NAME;
import static com.adcubum.timerecording.jira.jiraapi.readresponse.read.JiraApiTestReadConst.ISSUE_NR;
import static com.adcubum.timerecording.jira.jiraapi.readresponse.read.JiraApiTestReadConst.ISSUE_NR_FROM_ACTIVE_SPRINT_2;
import static com.adcubum.timerecording.jira.jiraapi.readresponse.read.JiraApiTestReadConst.READ_BOARD_SUCCESSFULL_RESPONSE;
import static com.adcubum.timerecording.jira.jiraapi.readresponse.read.JiraApiTestReadConst.READ_SPRINT_ID_RESPONSE;
import static com.adcubum.timerecording.jira.jiraapi.readresponse.read.JiraApiTestReadConst.READ_SPRINT_ID_RESPONSE_TWO_ACTIVE_SPRINTS;
import static com.adcubum.timerecording.jira.jiraapi.readresponse.read.JiraApiTestReadConst.READ_SPRINT_ISSUES_FOR_ACTIVE_SPRINT_1_RESPONSE;
import static com.adcubum.timerecording.jira.jiraapi.readresponse.read.JiraApiTestReadConst.READ_SPRINT_ISSUES_FOR_ACTIVE_SPRINT_2_RESPONSE;
import static com.adcubum.timerecording.jira.jiraapi.readresponse.read.JiraApiTestReadConst.READ_SPRINT_ISSUES_RESPONSE;
import static com.adcubum.timerecording.jira.jiraapi.readresponse.read.JiraApiTestReadConst.SPRINT_ID_FOR_BOARD;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.adcubum.timerecording.jira.data.ticket.Ticket;
import com.adcubum.timerecording.jira.jiraapi.mapresponse.JiraApiReadTicketsResult;
import com.adcubum.timerecording.jira.jiraapi.readresponse.response.JiraResponse;
import com.adcubum.timerecording.jira.jiraapi.readresponse.response.responsereader.ResponseReader;

class JiraApiReaderImplIntegTest {

   private static final String REST_AGILE_API_PATH = ADC_JIRA_BOARD_BASE_URL.replace(ADC_JIRA_HOST, "");
   private static final String LOCALHOST = "127.0.0.1";

   @Test
   void testReadTicketsFromBoard_WithTickets() {
      // Given
      int portNumber = 8181;
      String host = LOCALHOST;
      DummyHttpGetServerTestCaseBuilder tcb = new DummyHttpGetServerTestCaseBuilder(portNumber)
            .withHost(host + ":" + portNumber)
            .withHeaderAndResponse(REST_AGILE_API_PATH, READ_BOARD_SUCCESSFULL_RESPONSE) // Request & Response for reading the board
            .withHeaderAndResponse(REST_AGILE_API_PATH + "/" + BOARD_ID_FOR_NAME + "/sprint", READ_SPRINT_ID_RESPONSE) // Request & Response for reading the sprint id
            .withHeaderAndResponse(REST_AGILE_API_PATH + "/" + BOARD_ID_FOR_NAME + "/sprint/" + SPRINT_ID_FOR_BOARD + "/issue", // Request & Response for reading the tickets for sprint 
                  READ_SPRINT_ISSUES_RESPONSE)
            .withHttpWrapper(new HttpClientInterceptor(ADC_JIRA_HOST, "http://" + LOCALHOST, portNumber))
            .withJiraApiReader()
            .build();

      // When
      JiraApiReadTicketsResult ticketsFromBoard =
            tcb.jiraApiReader.readTicketsFromBoardAndSprints(JiraApiTestReadConst.BOARD_NAME, Collections.emptyList());
      assertThat(ticketsFromBoard.getTickets().size(), is(1));
      Ticket ticket = ticketsFromBoard.getTickets().get(0);
      assertThat(ticket.getNr(), is(ISSUE_NR));
   }

   @Test
   void testReadTwoActiveSprintsFromBoard() {
      // Given
      int portNumber = 8181;
      String host = LOCALHOST;
      DummyHttpGetServerTestCaseBuilder tcb = new DummyHttpGetServerTestCaseBuilder(portNumber)
            .withHost(host + ":" + portNumber)
            .withHeaderAndResponse(REST_AGILE_API_PATH, READ_BOARD_SUCCESSFULL_RESPONSE) // Request & Response for reading the board
            // Request & Response for reading the sprint id
            .withHeaderAndResponse(REST_AGILE_API_PATH + "/" + BOARD_ID_FOR_NAME + "/sprint", READ_SPRINT_ID_RESPONSE_TWO_ACTIVE_SPRINTS)
            // Request & Response for reading the tickets for the first active sprint
            .withHeaderAndResponse(REST_AGILE_API_PATH + "/" + BOARD_ID_FOR_NAME + "/sprint/" + ACTIVE_SPRINT_ID_1_FOR_BOARD + "/issue",
                  READ_SPRINT_ISSUES_FOR_ACTIVE_SPRINT_1_RESPONSE)
            // Request & Response for reading the tickets for 2nd active sprint
            .withHeaderAndResponse(REST_AGILE_API_PATH + "/" + BOARD_ID_FOR_NAME + "/sprint/" + ACTIVE_SPRINT_ID_2_FOR_BOARD + "/issue",
                  READ_SPRINT_ISSUES_FOR_ACTIVE_SPRINT_2_RESPONSE)
            .withHttpWrapper(new HttpClientInterceptor(ADC_JIRA_HOST, "http://" + LOCALHOST, portNumber))
            .withJiraApiReader()
            .build();

      // When
      JiraApiReadTicketsResult ticketsFromBoard =
            tcb.jiraApiReader.readTicketsFromBoardAndSprints(JiraApiTestReadConst.BOARD_NAME, Collections.emptyList());
      assertThat(ticketsFromBoard.getTickets().size(), is(2));
      Ticket ticketOfSprint1 = findTicket4Id(ticketsFromBoard.getTickets(), JiraApiTestReadConst.ISSUE_NR_FROM_ACTIVE_SPRINT_1);
      Ticket ticketOfSprint2 = findTicket4Id(ticketsFromBoard.getTickets(), ISSUE_NR_FROM_ACTIVE_SPRINT_2);
      assertThat(ticketOfSprint1, is(notNullValue()));
      assertThat(ticketOfSprint2, is(notNullValue()));
   }

   private Ticket findTicket4Id(List<Ticket> tickets, String id) {
      return tickets.stream()
            .filter(ticket -> id.equals(ticket.getNr()))
            .findFirst().orElse(null);
   }

   public static class HttpClientInterceptor extends HttpClient {

      private int port;
      private String host2Replace;
      private String host2ReplaceWith;

      public HttpClientInterceptor(String host2Replace, String host2ReplaceWith, int port) {
         super();
         this.port = port;
         this.host2Replace = host2Replace;
         this.host2ReplaceWith = host2ReplaceWith;
      }

      @Override
      public <T extends JiraResponse> T callRequestAndParse(ResponseReader<T> reader, String url) {
         String interceptUrl = url.replace(host2Replace, host2ReplaceWith + ":" + port);// sneaky change the url and port to our local one in order to redirect the calls to our mock server
         return super.callRequestAndParse(reader, interceptUrl);
      }
   }
}
