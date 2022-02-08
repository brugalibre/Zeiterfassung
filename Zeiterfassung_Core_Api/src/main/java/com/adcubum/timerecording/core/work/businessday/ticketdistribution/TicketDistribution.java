package com.adcubum.timerecording.core.work.businessday.ticketdistribution;

import com.adcubum.timerecording.core.work.businessday.BusinessDay;
import com.adcubum.timerecording.jira.data.ticket.Ticket;

import java.util.List;

/**
 * A {@link TicketDistribution} describes a number of {@link Ticket} and its frequency within
 * a booked {@link BusinessDay}
 *
 * @author dstalder
 */
public interface TicketDistribution {

   /**
    * Returns the amount of worked hours for the given ticket-nr or zero, if there is none with the given nr
    *
    * @param ticketNr the ticket-nr
    * @return the amount of worked hours for the given ticket-nr or zero, if there is none with the given nr
    */
   double getAmountOfHoursByTicketNr(String ticketNr);

   /**
    * Returns the percentage for the given ticket-nr or zero, if there is none with the given nr
    *
    * @param ticketNr the ticket-nr
    * @return the frequency for the given ticket-nr or zero, if there is none with the given nr
    */
   double getPercentageByTicketNr(String ticketNr);

   /**
    * @return all {@link TicketDistributionEntry}
    */
   List<TicketDistributionEntry> getTicketDistributionsEntries();
}
