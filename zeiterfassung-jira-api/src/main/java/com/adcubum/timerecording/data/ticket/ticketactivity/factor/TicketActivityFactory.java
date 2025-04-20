package com.adcubum.timerecording.data.ticket.ticketactivity.factor;

import com.adcubum.timerecording.core.factory.StaticFactory;
import com.adcubum.timerecording.jira.data.ticket.Ticket;
import com.adcubum.timerecording.jira.data.ticket.TicketActivity;

/**
 * The {@link TicketActivityFactory} is used to create and instantiate new {@link TicketActivity} instances
 *
 * @author DStalder
 *
 */
public interface TicketActivityFactory {
   /** The singleton instance of a {@link TicketActivityFactory} in order to create {@link TicketActivity} instances */
   TicketActivityFactory INSTANCE = StaticFactory.createNewObject("ticketactivityfactory", "jira-module-configuration.xml");

   /**
    * Creates a new  {@link TicketActivity} for the given name and serviceCode
    *
    * @param name
    *        the name of the {@link TicketActivity}
    * @param serviceCode
    *        the unique service code of the {@link TicketActivity}
    * @return a new {@link TicketActivity}
    */
   TicketActivity createNew(String name, int serviceCode);

    /**
     * Creates a dummy {@link TicketActivity} for which is no equivalent in the corresponding ticket or booking system
     * @param ticketActivityName
     *        the name of the {@link TicketActivity}
     * @param serviceCode
     *        the unique service code of the {@link TicketActivity}
     * @return
     */
   TicketActivity dummy(String ticketActivityName, int serviceCode);
}
