package com.adcubum.timerecording.ticketbacklog.factory;

import com.adcubum.timerecording.ticketbacklog.TicketBacklog;

/**
 * Delegate for the {@link TicketBacklogFactory} in order to determine the specific type of a
 * {@link TicketBacklog}
 *
 * @author dstalder
 */
public interface TicketBacklogFactoryDelegate {

   /**
    * @return a new created or already existing instance of a {@link TicketBacklog} depending on the configuration
    */
   TicketBacklog createTicketBacklog();
}
