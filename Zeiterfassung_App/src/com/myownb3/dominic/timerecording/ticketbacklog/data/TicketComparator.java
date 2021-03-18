package com.myownb3.dominic.timerecording.ticketbacklog.data;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

import java.util.Comparator;

import com.myownb3.dominic.timerecording.ticketbacklog.data.ticket.TicketAttrs;

public class TicketComparator implements Comparator<Ticket> {

   @Override
   public int compare(Ticket t1, Ticket t2) {
      // Tickets which are NOT within the sprint should appear at the bottom 
      if (!t1.isSprintTicket() && t2.isSprintTicket()) {
         return 1;
      } else if (!t2.isSprintTicket() && t1.isSprintTicket()) {
         return -1;
      }
      // Tickets which are assigned to you, should be appear at the top
      if (!t1.isCurrentUserAssigned() && t2.isCurrentUserAssigned()) {
         return 1;
      } else if (!t2.isCurrentUserAssigned() && t1.isCurrentUserAssigned()) {
         return -1;
      }
      return compareSprintId(t1, t2);
   }

   private int compareSprintId(Ticket t1, Ticket t2) {
      TicketAttrs ticketAttrs1 = t1.getTicketAttrs();
      TicketAttrs ticketAttrs2 = t2.getTicketAttrs();
      int compareValue = 0;
      // Tickets which belongs to the same sprint, should be grouped together 
      if (nonNull(ticketAttrs1.getSprintId()) && nonNull(ticketAttrs2.getSprintId())) {
         compareValue = ticketAttrs1.getSprintId().compareTo(ticketAttrs2.getSprintId());
      } else {
         if (nonNull(ticketAttrs1.getSprintId()) && isNull(ticketAttrs2.getSprintId())) {
            compareValue = -1;
         } else if (isNull(ticketAttrs1.getSprintId()) && nonNull(ticketAttrs2.getSprintId())) {
            compareValue = 1;
         }
      }
      if (compareValue == 0) {
         return t1.getNr().compareTo(t2.getNr());
      }
      return compareValue;
   }
}
