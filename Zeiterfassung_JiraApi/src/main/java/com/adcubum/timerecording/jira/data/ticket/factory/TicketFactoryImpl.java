package com.adcubum.timerecording.jira.data.ticket.factory;

import com.adcubum.timerecording.jira.data.ticket.Ticket;
import com.adcubum.timerecording.jira.data.ticket.TicketImpl;
import com.adcubum.timerecording.jira.jiraapi.readresponse.data.JiraIssue;

/**
 * The {@link TicketFactoryImpl} is used to create and instantiate new {@link TicketFactory} instances
 * This class needs to be placed in the impl-project due to dependencies to {@link JiraIssue}
 * 
 * @author DStalder
 *
 */
public class TicketFactoryImpl implements TicketFactory {

   /**
    * Creates a new Instance of a dummy {@link Ticket} for the given ticket-nr
    * 
    * @return a new Instance of a dummy {@link Ticket} for the given ticket-nr
    */
   @Override
   public Ticket dummy(String ticketNr) {
      return TicketImpl.dummy(ticketNr);
   }
}
