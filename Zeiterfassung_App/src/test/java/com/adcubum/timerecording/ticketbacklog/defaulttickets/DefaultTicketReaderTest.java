package com.adcubum.timerecording.ticketbacklog.defaulttickets;

import com.adcubum.timerecording.importexport.in.file.FileImporter;
import com.adcubum.timerecording.importexport.in.file.FileImporterFactory;
import com.adcubum.timerecording.jira.data.ticket.Ticket;
import com.adcubum.timerecording.jira.jiraapi.readresponse.read.JiraApiReader;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class DefaultTicketReaderTest {

   @Test
   void testInitDefaultScrumAndMeetingTickets_WithUserDefined() {

      // Given
      int expectedAmountOfTickets = 2;
      JiraApiReader jiraApiReader = mockJiraApiReader(mock(Ticket.class));
      FileImporter fileImporter = FileImporterFactory.createNew();
      DefaultTicketReader defaultTicketReader = new DefaultTicketReader(jiraApiReader, "src\\test\\resources\\testTickets.txt", fileImporter);

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
      FileImporter fileImporter = FileImporterFactory.createNew();
      DefaultTicketReader defaultTicketReader =
            new DefaultTicketReader(jiraApiReader, "blubbediblu/testTickets.txt", fileImporter);

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
