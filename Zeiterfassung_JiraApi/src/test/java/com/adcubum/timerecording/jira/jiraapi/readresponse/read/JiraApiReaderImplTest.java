package com.adcubum.timerecording.jira.jiraapi.readresponse.read;

import com.adcubum.timerecording.jira.data.ticket.IssueType;
import com.adcubum.timerecording.jira.data.ticket.Ticket;
import com.adcubum.timerecording.jira.data.ticket.TicketAttrs;
import com.adcubum.timerecording.jira.jiraapi.configuration.JiraApiConfiguration;
import com.adcubum.timerecording.jira.jiraapi.configuration.JiraApiConfigurationFactory;
import com.adcubum.timerecording.jira.jiraapi.configuration.JiraApiConstants;
import com.adcubum.timerecording.jira.jiraapi.http.HttpClient;
import com.adcubum.timerecording.jira.jiraapi.mapresponse.JiraApiReadTicketsResult;
import com.adcubum.timerecording.jira.jiraapi.readresponse.data.*;
import com.adcubum.timerecording.jira.jiraapi.readresponse.data.JiraBoardsResponse.JiraBoardResponse;
import com.adcubum.timerecording.jira.jiraapi.readresponse.data.JiraIssueFields.JiraIssueType;
import com.adcubum.timerecording.jira.jiraapi.readresponse.data.generic.JiraGenericValuesResponse;
import com.adcubum.timerecording.jira.jiraapi.readresponse.data.generic.JiraGenericValuesResponse.GenericNameAttrs;
import com.adcubum.timerecording.jira.jiraapi.readresponse.response.responsereader.JiraBoardResponseReader;
import com.adcubum.timerecording.jira.jiraapi.readresponse.response.responsereader.JiraGenericValuesResponseReader;
import com.adcubum.timerecording.jira.jiraapi.readresponse.response.responsereader.JiraIssueResponseReader;
import com.adcubum.timerecording.jira.jiraapi.readresponse.response.responsereader.JiraIssuesResponseReader;
import com.adcubum.timerecording.security.login.auth.AuthenticationContext;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.adcubum.timerecording.jira.jiraapi.configuration.JiraApiConstants.*;
import static java.util.Objects.nonNull;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class JiraApiReaderImplTest {

   @Test
   void testUserAuthenticated() {
      // Given
      String pwd = "";
      String username = "";
      AuthenticationContext atuhenticationContext = new AuthenticationContext(username, () -> pwd.toCharArray());
      HttpClient httpClient = mock(HttpClient.class);
      JiraApiConfiguration jiraApiConfiguration = JiraApiConfigurationFactory.createDefault();
      JiraApiReader jiraApiReader = new JiraApiReaderImpl(httpClient, jiraApiConfiguration);

      // When
      jiraApiReader.userAuthenticated(atuhenticationContext);

      // When
      verify(httpClient).setCredentials(eq(username), eq(pwd));
   }

   @Test
   void testReadTicket4Id_VerifyProjectDescAndNr() {
      // Given
      String ticketNr = "SYRIUS-65468";
      String issueType = "Bug";
      String expectedProjectDesc = "Scrum Aufwände";
      long expectedProjectNumber = 536003;

      JiraApiReaderImpl jiraApiReader = new TestCaseBuilder()
            .withTicketNr(ticketNr)
            .withJiraIssueResponse(true, issueType)
            .withCustomField_10060(expectedProjectNumber + " " + expectedProjectDesc + "")
            .build();

      // When
      Optional<Ticket> actualTicketOpt = jiraApiReader.readTicket4Nr(ticketNr);

      // Then
      assertThat(actualTicketOpt.isPresent(), is(true));
      Ticket actualTicket = actualTicketOpt.get();
      TicketAttrs ticketAttrs = actualTicket.getTicketAttrs();
      assertThat(ticketAttrs.getProjectNr(), is(expectedProjectNumber));
      assertThat(ticketAttrs.getProjectDesc(), is(expectedProjectDesc));
      assertThat(ticketAttrs.getExternalNr(), is(nullValue()));
      assertThat(ticketAttrs.getEpicNr(), is(nullValue()));
      assertThat(ticketAttrs.getImplementationPackage(), is(""));
      assertThat(ticketAttrs.getBusinessTeamPlaning(), is(nullValue()));
      assertThat(ticketAttrs.getPlaningId(), is(nullValue()));
      assertThat(ticketAttrs.getSyriusExtension(), is(nullValue()));
      assertThat(ticketAttrs.getSubthema(), is(nullValue()));
      assertThat(ticketAttrs.getSyriusRelease(), is("[]"));
      assertThat(ticketAttrs.getIssueType(), is(IssueType.BUG));
   }

   @Test
   void testReadTicket4Id_VerifyThemaAndProjectCostUnit() {
      // Given
      String ticketNr = "SYRIUS-1324";
      String expectedThema = "Vertragsverwaltung (Health)";
      int expectedProjectCostUnit = 41003;
      String issueType = "Feature";

      JiraApiReaderImpl jiraApiReader = new TestCaseBuilder()
            .withTicketNr(ticketNr)
            .withJiraIssueResponse(true, issueType)
            .withCustomfield_11310(expectedThema + " (" + expectedProjectCostUnit + ")")
            .build();

      // When
      Optional<Ticket> actualTicketOpt = jiraApiReader.readTicket4Nr(ticketNr);

      // Then
      assertThat(actualTicketOpt.isPresent(), is(true));
      Ticket actualTicket = actualTicketOpt.get();
      assertThat(actualTicket.getTicketAttrs().getThema(), is(expectedThema));
      assertThat(actualTicket.getTicketAttrs().getProjectCostUnit(), is(expectedProjectCostUnit));
      assertThat(actualTicket.getTicketAttrs().getIssueType(), is(IssueType.FEATURE));
   }

   @Test
   void testReadTicket4Id_FailedReadTicket() {
      // Given
      String ticketNr = "SYRIUS-1324";
      JiraApiReaderImpl jiraApiReader = new TestCaseBuilder()
            .withTicketNr(ticketNr)
            .withJiraIssueResponse(false, null)
            .build();

      // When
      Optional<Ticket> actualTicketOpt = jiraApiReader.readTicket4Nr(ticketNr);

      // Then
      assertThat(actualTicketOpt.isPresent(), is(false));
   }

   @Test
   void testReadTicketsFromBoard_NoBoard4Id() {
      // Given
      String boardName = "My Favorit Jira Board Name";

      JiraApiReaderImpl jiraApiReader = new TestCaseBuilder()
            .withJiraIssueResponse(false, null)
            .build();

      // When
      JiraApiReadTicketsResult actualJiraApiReadTicketsResult = jiraApiReader.readTicketsFromBoardAndSprints(boardName, Collections.emptyList());

      // Then
      assertThat(actualJiraApiReadTicketsResult, is(notNullValue()));
      assertThat(actualJiraApiReadTicketsResult.getTickets(), is(notNullValue()));
      assertThat(actualJiraApiReadTicketsResult.getTickets().isEmpty(), is(true));
   }

   @Test
   void testReadTicketsFromBoard_WithTickets() {
      // Given
      String boardName = "My not so favorit Jira Board Name";
      String futurSprintName = "futurSprintName";
      JiraIssue jiraIssue = new JiraIssue();
      jiraIssue.setId("1");
      JiraIssue futureJiraIssue = new JiraIssue();
      futureJiraIssue.setId("2");
      JiraApiReaderImpl jiraApiReader = new TestCaseBuilder()
            .withBoardName(boardName)
            .withFutureSprintId("2", futurSprintName)
            .withReceivedSprintIssues(Collections.singletonList(jiraIssue))
            .withReceivedFutureSprintIssues(Collections.singletonList(futureJiraIssue))
            .build();

      // When
      JiraApiReadTicketsResult actualJiraApiReadTicketsResult =
            jiraApiReader.readTicketsFromBoardAndSprints(boardName, Collections.singletonList(futurSprintName));

      // Then
      assertThat(actualJiraApiReadTicketsResult, is(notNullValue()));
      assertThat(actualJiraApiReadTicketsResult.getTickets(), is(notNullValue()));
      assertThat(actualJiraApiReadTicketsResult.getTickets().size(), is(2));
   }

   @Test
   void testReadTicketsFromBoard_WithMoreThanMaxResTickets() {
      // Given
      String boardName = "My other Favorit Jira Board Name";
      int amountOfReceivedTickets = JIRA_MAX_RESULTS_RETURNED + 1;
      List<JiraIssue> receivedIssues = createReceivedTickets(amountOfReceivedTickets);
      JiraApiReaderImpl jiraApiReader = new TestCaseBuilder()
            .withBoardName(boardName)
            .withReceivedSprintIssues(receivedIssues)
            .build();

      // When
      JiraApiReadTicketsResult actualJiraApiReadTicketsResult = jiraApiReader.readTicketsFromBoardAndSprints(boardName, Collections.emptyList());

      // Then
      assertThat(actualJiraApiReadTicketsResult, is(notNullValue()));
      assertThat(actualJiraApiReadTicketsResult.getTickets(), is(notNullValue()));
      assertThat(actualJiraApiReadTicketsResult.getTickets().size(), is(amountOfReceivedTickets));
   }

   private List<JiraIssue> createReceivedTickets(int amountOfReceivedTickets) {
      List<JiraIssue> receivedIssues = new ArrayList<>();
      for (int i = 0; i < amountOfReceivedTickets; i++) {
         JiraIssue e = new JiraIssue();
         e.setKey(String.valueOf(Math.random()));// we need a unique key to add the ticket
         e.setId(String.valueOf(Math.random()));// we need also a unique id to add the ticket
         receivedIssues.add(e);
      }
      return receivedIssues;
   }

   private static class TestCaseBuilder {

      private JiraApiConfiguration jiraApiConfiguration;
      private HttpClient httpClient;
      private String ticketNr;
      private JiraIssueResponse jiraIssueResponse;
      private JiraBoardsResponse jiraGetBoardsResponse;
      private JiraGenericValuesResponse jiraGetSprintResponse;
      private JiraGenericValuesResponse jiraGetFuturSprintResponse;
      private List<JiraIssue> issues;
      private String boardName;
      private List<JiraIssue> futureIssues;
      private String futurSprintId;
      private String futurSprintName;

      private TestCaseBuilder() {
         this.httpClient = mock(HttpClient.class);
         this.jiraIssueResponse = new JiraIssueResponse();
         this.jiraGetBoardsResponse = new JiraBoardsResponse();
         this.jiraGetSprintResponse = new JiraGenericValuesResponse();
         this.jiraGetFuturSprintResponse = new JiraGenericValuesResponse();
         this.issues = new ArrayList<>();
         this.futureIssues = new ArrayList<>();
         this.jiraApiConfiguration = JiraApiConfigurationFactory.createDefault();
      }

      public TestCaseBuilder withBoardName(String boardName) {
         this.boardName = boardName;
         return this;
      }

      public TestCaseBuilder withTicketNr(String ticketNr) {
         this.ticketNr = ticketNr;
         return this;
      }

      public TestCaseBuilder withReceivedSprintIssues(List<JiraIssue> issues) {
         this.issues = issues;
         return this;
      }

      public TestCaseBuilder withFutureSprintId(String futurSprintId, String futurSprintName) {
         this.futurSprintId = futurSprintId;
         this.futurSprintName = futurSprintName;
         return this;
      }

      public TestCaseBuilder withReceivedFutureSprintIssues(List<JiraIssue> futureIssues) {
         this.futureIssues = futureIssues;
         return this;
      }

      public TestCaseBuilder withJiraIssueResponse(boolean isSuccessful, String issueType) {
         createJiraIssueResponse(isSuccessful, issueType);
         return this;
      }

      private void createJiraIssueResponse(boolean isSuccessful, String issueType) {
         if (isSuccessful) {
            jiraIssueResponse.setKey(ticketNr);
            jiraIssueResponse.setId(String.valueOf(Math.random() * 10));
         }
         JiraIssueFields fields = new JiraIssueFields();
         JiraIssueType issueType2 = new JiraIssueType();
         issueType2.setName(issueType);
         fields.setIssuetype(issueType2);
         jiraIssueResponse.setFields(fields);
      }

      public TestCaseBuilder withCustomField_10060(String value) {
         JiraIssueGenericValueObject field10060 = new JiraIssueGenericValueObject();
         field10060.setValue(value);
         jiraIssueResponse.getFields().setCustomfield_10060(field10060);
         return this;
      }

      public TestCaseBuilder withCustomfield_11310(String value) {
         JiraIssueThemaInfos field11310 = new JiraIssueThemaInfos();
         field11310.setValue(value);
         jiraIssueResponse.getFields().setCustomfield_11310(field11310);
         return this;
      }

      private JiraApiReaderImpl build() {
         String boardId = issues.isEmpty() ? BoardInfo.UNKNOWN : "boardId25";
         String sprintId = "1";
         mockReadTicket4TicketNr();
         mockReadTicketsFromBoardName(boardId, sprintId, futurSprintId, futurSprintName);
         return new JiraApiReaderImpl(httpClient, jiraApiConfiguration);
      }

      private void mockReadTicketsFromBoardName(String boardId, String sprintId, String futurSprintId, String futurSprintName) {
         prepareReadResults(boardId, sprintId, null, issues, jiraGetSprintResponse);
         if (nonNull(futurSprintId)) {
            prepareReadResults(boardId, futurSprintId, futurSprintName, futureIssues, jiraGetFuturSprintResponse);
         }
         mockReadBoards();
         mockReadSprint(boardId);
         mockReadSprintTicketsUntilMaxResults(boardId, sprintId, issues);
         if (nonNull(futurSprintId)) {
            mockReadSprintTicketsUntilMaxResults(boardId, futurSprintId, futureIssues);
         }
         mockReadFutureSprint(boardId);
         mockReadSprintTicketsFromMaxResults(boardId, sprintId);
      }

      private void mockReadBoards() {
         int index = 0;
         do {
            String getAllBoardsUrl = jiraApiConfiguration.getGetAllBoardUrls().replace(START_AT_PLACE_HOLDER, String.valueOf(index));
            when(httpClient.callRequestAndParse(any(JiraBoardResponseReader.class), eq(getAllBoardsUrl))).thenReturn(jiraGetBoardsResponse);
            index = index + JiraApiConstants.JIRA_MAX_RESULTS_RETURNED;

         } while (index < JiraApiConstants.MAX_RESULTS);
      }

      private void mockReadSprint(String boardId) {
         String getSprintIdUrl = jiraApiConfiguration.getGetActiveSprintIdsForBoardUrl().replace(BOARD_ID_PLACE_HOLDER, boardId);
         when(httpClient.callRequestAndParse(any(JiraGenericValuesResponseReader.class), eq(getSprintIdUrl))).thenReturn(jiraGetSprintResponse);
      }

      private void mockReadFutureSprint(String boardId) {
         String getFuturSprintIdUrl = jiraApiConfiguration.getGetFuturSprintIdsForBoardUrl().replace(BOARD_ID_PLACE_HOLDER, boardId);
         when(httpClient.callRequestAndParse(any(JiraGenericValuesResponseReader.class), eq(getFuturSprintIdUrl)))
               .thenReturn(jiraGetFuturSprintResponse);
      }

      private void prepareReadResults(String boardId, String sprintId, String sprintName, List<JiraIssue> sprintIssues,
            JiraGenericValuesResponse jiraGetSprintResponse) {
         // If we except to receive any tickets from the sprint, we have to set the 'values' in the sprint result -> we found a sprint -> go ahead for the tickets
         if (!sprintIssues.isEmpty()) {
            JiraBoardResponse genericBoardNameAttrs = new JiraBoardResponse();
            genericBoardNameAttrs.setId(boardId);
            genericBoardNameAttrs.setName(boardName);
            jiraGetBoardsResponse.setValues(Collections.singletonList(genericBoardNameAttrs));

            GenericNameAttrs genericSprintNameAttrs = new GenericNameAttrs();
            genericSprintNameAttrs.setId(sprintId);
            genericSprintNameAttrs.setName(sprintName);
            jiraGetSprintResponse.setValues(Collections.singletonList(genericSprintNameAttrs));
         }
      }

      private void mockReadSprintTicketsFromMaxResults(String boardId, String sprintId) {
         if (issues.size() >= JIRA_MAX_RESULTS_RETURNED) {
            String maxPlus1 = String.valueOf(JIRA_MAX_RESULTS_RETURNED + 1);
            String url = createGetIssues4BoardUrl(boardId, sprintId)
                  .replace(START_AT_PLACE_LITERAL + "=0", START_AT_PLACE_LITERAL + "=" + maxPlus1);// Das isch wie nöd eso schöö...
            JiraIssuesResponse jiraIssuesResponse = new JiraIssuesResponse();
            jiraIssuesResponse.setIssues(issues.subList(JIRA_MAX_RESULTS_RETURNED, issues.size()));
            jiraIssuesResponse.setTotal(issues.size());
            when(httpClient.callRequestAndParse(any(JiraIssuesResponseReader.class), eq(url))).thenReturn(jiraIssuesResponse);
         }
      }

      private void mockReadSprintTicketsUntilMaxResults(String boardId, String sprintId, List<JiraIssue> issues) {
         String createGetIssues4BoardUrl = createGetIssues4BoardUrl(boardId, sprintId);
         JiraIssuesResponse jiraIssuesResponse = new JiraIssuesResponse();
         if (!issues.isEmpty()) {
            jiraIssuesResponse.setTotal(issues.size());
            jiraIssuesResponse.setIssues(issues.subList(0, Math.min(JIRA_MAX_RESULTS_RETURNED, issues.size())));
         } else {
            jiraIssuesResponse.setIssues(Collections.emptyList());
         }
         when(httpClient.callRequestAndParse(any(JiraIssuesResponseReader.class), eq(createGetIssues4BoardUrl))).thenReturn(jiraIssuesResponse);
      }

      private String createGetIssues4BoardUrl(String boardId, String sprintId) {
         return jiraApiConfiguration.getGetIssues4SprintBoardIdUrl()
               .replace(BOARD_ID_PLACE_HOLDER, boardId)
               .replace(SPRINT_ID_PLACE_HOLDER, sprintId)
               .replace(START_AT_PLACE_HOLDER, "0"); // lets start at the begining
      }

      private void mockReadTicket4TicketNr() {
         String url = jiraApiConfiguration.getGetIssueUrl() + ticketNr;
         when(httpClient.callRequestAndParse(any(JiraIssueResponseReader.class), eq(url))).thenReturn(jiraIssueResponse);
      }

   }

}
