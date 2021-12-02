package com.adcubum.timerecording.proles.ticketbacklog.read;

import com.adcubum.timerecording.jira.data.ticket.Ticket;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

class ProlesTicketReaderImplTest {

   @Test
   void readProlesTicketFromPath_TicketsBookable() {

      // Given
      int expectedAmountOfTickets = 3;
      ProlesTicketReader prolesTicketReader = new ProlesTicketReaderImpl();

      // When
      List<Ticket> ticketFromPath = prolesTicketReader.readProlesTicketFromPath("testProlesTickets.json");

      // Then
      assertThat(ticketFromPath.size(), is(expectedAmountOfTickets));
      for (Ticket ticket : ticketFromPath) {
         assertThat(ticket.isBookable(), is(true));
         assertThat(ticket.isDummyTicket(), is(false));
         assertThat(ticket.isCurrentUserAssigned(), is(false));
         assertThat(ticket.isSprintTicket(), is(false));
      }
   }
}