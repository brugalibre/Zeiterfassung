package com.myownb3.dominic.timerecording.ticketbacklog.jiraapi.readresponse.read;

import static com.myownb3.dominic.timerecording.ticketbacklog.jiraapi.constant.JiraApiConstants.BOARD_ID_PLACE_HOLDER;
import static com.myownb3.dominic.timerecording.ticketbacklog.jiraapi.constant.JiraApiConstants.GET_ACTIVE_SPRINT_ID_FOR_BOARD_URL;
import static com.myownb3.dominic.timerecording.ticketbacklog.jiraapi.constant.JiraApiConstants.GET_ALL_BOARDS_URL;
import static com.myownb3.dominic.timerecording.ticketbacklog.jiraapi.constant.JiraApiConstants.GET_FUTURE_SPRINT_IDS_FOR_BOARD_URL;
import static com.myownb3.dominic.timerecording.ticketbacklog.jiraapi.constant.JiraApiConstants.GET_ISSUES_4_BOARD_URL;
import static com.myownb3.dominic.timerecording.ticketbacklog.jiraapi.constant.JiraApiConstants.JIRA_MAX_RESULTS_RETURNED;
import static com.myownb3.dominic.timerecording.ticketbacklog.jiraapi.constant.JiraApiConstants.SPRINT_ID_PLACE_HOLDER;
import static com.myownb3.dominic.timerecording.ticketbacklog.jiraapi.constant.JiraApiConstants.START_AT_PLACE_HOLDER;
import static com.myownb3.dominic.timerecording.ticketbacklog.jiraapi.constant.JiraApiConstants.START_AT_PLACE_LITERAL;
import static java.util.Objects.nonNull;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import com.adcubum.timerecording.security.login.auth.AuthenticationContext;
import com.myownb3.dominic.timerecording.ticketbacklog.data.Ticket;
import com.myownb3.dominic.timerecording.ticketbacklog.data.ticket.IssueType;
import com.myownb3.dominic.timerecording.ticketbacklog.data.ticket.TicketAttrs;
import com.myownb3.dominic.timerecording.ticketbacklog.jiraapi.constant.JiraApiConstants;
import com.myownb3.dominic.timerecording.ticketbacklog.jiraapi.mapresponse.JiraApiReadTicketsResult;
import com.myownb3.dominic.timerecording.ticketbacklog.jiraapi.readresponse.data.JiraGenericValuesResponse;
import com.myownb3.dominic.timerecording.ticketbacklog.jiraapi.readresponse.data.JiraGenericValuesResponse.GenericNameAttrs;
import com.myownb3.dominic.timerecording.ticketbacklog.jiraapi.readresponse.data.JiraIssue;
import com.myownb3.dominic.timerecording.ticketbacklog.jiraapi.readresponse.data.JiraIssueFields;
import com.myownb3.dominic.timerecording.ticketbacklog.jiraapi.readresponse.data.JiraIssueFields.JiraIssueType;
import com.myownb3.dominic.timerecording.ticketbacklog.jiraapi.readresponse.data.JiraIssueGenericValueObject;
import com.myownb3.dominic.timerecording.ticketbacklog.jiraapi.readresponse.data.JiraIssueResponse;
import com.myownb3.dominic.timerecording.ticketbacklog.jiraapi.readresponse.data.JiraIssueThemaInfos;
import com.myownb3.dominic.timerecording.ticketbacklog.jiraapi.readresponse.data.JiraIssuesResponse;
import com.myownb3.dominic.timerecording.ticketbacklog.jiraapi.readresponse.read.JiraApiReader.SprintInfo;
import com.myownb3.dominic.timerecording.ticketbacklog.jiraapi.readresponse.response.responsereader.JiraGenericValuesResponseReader;
import com.myownb3.dominic.timerecording.ticketbacklog.jiraapi.readresponse.response.responsereader.JiraIssueResponseReader;
import com.myownb3.dominic.timerecording.ticketbacklog.jiraapi.readresponse.response.responsereader.JiraIssuesResponseReader;

class JiraApiReaderTest {

   @Test
   void testUserAuthenticated() {
      // Given
      String pwd = "";
      String username = "";
      AuthenticationContext atuhenticationContext = new AuthenticationContext(username, () -> pwd.toCharArray());
      HttpClient httpClient = mock(HttpClient.class);
      JiraApiReader jiraApiReader = new JiraApiReader(httpClient);

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

      JiraApiReader jiraApiReader = new TestCaseBuilder()
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

      JiraApiReader jiraApiReader = new TestCaseBuilder()
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
      JiraApiReader jiraApiReader = new TestCaseBuilder()
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

      JiraApiReader jiraApiReader = new TestCaseBuilder()
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
      JiraIssue jiraIssue = new JiraIssue();
      jiraIssue.setId("1");
      JiraIssue futureJiraIssue = new JiraIssue();
      futureJiraIssue.setId("2");
      JiraApiReader jiraApiReader = new TestCaseBuilder()
            .withBoardName(boardName)
            .withFutureSprintId("2")
            .withReceivedSprintIssues(Collections.singletonList(jiraIssue))
            .withReceivedFutureSprintIssues(Collections.singletonList(futureJiraIssue))
            .build();

      // When
      JiraApiReadTicketsResult actualJiraApiReadTicketsResult = jiraApiReader.readTicketsFromBoardAndSprints(boardName, Collections.emptyList());

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
      JiraApiReader jiraApiReader = new TestCaseBuilder()
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

      private HttpClient httpClient;
      private String ticketNr;
      private JiraIssueResponse jiraIssueResponse;
      private JiraGenericValuesResponse jiraGetBoardsResponse;
      private JiraGenericValuesResponse jiraGetSprintResponse;
      private JiraGenericValuesResponse jiraGetFuturSprintResponse;
      private List<JiraIssue> issues;
      private String boardName;
      private List<JiraIssue> futureIssues;
      private String futurSprintId;

      private TestCaseBuilder() {
         this.httpClient = mock(HttpClient.class);
         this.jiraIssueResponse = new JiraIssueResponse();
         this.jiraGetBoardsResponse = new JiraGenericValuesResponse();
         this.jiraGetSprintResponse = new JiraGenericValuesResponse();
         this.jiraGetFuturSprintResponse = new JiraGenericValuesResponse();
         this.issues = new ArrayList<>();
         this.futureIssues = new ArrayList<>();
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

      public TestCaseBuilder withFutureSprintId(String futurSprintId) {
         this.futurSprintId = futurSprintId;
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

      private JiraApiReader build() {
         String boardId = issues.isEmpty() ? SprintInfo.UNKNOWN : "boardId25";
         String sprintId = "1";
         mockReadTicket4TicketNr();
         mockReadTicketsFromBoardName(boardId, sprintId, futurSprintId);
         return new JiraApiReader(httpClient);
      }

      private void mockReadTicketsFromBoardName(String boardId, String sprintId, String futurSprintId) {
         prepareReadResults(boardId, sprintId, issues, jiraGetSprintResponse);
         if (nonNull(futurSprintId)) {
            prepareReadResults(boardId, futurSprintId, futureIssues, jiraGetFuturSprintResponse);
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
            String getAllBoardsUrl = GET_ALL_BOARDS_URL.replace(START_AT_PLACE_HOLDER, String.valueOf(index));
            when(httpClient.callRequestAndParse(any(JiraGenericValuesResponseReader.class), eq(getAllBoardsUrl))).thenReturn(jiraGetBoardsResponse);
            index = index + JiraApiConstants.JIRA_MAX_RESULTS_RETURNED;

         } while (index < JiraApiConstants.MAX_RESULTS);
      }

      private void mockReadSprint(String boardId) {
         String getSprintIdUrl = GET_ACTIVE_SPRINT_ID_FOR_BOARD_URL.replace(BOARD_ID_PLACE_HOLDER, boardId);
         when(httpClient.callRequestAndParse(any(JiraGenericValuesResponseReader.class), eq(getSprintIdUrl))).thenReturn(jiraGetSprintResponse);
      }

      private void mockReadFutureSprint(String boardId) {
         String getFuturSprintIdUrl = GET_FUTURE_SPRINT_IDS_FOR_BOARD_URL.replace(BOARD_ID_PLACE_HOLDER, boardId);
         when(httpClient.callRequestAndParse(any(JiraGenericValuesResponseReader.class), eq(getFuturSprintIdUrl)))
               .thenReturn(jiraGetFuturSprintResponse);
      }

      private void prepareReadResults(String boardId, String sprintId, List<JiraIssue> sprintIssues,
            JiraGenericValuesResponse jiraGetSprintResponse) {
         // If we except to receive any tickets from the sprint, we have to set the 'values' in the sprint result -> we found a sprint -> go ahead for the tickets
         if (!sprintIssues.isEmpty()) {
            GenericNameAttrs genericBoardNameAttrs = new GenericNameAttrs();
            genericBoardNameAttrs.setId(boardId);
            genericBoardNameAttrs.setName(boardName);
            jiraGetBoardsResponse.setValues(Collections.singletonList(genericBoardNameAttrs));

            GenericNameAttrs genericSprintNameAttrs = new GenericNameAttrs();
            genericSprintNameAttrs.setId(sprintId);
            jiraGetSprintResponse.setValues(Collections.singletonList(genericSprintNameAttrs));
         }
      }

      private void mockReadSprintTicketsFromMaxResults(String boardId, String sprintId) {
         if (issues.size() >= JIRA_MAX_RESULTS_RETURNED) {
            String url = createGetIssues4BoardUrl(boardId, sprintId)
                  .replace(START_AT_PLACE_LITERAL + "=0", START_AT_PLACE_LITERAL + "=" + JIRA_MAX_RESULTS_RETURNED);// Das isch wie nöd eso schöö...
            JiraIssuesResponse jiraIssuesResponse = new JiraIssuesResponse();
            jiraIssuesResponse.setIssues(issues.subList(JIRA_MAX_RESULTS_RETURNED, issues.size()));
            when(httpClient.callRequestAndParse(any(JiraIssuesResponseReader.class), eq(url))).thenReturn(jiraIssuesResponse);
         }
      }

      private void mockReadSprintTicketsUntilMaxResults(String boardId, String sprintId, List<JiraIssue> issues) {
         String createGetIssues4BoardUrl = createGetIssues4BoardUrl(boardId, sprintId);
         JiraIssuesResponse jiraIssuesResponse = new JiraIssuesResponse();
         if (!issues.isEmpty()) {
            jiraIssuesResponse.setIssues(issues.subList(0, Math.min(JIRA_MAX_RESULTS_RETURNED, issues.size())));
         } else {
            jiraIssuesResponse.setIssues(Collections.emptyList());
         }
         when(httpClient.callRequestAndParse(any(JiraIssuesResponseReader.class), eq(createGetIssues4BoardUrl))).thenReturn(jiraIssuesResponse);
      }

      private static String createGetIssues4BoardUrl(String boardId, String sprintId) {
         return GET_ISSUES_4_BOARD_URL
               .replace(BOARD_ID_PLACE_HOLDER, boardId)
               .replace(SPRINT_ID_PLACE_HOLDER, sprintId)
               .replace(START_AT_PLACE_HOLDER, "0"); // lets start at the begining
      }

      private void mockReadTicket4TicketNr() {
         String url = JiraApiConstants.GET_ISSUE_URL + ticketNr;
         when(httpClient.callRequestAndParse(any(JiraIssueResponseReader.class), eq(url))).thenReturn(jiraIssueResponse);
      }

   }

}
