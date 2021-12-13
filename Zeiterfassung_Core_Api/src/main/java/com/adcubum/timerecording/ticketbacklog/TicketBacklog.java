package com.adcubum.timerecording.ticketbacklog;

import com.adcubum.timerecording.jira.data.ticket.Ticket;
import com.adcubum.timerecording.jira.data.ticket.TicketActivity;
import com.adcubum.timerecording.jira.jiraapi.configuration.JiraApiConfiguration;
import com.adcubum.timerecording.jira.jiraapi.readresponse.read.JiraApiReader;
import com.adcubum.timerecording.ticketbacklog.callback.TicketBacklogCallbackHandler;
import com.adcubum.timerecording.ticketbacklog.config.TicketConfiguration;

import java.util.List;

/**
 * The {@link TicketBacklog} is responsible for managing and maintaining all the {@link Ticket}s available
 * 
 * @author Dominic
 *
 */
public interface TicketBacklog {

   /**
    * Initialises this {@link TicketBacklog} and calls the given callback handler afterwards
    */
   void initTicketBacklog();

   /**
    * Defines the {@link TicketBacklogCallbackHandler} of this {@link TicketBacklog}
    * @param callbackHandler the callbackhandler to register
    */
   void addTicketBacklogCallbackHandler(TicketBacklogCallbackHandler callbackHandler);

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
    * Returns a found {@link TicketActivity} for the given unique service-code.
    *
    * Note that if no {@link TicketActivity} exists for the given code, than a dummy {@link TicketActivity} is returned
    * So this method always returns an instance of a {@link TicketActivity}
    *
    * @param serviceCode
    *        the unique service code
    * @return a {@link TicketActivity} for the given service-code
    * @see TicketActivity#isDummy()
    */
   TicketActivity getTicketActivity4ServiceCode(int serviceCode);

   /**
    * @return the {@link Ticket}s of this {@link TicketBacklog}
    */
   List<Ticket> getTickets();

   /**
    * @return the {@link JiraApiConfiguration}
    */
   TicketConfiguration getTicketConfiguration();
}
