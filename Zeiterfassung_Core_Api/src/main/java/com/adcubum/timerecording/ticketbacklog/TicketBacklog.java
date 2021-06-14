package com.adcubum.timerecording.ticketbacklog;

import java.util.List;

import com.adcubum.timerecording.jira.data.Ticket;
import com.adcubum.timerecording.jira.jiraapi.readresponse.read.JiraApiReader;
import com.adcubum.timerecording.ticketbacklog.callback.UiTicketBacklogCallbackHandler;

/**
 * The {@link TicketBacklog} is responsible for managing and maintaining all the {@link Ticket}s available
 * 
 * @author Dominic
 *
 */
public interface TicketBacklog {

   /**
    * Initialises this {@link TicketBacklog} and calls the given callback handler afterwards
    * 
    * @param callbackHandler
    *        the {@link UiTicketBacklogCallbackHandler}
    */
   void initTicketBacklog(UiTicketBacklogCallbackHandler callbackHandler);

   /**
    * Evaluates a a {@link Ticket} for the given ticket-nr. If there is no ticket with the given nr, the {@link JiraApiReader} will be
    * called in order to retrieve it. This {@link Ticket} will then be added to this Backlog if it exists and it's bookable
    * 
    * If there is no Ticket for the given number this method returns a dummy-Ticket with only it's ticket-nr set
    * 
    * @param ticketNr
    *        the given Ticket nr
    * 
    * @return a {@link Ticket}
    */
   Ticket getTicket4Nr(String ticketNr);

   /**
    * @return the {@link Ticket}s of this {@link TicketBacklog}
    */
   List<Ticket> getTickets();
}
