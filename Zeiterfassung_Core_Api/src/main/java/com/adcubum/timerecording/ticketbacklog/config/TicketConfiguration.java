package com.adcubum.timerecording.ticketbacklog.config;

/**
 * The {@link TicketConfiguration} contains different constants and customizable values about the {@link com.adcubum.timerecording.jira.data.ticket.Ticket}s
 */
public interface TicketConfiguration {

   /**
    * @return the regex for a ticket-name
    */
   String getTicketNamePattern();

   /**
    * @return the default ticket name
    */
   String getDefaultTicketName();

   /**
    * @return the regex for multiple ticket-names
    */
   String getMultiTicketNamePattern();
}
