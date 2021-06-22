package com.adcubum.timerecording.jira.data.ticket;

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
    * Returns a string starting with the ticket-nr and following by
    * ('description-of-this-ticket')
    * 
    * @param ticket
    *        the ticket
    * @return a representation
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
    * @return <code>true</code> if this {@link TicketImpl} is a dummy Ticket
    *         since it does not exist in jira or <code>false</code> if not
    */
   boolean isDummyTicket();

   /**
    * @return <code>true</code> if there are all relevant value present or
    *         <code>false</code> if not
    */
   boolean isBookable();

   /**
    * @return the number of the {@link TicketImpl}
    */
   String getNr();

   /**
    * @return the TicketAttrs of the {@link TicketImpl}
    */
   TicketAttrs getTicketAttrs();

}
