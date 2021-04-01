package com.adcubum.timerecording.ui.app.pages.combobox;

import static java.util.Objects.nonNull;

import com.adcubum.timerecording.jira.data.Ticket;
import com.adcubum.timerecording.jira.data.ticket.TicketAttrs;

public class TicketComboboxItem {
   private Ticket ticket;

   private TicketComboboxItem(Ticket ticket) {
      this.ticket = ticket;
   }

   public Ticket getTicket() {
      return ticket;
   }

   @Override
   public String toString() {
      String sprintId = getSprintId();
      String sprintSuffix = nonNull(sprintId) ? " (" + sprintId + ")" : "";
      return ticket.getTicketRep() + sprintSuffix;
   }

   private String getSprintId() {
      TicketAttrs ticketAttrs = ticket.getTicketAttrs();
      return ticketAttrs.getSprintName();
   }

   public String getKey() {
      return ticket.getNr();
   }

   public static TicketComboboxItem of(Ticket ticket) {
      return new TicketComboboxItem(ticket);
   }
}
