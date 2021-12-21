package com.adcubum.timerecording.ticketbacklog.proles;

import com.adcubum.timerecording.data.ticket.ticketactivity.factor.TicketActivityFactory;
import com.adcubum.timerecording.jira.data.ticket.Ticket;
import com.adcubum.timerecording.jira.data.ticket.TicketActivity;
import com.adcubum.timerecording.jira.data.ticket.factory.TicketFactory;
import com.adcubum.timerecording.proles.ticketbacklog.read.ProlesTicketReader;
import com.adcubum.timerecording.proles.ticketbacklog.read.ProlesTicketReaderFactory;
import com.adcubum.timerecording.ticketbacklog.AbstractTicketBacklog;
import com.adcubum.timerecording.ticketbacklog.callback.UpdateStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class ProlesTicketBacklogImpl extends AbstractTicketBacklog {

   private static final Logger LOG = LoggerFactory.getLogger(ProlesTicketBacklogImpl.class);
   private static final String DEFAULT_PROLES_TICKETS_FILE = "defaultProlesTickets.json";
   private List<Ticket> tickets;

   ProlesTicketBacklogImpl() {
      super();
      this.tickets = new ArrayList<>();
   }

   @Override
   public void initTicketBacklog() {
      LOG.info("Initialize ticket backlog...");
      ProlesTicketReader prolesTicketReader = ProlesTicketReaderFactory.createNew();
      this.tickets = prolesTicketReader.readProlesTicketFromPath(DEFAULT_PROLES_TICKETS_FILE);
      LOG.info("Initialized ticket backlog successfully, read {} tickets", tickets.size());
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
   public TicketActivity getTicketActivity4ServiceCode(int serviceCode) {
      return findTicketActivityByFilter(serviceCode);
   }

   private TicketActivity findTicketActivityByFilter(int serviceCode) {
      return tickets.stream()
              .map(Ticket::getTicketActivities)
              .flatMap(List::stream)
              .filter(byServiceCode(serviceCode))
              .findFirst()
              .orElseGet(() -> TicketActivityFactory.INSTANCE.dummy("unknown", serviceCode));
   }

   private static Predicate<TicketActivity> byServiceCode(int serviceCode) {
      return ticketActivity -> ticketActivity.getActivityCode() == serviceCode;
   }

   @Override
   public List<Ticket> getTickets() {
      return List.copyOf(tickets);
   }
}
