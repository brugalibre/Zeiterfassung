package com.adcubum.timerecording.jira.data.ticket;

import java.util.List;

/**
 * The {@link Ticket} represents a agile ticket which may be assigned a sprint
 * or a user Additionally it has a lot of attributes (most of them are
 * customized). Those attributes are placed in the {@link TicketAttrs} object
 * 
 * @author Dominic
 *
 */
public interface Ticket {

   /**
    * @return all {@link TicketActivity} of this {@link Ticket}
    */
   List<TicketActivity> getTicketActivities();

   /**
    * Returns a string starting with the ticket-nr and following by
    * ('description-of-this-ticket')
    * 
    * @return a representation of this ticket
    */
   String getTicketRep();

   /**
    * @return <code>true</code> if the current logged in user is the assignee
    *         of this {@link Ticket} or <code>false</code> if not
    * 
    */
   boolean isCurrentUserAssigned();

   /**
    * @return <code>true</code> if the Ticket is part of a sprint or
    *         <code>false</code> if it's a common ticket (like the INTA's)
    */
   boolean isSprintTicket();

   /**
    * @return <code>true</code> if this {@link Ticket} is a dummy Ticket
    *         since it does not exist in jira or <code>false</code> if not
    */
   boolean isDummyTicket();

   /**
    * @return <code>true</code> if there are all relevant value present or
    *         <code>false</code> if not
    */
   boolean isBookable();

   /**
    * @return the number of the {@link Ticket}
    */
   String getNr();

   /**
    * @return the TicketAttrs of the {@link Ticket}
    */
   TicketAttrs getTicketAttrs();

}
