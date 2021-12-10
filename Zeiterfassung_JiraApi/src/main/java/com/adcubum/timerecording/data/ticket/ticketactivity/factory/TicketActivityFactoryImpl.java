package com.adcubum.timerecording.data.ticket.ticketactivity.factory;

import com.adcubum.timerecording.data.ticket.ticketactivity.TicketActivityImpl;
import com.adcubum.timerecording.data.ticket.ticketactivity.factor.TicketActivityFactory;
import com.adcubum.timerecording.jira.data.ticket.TicketActivity;
import com.adcubum.timerecording.jira.data.ticket.factory.TicketFactory;

/**
 * The {@link TicketActivityFactoryImpl} is used to create and instantiate new {@link TicketFactory} instances
 *
 * @author DStalder
 *
 */
public class TicketActivityFactoryImpl implements TicketActivityFactory {

   @Override
   public TicketActivity createNew(String name, int serviceCode) {
      return new TicketActivityImpl(name, serviceCode);
   }

   @Override
   public TicketActivity dummy(String ticketActivityName, int serviceCode) {
      return new TicketActivityImpl(ticketActivityName, serviceCode, true);
   }
}
