package com.myownb3.dominic.timerecording.ticketbacklog.data;

import static com.myownb3.dominic.timerecording.settings.common.Const.USER_NAME_VALUE_KEY;
import static java.util.Objects.nonNull;

import com.myownb3.dominic.timerecording.settings.Settings;
import com.myownb3.dominic.timerecording.ticketbacklog.data.ticket.TicketAttrs;
import com.myownb3.dominic.timerecording.ticketbacklog.jiraapi.readresponse.data.JiraIssue;

public class Ticket {

   private TicketAttrs ticketAttrs;
   private boolean isCurrentUserAssigned;
   private boolean isSprintTicket;

   private Ticket(TicketAttrs ticketAttrs, boolean isSprintTicket) {
      this.ticketAttrs = ticketAttrs;
      this.isCurrentUserAssigned = isCurrentUserAssigned(ticketAttrs.getAssignee());
      this.isSprintTicket = isSprintTicket;
   }

   public static Ticket of(JiraIssue issue, boolean isSprintTicket) {
      return new Ticket(TicketAttrs.of(issue), isSprintTicket);
   }

   public static Ticket of(JiraIssue issue) {
      return new Ticket(TicketAttrs.of(issue), true);
   }

   private boolean isCurrentUserAssigned(String assignee) {
      String currentUserName = Settings.INSTANCE.getSettingsValue(USER_NAME_VALUE_KEY);
      return nonNull(currentUserName) && currentUserName.equalsIgnoreCase(assignee);
   }

   /**
    * @return the TicketAttrs of the {@link Ticket}
    */
   public TicketAttrs getTicketAttrs() {
      return ticketAttrs;
   }

   /**
    * @return the number of the {@link Ticket}
    */
   public String getNr() {
      return ticketAttrs.getNr();
   }

   /**
    * @return <code>true</code> if the Ticket is part of a sprint or <code>false</code> if it's a common ticket (like the INTA's)
    */
   public boolean isSprintTicket() {
      return isSprintTicket;
   }

   public boolean isCurrentUserAssigned() {
      return isCurrentUserAssigned;
   }

   /**
    * Returns a string starting with the ticket-nr and following by ('description-of-this-ticket')
    * 
    * @param ticket
    *        the ticket
    * @return a representation
    */
   public String getTicketRep() {
      return this.getNr() + " (" + this.ticketAttrs.getTitle() + ")";
   }

   @Override
   public String toString() {
      return "Ticket-Nr = " + ticketAttrs.getNr() + " (" + ticketAttrs.getTitle() + "), projekt-nr = " + ticketAttrs.getProjectNr() + " ("
            + ticketAttrs.getProjectDesc() + ")";
   }

   @Override
   public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + ((ticketAttrs == null) ? 0 : ticketAttrs.hashCode());
      return result;
   }

   @Override
   public boolean equals(Object obj) {
      if (this == obj)
         return true;
      if (obj == null)
         return false;
      if (getClass() != obj.getClass())
         return false;
      Ticket other = (Ticket) obj;
      if (ticketAttrs == null) {
         if (other.ticketAttrs != null)
            return false;
      } else if (!ticketAttrs.equals(other.ticketAttrs))
         return false;
      return true;
   }
}
