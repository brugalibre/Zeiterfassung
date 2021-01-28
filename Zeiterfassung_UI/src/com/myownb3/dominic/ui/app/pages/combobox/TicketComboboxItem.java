package com.myownb3.dominic.ui.app.pages.combobox;

import com.myownb3.dominic.timerecording.ticketbacklog.data.Ticket;

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
      return ticket.getTicketRep();
   }

   public String getKey() {
      return ticket.getNr();
   }

   public static TicketComboboxItem of(Ticket ticket) {
      return new TicketComboboxItem(ticket);
   }
}
