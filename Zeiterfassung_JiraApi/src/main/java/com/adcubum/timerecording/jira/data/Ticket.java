package com.adcubum.timerecording.jira.data;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

import com.adcubum.timerecording.jira.data.ticket.TicketAttrs;
import com.adcubum.timerecording.jira.jiraapi.readresponse.data.JiraIssue;
import com.adcubum.timerecording.security.login.auth.AuthenticationService;

public class Ticket {

   private TicketAttrs ticketAttrs;
   private boolean isCurrentUserAssigned;
   private boolean isSprintTicket;
   private boolean isDummyTicket;

   private Ticket(TicketAttrs ticketAttrs, boolean isSprintTicket, boolean isDummyTicket) {
      this.ticketAttrs = ticketAttrs;
      this.isCurrentUserAssigned = isCurrentUserAssigned(ticketAttrs.getAssignee());
      this.isSprintTicket = isSprintTicket;
      this.isDummyTicket = isDummyTicket;
   }

   /**
    * Creates an empty Ticket. Only the Ticket-Nr is set
    * 
    * @param ticketNr
    *        the ticket-nr
    * @return an empty Ticket. Only the Ticket-Nr is set
    */
   public static Ticket dummy(String ticketNr) {
      JiraIssue jiraIssue = new JiraIssue();
      jiraIssue.setKey(ticketNr);
      return Ticket.of(jiraIssue, false, true);
   }

   public static Ticket of(JiraIssue issue, boolean isSprintTicket, boolean isDummyTicket) {
      return new Ticket(TicketAttrs.of(issue), isSprintTicket, isDummyTicket);
   }

   public static Ticket of(JiraIssue issue) {
      return new Ticket(TicketAttrs.of(issue), true, false);
   }

   private boolean isCurrentUserAssigned(String assignee) {
      String currentUserName = AuthenticationService.INSTANCE.getUsername();
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
    * @return <code>true</code> if there are all relevant value present or <code>false</code> if not
    */
   public boolean isBookable() {
      return ticketAttrs.isBookable();
   }

   /**
    * @return <code>true</code> if this {@link Ticket} is a dummy Ticket since it does not exist in jira or <code>false</code> if not
    */
   public boolean isDummyTicket() {
      return isDummyTicket;
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
      String title = this.ticketAttrs.getTitle();
      if (isNull(title)) {
         return this.getNr();
      }
      return this.getNr() + " (" + title + ")";
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
