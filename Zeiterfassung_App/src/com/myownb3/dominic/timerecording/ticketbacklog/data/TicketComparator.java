package com.myownb3.dominic.timerecording.ticketbacklog.data;

import java.util.Comparator;

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
      return t1.getNr().compareTo(t2.getNr());
   }
}
