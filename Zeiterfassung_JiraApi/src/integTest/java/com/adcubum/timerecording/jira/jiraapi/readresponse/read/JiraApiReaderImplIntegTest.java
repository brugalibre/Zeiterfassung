package com.adcubum.timerecording.jira.jiraapi.readresponse.read;

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

import com.adcubum.timerecording.jira.jiraapi.http.HttpClient;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.adcubum.timerecording.jira.data.ticket.Ticket;
import com.adcubum.timerecording.jira.jiraapi.configuration.JiraApiConfiguration;
import com.adcubum.timerecording.jira.jiraapi.configuration.JiraApiConfigurationBuilder;
import com.adcubum.timerecording.jira.jiraapi.mapresponse.JiraApiReadTicketsResult;

class JiraApiReaderImplIntegTest {

   private static final String LOCALHOST = "127.0.0.1";
   private static int portNumber = 8181;
   private static JiraApiConfiguration jiraApiConfiguration;
   private static String restAgileApiPath;

   @BeforeAll
   public static void setUp() {
      jiraApiConfiguration = JiraApiConfigurationBuilder.of()
            .withDefaultJiraApiConfiguration()
            .withJiraUrl("http://" + LOCALHOST + ":" + portNumber)
            .build();
      // Path with which the request is registered on the mock-webserver. It's without the url and parameters!
      restAgileApiPath = jiraApiConfiguration.getJiraBoardBaseUrl()
            .replace(jiraApiConfiguration.getJiraUrl(), "");
   }

   @Test
   void testReadTicketsFromBoard_WithTickets() {
      // Given
      DummyHttpGetServerTestCaseBuilder tcb = new DummyHttpGetServerTestCaseBuilder(portNumber)
            .withHost(LOCALHOST + ":" + portNumber)
            .withHeaderAndResponse(restAgileApiPath, READ_BOARD_SUCCESSFULL_RESPONSE) // Request & Response for reading the board
            .withHeaderAndResponse(restAgileApiPath + "/" + BOARD_ID_FOR_NAME + "/sprint", READ_SPRINT_ID_RESPONSE) // Request & Response for reading the sprint id
            .withHeaderAndResponse(restAgileApiPath + "/" + BOARD_ID_FOR_NAME + "/sprint/" + SPRINT_ID_FOR_BOARD + "/issue", // Request & Response for reading the tickets for sprint
                  READ_SPRINT_ISSUES_RESPONSE)
            .withHttpWrapper(new HttpClient())
            .withJiraApiReader()
            .build();

      // When
      JiraApiReadTicketsResult ticketsFromBoard =
            tcb.jiraApiReader.readTicketsFromBoardAndSprints(JiraApiTestReadConst.BOARD_NAME, Collections.emptyList());
      assertThat(ticketsFromBoard.getTickets().size(), is(1));
      Ticket ticket = ticketsFromBoard.getTickets().get(0);
      assertThat(ticket.getNr(), is(ISSUE_NR));

      // finally
      tcb.clientServer.stop();
   }

   @Test
   void testReadTwoActiveSprintsFromBoard() {
      // Given
      DummyHttpGetServerTestCaseBuilder tcb = new DummyHttpGetServerTestCaseBuilder(portNumber)
            .withHost(LOCALHOST + ":" + portNumber)
            .withHeaderAndResponse(restAgileApiPath, READ_BOARD_SUCCESSFULL_RESPONSE) // Request & Response for reading the board
            // Request & Response for reading the sprint id
            .withHeaderAndResponse(restAgileApiPath + "/" + BOARD_ID_FOR_NAME + "/sprint", READ_SPRINT_ID_RESPONSE_TWO_ACTIVE_SPRINTS)
            // Request & Response for reading the tickets for the first active sprint
            .withHeaderAndResponse(restAgileApiPath + "/" + BOARD_ID_FOR_NAME + "/sprint/" + ACTIVE_SPRINT_ID_1_FOR_BOARD + "/issue",
                  READ_SPRINT_ISSUES_FOR_ACTIVE_SPRINT_1_RESPONSE)
            // Request & Response for reading the tickets for 2nd active sprint
            .withHeaderAndResponse(restAgileApiPath + "/" + BOARD_ID_FOR_NAME + "/sprint/" + ACTIVE_SPRINT_ID_2_FOR_BOARD + "/issue",
                  READ_SPRINT_ISSUES_FOR_ACTIVE_SPRINT_2_RESPONSE)
            .withHttpWrapper(new HttpClient())
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

      // finally
      tcb.clientServer.stop();
   }

   private Ticket findTicket4Id(List<Ticket> tickets, String id) {
      return tickets.stream()
            .filter(ticket -> id.equals(ticket.getNr()))
            .findFirst().orElse(null);
   }
}
