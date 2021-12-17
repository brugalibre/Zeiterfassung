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

   /**
    * @return <code>true</code> if a {@link com.adcubum.timerecording.jira.data.ticket.Ticket} needs a description or <code>false</code> if the description can be empty or <code>null</code>
    */
   boolean getIsDescriptionRequired();
}
