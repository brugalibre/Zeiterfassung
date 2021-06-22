package com.adcubum.timerecording.jira.data.ticket.factory;

import com.adcubum.timerecording.core.factory.StaticFactory;
import com.adcubum.timerecording.jira.data.ticket.Ticket;

/**
 * The {@link TicketFactory} is used to create and instantiate new {@link Ticket} instances
 * 
 * @author DStalder
 *
 */
public interface TicketFactory {
   /** The singleton instance of a {@link TicketFactory} in order to create {@link Ticket} instances */
   public static final TicketFactory INSTANCE = StaticFactory.createNewObject("ticketfactory", "jira-module-configuration.xml");

   /**
    * Creates a new dummy {@link Ticket} for the given ticket-rn
    * 
    * @param ticketNr
    *        the number of the dummy {@link Ticket}
    * @return a new dummy {@link Ticket} for the given ticket-rn
    */
   Ticket dummy(String ticketNr);
}
