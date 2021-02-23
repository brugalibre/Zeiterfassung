package com.myownb3.dominic.timerecording.ticketbacklog.defaulttickets;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import com.myownb3.dominic.timerecording.ticketbacklog.data.Ticket;
import com.myownb3.dominic.timerecording.ticketbacklog.jiraapi.readresponse.read.JiraApiReader;

class DefaultTicketReaderTest {

   @Test
   void testInitDefaultScrumAndMeetingTickets_WithUserDefined() {

      // Given
      int expectedAmountOfTickets = 2 + DefaultTicketConst.getDefaultScrumtTicketNrs().size();
      JiraApiReader jiraApiReader = mockJiraApiReader(mock(Ticket.class));
      DefaultTicketReader defaultTicketReader = new DefaultTicketReader(jiraApiReader, "testRes/testTickets.txt");

      // When
      List<Ticket> actualTickets = defaultTicketReader.readDefaultTickets();

      // Then
      assertThat(actualTickets.size(), is(expectedAmountOfTickets));
   }

   @Test
   void testInitDefaultScrumAndMeetingTickets_WithoutUserDefined() {

      // Given
      int expectedAmountOfTickets = DefaultTicketConst.getDefaultScrumtTicketNrs().size();
      JiraApiReader jiraApiReader = mockJiraApiReader(mock(Ticket.class));
      DefaultTicketReader defaultTicketReader = new DefaultTicketReader(jiraApiReader, "blubbediblu/testTickets.txt");

      // When
      List<Ticket> actualTickets = defaultTicketReader.readDefaultTickets();

      // Then
      assertThat(actualTickets.size(), is(expectedAmountOfTickets));
   }

   @Test
   void testInitDefaultScrumAndMeetingTickets_NonDefaultTicketCouldBeRead() {

      // Given
      int expectedAmountOfTickets = 0;
      JiraApiReader jiraApiReader = mockJiraApiReader(null);
      DefaultTicketReader defaultTicketReader = new DefaultTicketReader(jiraApiReader, "blubbediblu/testTickets.txt");

      // When
      List<Ticket> actualTickets = defaultTicketReader.readDefaultTickets();

      // Then
      assertThat(actualTickets.size(), is(expectedAmountOfTickets));
   }

   private JiraApiReader mockJiraApiReader(Ticket ticket) {
      JiraApiReader jiraApiReader = mock(JiraApiReader.class);
      when(jiraApiReader.readTicket4Nr(any())).thenReturn(Optional.ofNullable(ticket));
      return jiraApiReader;
   }

}
