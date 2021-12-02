package com.adcubum.timerecording.ticketbacklog.proles;

import com.adcubum.timerecording.jira.data.ticket.Ticket;
import com.adcubum.timerecording.jira.data.ticket.factory.TicketFactory;
import com.adcubum.timerecording.proles.ticketbacklog.read.ProlesTicketReader;
import com.adcubum.timerecording.proles.ticketbacklog.read.ProlesTicketReaderFactory;
import com.adcubum.timerecording.ticketbacklog.AbstractTicketBacklog;
import com.adcubum.timerecording.ticketbacklog.TicketBacklog;
import com.adcubum.timerecording.ticketbacklog.callback.TicketBacklogCallbackHandler;
import com.adcubum.timerecording.ticketbacklog.callback.UpdateStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

public class ProlesTicketBacklogImpl extends AbstractTicketBacklog {

   private static final String DEFAULT_PROLES_TICKETS_FILE = "defaultProlesTickets.json";
   private List<Ticket> tickets;

   ProlesTicketBacklogImpl() {
      super();
      this.tickets = new ArrayList<>();
   }

   @Override
   public void initTicketBacklog() {
      ProlesTicketReader prolesTicketReader = ProlesTicketReaderFactory.createNew();
      this.tickets = prolesTicketReader.readProlesTicketFromPath(DEFAULT_PROLES_TICKETS_FILE);
      notifyCallbackHandlers(UpdateStatus.SUCCESS);
   }

   @Override
   public Ticket getTicket4Nr(String ticketNr) {
      return tickets.stream()
            .filter(existingTicket -> existingTicket.getNr().equals(ticketNr))
            .findFirst()
            .orElseGet(() -> TicketFactory.INSTANCE.dummy(ticketNr));
   }

   @Override
   public List<Ticket> getTickets() {
      return List.copyOf(tickets);
   }
}
