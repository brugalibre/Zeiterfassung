package com.adcubum.timerecording.messaging.receive.mapping;

/**
 * The {@link TicketMapper} servers as a mapping between a ticket-system which sends its
 * ticket to a master ticket-system.
 * E.g. between post finance (uses jira-tickets) and nag (uses proles-tickets)
 */
public interface TicketMapper {

   /**
    * Maps the given ticket-Nr into a ticket-nr which is appropriate for the receiving system
    *
    * @param ticketNr the ticket-nr to map
    * @return the mapped ticket-nr
    */
   String mapTicketNr(String ticketNr);

   /**
    * Maps the given ticket-activity-name into a ticket-nr which is appropriate for the receiving system
    *
    * @param ticketActivityName the ticket-activity-name to map
    * @return the mapped ticket-activity-name
    */
   String mapTicketActivityCode(String ticketActivityName);
}
