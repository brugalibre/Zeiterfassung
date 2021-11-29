package com.adcubum.timerecording.ticketbacklog.proles;

import java.util.List;
import java.util.Set;

import com.adcubum.timerecording.importexport.in.file.FileImporter;
import com.adcubum.timerecording.jira.data.ticket.Ticket;
import com.adcubum.timerecording.jira.data.ticket.factory.TicketFactory;
import com.adcubum.timerecording.ticketbacklog.TicketBacklog;
import com.adcubum.timerecording.ticketbacklog.TicketBacklogHelper;
import com.adcubum.timerecording.ticketbacklog.callback.UiTicketBacklogCallbackHandler;

public class ProlesTicketBacklogImpl implements TicketBacklog {

   private TicketBacklogHelper backlogHelper;
   private Set<Ticket> tickets;
   private FileImporter fileImporter;

   ProlesTicketBacklogImpl() {}

   @Override
   public void initTicketBacklog(UiTicketBacklogCallbackHandler callbackHandler) {
      // TODO Auto-generated method stub

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
