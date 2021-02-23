package com.myownb3.dominic.timerecording.ticketbacklog.data;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.myownb3.dominic.timerecording.settings.common.Const;
import com.myownb3.dominic.timerecording.test.BaseTestWithSettings;
import com.myownb3.dominic.timerecording.ticketbacklog.jiraapi.readresponse.data.JiraIssue;
import com.myownb3.dominic.timerecording.ticketbacklog.jiraapi.readresponse.data.JiraIssueAssignee;
import com.myownb3.dominic.timerecording.ticketbacklog.jiraapi.readresponse.data.JiraIssueFields;
import com.myownb3.dominic.timerecording.ticketbacklog.jiraapi.readresponse.data.JiraIssueResponse;

class TicketTest extends BaseTestWithSettings {

   @Test
   void testIsSprintTicket_SortAllMixed() {

      // Given
      String username = "hampi";
      saveProperty2Settings(Const.USER_NAME_VALUE_KEY, username);
      Ticket sprintTicketAssigned = new TicketBuilder("SYRIUS-ZZZZZ")
            .isSprintTicket(true)
            .withAssignee(username)
            .build();
      Ticket sprintTicketNotAssigned = new TicketBuilder("SYRIUS-XZZZZ")
            .isSprintTicket(true)
            .build();
      Ticket ticketAssigned1 = new TicketBuilder("SYRIUS-XYZ")
            .withAssignee(username)
            .build();
      Ticket ticketNotAssigned2 = new TicketBuilder("AAAAA")
            .build();
      Ticket ticketNotAssigned1 = new TicketBuilder("SYRIUS-ACBD")
            .build();
      Ticket ticketAssigned2 = new TicketBuilder("ZZZZ")
            .withAssignee(username)
            .build();
      List<Ticket> tickets =
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
   void testIsSprintTicket_SortForTheSakeOfSeTestabdeckung() {

      // Given
      String username = "hampi";
      saveProperty2Settings(Const.USER_NAME_VALUE_KEY, username);
      Ticket ticketAssigned = new TicketBuilder("SYRIUS-ZZZZ")
            .withAssignee(username)
            .build();
      Ticket sprintTicketAssigned = new TicketBuilder("SYRIUS-ZZZZ")
            .isSprintTicket(true)
            .withAssignee(username)
            .build();
      Ticket sprintTicketNotAssigned = new TicketBuilder("SYRIUS-XZZZZ")
            .isSprintTicket(true)
            .build();
      List<Ticket> tickets =
            Arrays.asList(sprintTicketAssigned, ticketAssigned, sprintTicketNotAssigned);

      // When
      Collections.sort(tickets, new TicketComparator());

      // Then
      assertThat(tickets.get(0), is(sprintTicketAssigned));
      assertThat(tickets.get(1), is(sprintTicketNotAssigned));
      assertThat(tickets.get(2), is(ticketAssigned));
   }


   private static final class TicketBuilder {
      private JiraIssueResponse jiraIssueResponse;
      private JiraIssueFields jiraIssueFields;
      private boolean isSprintTicket;

      private TicketBuilder(String ticketNr) {
         this.jiraIssueFields = new JiraIssueFields();
         this.jiraIssueResponse = new JiraIssueResponse();
         jiraIssueResponse.setFields(jiraIssueFields);
         jiraIssueResponse.setKey(ticketNr);
      }

      private TicketBuilder isSprintTicket(boolean isSprintTicket) {
         this.isSprintTicket = isSprintTicket;
         return this;
      }

      private TicketBuilder withAssignee(String assignee) {
         JiraIssueAssignee jiraIssueAssignee = new JiraIssueAssignee();
         jiraIssueAssignee.setKey(assignee);
         jiraIssueFields.setAssignee(jiraIssueAssignee);
         return this;
      }

      private Ticket build() {
         return Ticket.of(JiraIssue.of(jiraIssueResponse), isSprintTicket);
      }
   }
}
