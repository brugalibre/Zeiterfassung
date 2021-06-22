package com.adcubum.timerecording.jira.data;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.adcubum.timerecording.jira.data.ticket.TicketImpl;
import com.adcubum.timerecording.jira.jiraapi.readresponse.data.JiraIssue;
import com.adcubum.timerecording.jira.jiraapi.readresponse.data.JiraIssueAssignee;
import com.adcubum.timerecording.jira.jiraapi.readresponse.data.JiraIssueFields;
import com.adcubum.timerecording.jira.jiraapi.readresponse.data.JiraIssueResponse;
import com.adcubum.timerecording.jira.jiraapi.readresponse.data.jiraissuefields.GenericNameIdObject;
import com.adcubum.timerecording.jira.test.BaseTestWithSettings;
import com.adcubum.timerecording.security.login.auth.AuthenticationService;

class TicketImplTest extends BaseTestWithSettings {

   @Test
   void testTicketRepresentation() {

      // Given
      String ticketNr = "dumpi";
      String expectedTicketRep = ticketNr;
      TicketImpl ticket = TicketImpl.dummy(ticketNr);

      // When
      String actualTicketRep = ticket.getTicketRep();

      // Then
      assertThat(actualTicketRep, is(expectedTicketRep));
   }

   @Test
   void testTicketRepresentationWithDescription() {

      // Given
      String ticketNr = "hampidampi";
      String description = "test";
      String expectedTicketRep = ticketNr + " (" + description + ")";
      TicketImpl ticket = new TicketBuilder(ticketNr)
            .withDescription(description)
            .build();

      // When
      String actualTicketRep = ticket.getTicketRep();

      // Then
      assertThat(actualTicketRep, is(expectedTicketRep));
   }

   @Test
   void testTicketToString() {

      // Given
      String ticketNr = "hampidumpi";
      String description = "test";
      String expectedTicketRep = ticketNr + " (" + description + ")";
      String expectedToString = "Ticket-Nr = " + expectedTicketRep + ", projekt-nr = -1 ()";
      TicketImpl ticket = new TicketBuilder(ticketNr)
            .withDescription(description)
            .build();

      // When
      String ticket2String = ticket.toString();

      // Then
      assertThat(ticket2String, is(expectedToString));
   }

   @Test
   void testIsSprintTicket_SortAllMixed() {

      // Given
      String username = "hampi";
      AuthenticationService.INSTANCE.init();
      AuthenticationService.INSTANCE.doUserAuthentication(username, "123".toCharArray());
      TicketImpl sprintTicketAssigned = new TicketBuilder("SYRIUS-ZZZZZ")
            .isSprintTicket(true)
            .withAssignee(username)
            .build();
      TicketImpl sprintTicketNotAssigned = new TicketBuilder("SYRIUS-XZZZZ")
            .isSprintTicket(true)
            .build();
      TicketImpl ticketAssigned1 = new TicketBuilder("SYRIUS-XYZ")
            .withAssignee(username)
            .build();
      TicketImpl ticketNotAssigned2 = new TicketBuilder("AAAAA")
            .build();
      TicketImpl ticketNotAssigned1 = new TicketBuilder("SYRIUS-ACBD")
            .build();
      TicketImpl ticketAssigned2 = new TicketBuilder("ZZZZ")
            .withAssignee(username)
            .build();
      List<TicketImpl> tickets =
            Arrays.asList(ticketNotAssigned2, sprintTicketAssigned, ticketAssigned2, sprintTicketNotAssigned, ticketAssigned1, ticketNotAssigned1);

      // When
      Collections.shuffle(tickets);
      Collections.sort(tickets, new TicketComparator());

      // Then
      assertThat(tickets.get(0), is(sprintTicketAssigned));
      assertThat(tickets.get(1), is(sprintTicketNotAssigned));
      assertThat(tickets.get(2), is(ticketAssigned1));
      assertThat(tickets.get(3), is(ticketAssigned2));
      assertThat(tickets.get(4), is(ticketNotAssigned2));
      assertThat(tickets.get(5), is(ticketNotAssigned1));
   }

   @Test
   void testIsSprintTicket_WithAndWithoutSprintId() {

      // Given
      String sprintId1 = "1";
      String sprintId2 = "2";
      TicketImpl ticketWithSprintId1 = new TicketBuilder("SYRIUS-234")
            .withSprintId(sprintId1)
            .build();
      TicketImpl ticketWithSprintId2 = new TicketBuilder("SYRIUS-123")
            .withSprintId(sprintId1)
            .build();
      TicketImpl ticketWithoutSprintId1 = new TicketBuilder("ZZZZ")
            .build();
      TicketImpl anotherTicket2WithSprintId2 = new TicketBuilder("SYRIUS-ZZ")
            .withSprintId(sprintId2)
            .build();
      TicketImpl anotherTicketWithSprintId2 = new TicketBuilder("SYRIUS-YYY")
            .withSprintId(sprintId2)
            .build();
      List<TicketImpl> tickets =
            Arrays.asList(ticketWithSprintId1, anotherTicket2WithSprintId2, ticketWithoutSprintId1, anotherTicketWithSprintId2, ticketWithSprintId2);

      // When
      Collections.sort(tickets, new TicketComparator());

      // Then
      assertThat(tickets.get(0), is(ticketWithSprintId2));
      assertThat(tickets.get(1), is(ticketWithSprintId1));
      assertThat(tickets.get(2), is(anotherTicketWithSprintId2));
      assertThat(tickets.get(3), is(anotherTicket2WithSprintId2));
      assertThat(tickets.get(4), is(ticketWithoutSprintId1));
   }

   @Test
   void testIsSprintTicket_SortForTheSakeOfSeTestabdeckung() {

      // Given
      String username = "hampi";
      TicketImpl ticketAssigned = new TicketBuilder("SYRIUS-ZZZZ")
            .withAssignee(username)
            .build();
      TicketImpl sprintTicketAssigned = new TicketBuilder("SYRIUS-ZZZZ")
            .isSprintTicket(true)
            .withAssignee(username)
            .build();
      TicketImpl sprintTicketNotAssigned = new TicketBuilder("SYRIUS-XZZZZ")
            .isSprintTicket(true)
            .build();
      List<TicketImpl> tickets =
            Arrays.asList(sprintTicketAssigned, ticketAssigned, sprintTicketNotAssigned);

      // When
      Collections.sort(tickets, new TicketComparator());

      // Then
      assertThat(tickets.get(0), is(sprintTicketAssigned));
      assertThat(tickets.get(1), is(sprintTicketNotAssigned));
      assertThat(tickets.get(2), is(ticketAssigned));
   }

   @Test
   void testIsDummyTicket_IsDummy() {

      // Given
      String ticketNr = "sdf";

      // When
      TicketImpl ticket = new TicketBuilder(ticketNr)
            .isDummyTicket(true)
            .build();

      // Then
      assertThat(ticket.isDummyTicket(), is(true));
   }

   @Test
   void testIsDummyTicket_IsNotDummy() {

      // Given
      String ticketNr = "ABES";

      // When
      TicketImpl ticket = new TicketBuilder(ticketNr)
            .build();

      // Then
      assertThat(ticket.isDummyTicket(), is(false));
   }

   private static final class TicketBuilder {
      private JiraIssueResponse jiraIssueResponse;
      private JiraIssueFields jiraIssueFields;
      private boolean isSprintTicket;
      private boolean isDummyTicket;
      private GenericNameIdObject sprintIdObject;

      private TicketBuilder(String ticketNr) {
         this.jiraIssueFields = new JiraIssueFields();
         this.jiraIssueResponse = new JiraIssueResponse();
         this.sprintIdObject = new GenericNameIdObject();
         jiraIssueResponse.setFields(jiraIssueFields);
         jiraIssueResponse.setKey(ticketNr);
         jiraIssueResponse.setId(ticketNr);
         jiraIssueFields.setSprint(sprintIdObject);
      }

      public TicketBuilder withSprintId(String sprintId) {
         this.sprintIdObject.setId(sprintId);
         return this;
      }

      private TicketBuilder isSprintTicket(boolean isSprintTicket) {
         this.isSprintTicket = isSprintTicket;
         return this;
      }

      private TicketBuilder isDummyTicket(boolean isDummyTicket) {
         this.isDummyTicket = isDummyTicket;
         return this;
      }

      private TicketBuilder withDescription(String description) {
         jiraIssueFields.setSummary(description);
         return this;
      }

      private TicketBuilder withAssignee(String assignee) {
         JiraIssueAssignee jiraIssueAssignee = new JiraIssueAssignee();
         jiraIssueAssignee.setKey(assignee);
         jiraIssueFields.setAssignee(jiraIssueAssignee);
         return this;
      }

      private TicketImpl build() {
         return TicketImpl.of(JiraIssue.of(jiraIssueResponse), isSprintTicket, isDummyTicket);
      }
   }
}
