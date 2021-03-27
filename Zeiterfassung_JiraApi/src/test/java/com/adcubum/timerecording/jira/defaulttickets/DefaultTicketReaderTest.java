package com.adcubum.timerecording.jira.defaulttickets;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import com.adcubum.timerecording.importexport.in.file.FileImporterImpl;
import com.adcubum.timerecording.jira.data.Ticket;
import com.adcubum.timerecording.jira.jiraapi.readresponse.read.JiraApiReader;

class DefaultTicketReaderTest {

   @Test
   void testInitDefaultScrumAndMeetingTickets_WithUserDefined() {

      // Given
      int expectedAmountOfTickets = 2 + DefaultTicketConst.getDefaultScrumtTicketNrs().size();
      JiraApiReader jiraApiReader = mockJiraApiReader(mock(Ticket.class));
      DefaultTicketReader defaultTicketReader =
            new DefaultTicketReader(jiraApiReader, "src\\test\\resources\\testTickets.txt", FileImporterImpl.INTANCE::importFile);

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
      DefaultTicketReader defaultTicketReader =
            new DefaultTicketReader(jiraApiReader, "blubbediblu/testTickets.txt", FileImporterImpl.INTANCE::importFile);

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
      DefaultTicketReader defaultTicketReader =
            new DefaultTicketReader(jiraApiReader, "blubbediblu/testTickets.txt", FileImporterImpl.INTANCE::importFile);

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
