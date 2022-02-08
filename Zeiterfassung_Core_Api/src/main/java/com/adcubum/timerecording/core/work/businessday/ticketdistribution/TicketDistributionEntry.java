package com.adcubum.timerecording.core.work.businessday.ticketdistribution;

import com.adcubum.timerecording.core.work.businessday.BusinessDay;
import com.adcubum.timerecording.jira.data.ticket.Ticket;

/**
 * A {@link TicketDistributionEntry} describes a number of {@link Ticket} and its frequency within
 * a booked {@link BusinessDay}
 *
 * @author dstalder
 */
public interface TicketDistributionEntry {

   /**
    * @return the number
    */
   String getTicketNr();

   /**
    * @return the amount of worked hours of this {@link TicketDistributionEntry}
    */
   double getAmountOfHours();
}
