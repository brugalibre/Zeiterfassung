package com.adcubum.timerecording.core.book.adapter;

import com.adcubum.timerecording.core.book.result.BookerResult;
import com.adcubum.timerecording.core.work.businessday.BusinessDay;
import com.adcubum.timerecording.core.work.businessday.BusinessDayIncrement;
import com.adcubum.timerecording.jira.data.ticket.Ticket;

/**
 * The {@link BookerAdapter} wraps the actual implementation for booking. Depending on if the abacus interface is reachable or if
 * the fallback to a Selenium-driver controlled Browser is used as fallback
 *
 * @author Dominic
 *
 */
public interface BookerAdapter {

   /**
    * Initializes this {@link BookerAdapter} if necessary
    */
   void init();

   /**
    * @return the {@link ServiceCodeAdapter}
    */
   public ServiceCodeAdapter getServiceCodeAdapter();

   /**
    * Collects from each {@link BusinessDayIncrement} the content to book and calls the implementation depending api to book
    * Additionally all booked {@link BusinessDayIncrement} are flagged as charged
    *
    * @param businessDay
    *        the {@link BusinessDay} to book
    * @return a {@link BookerResult} which contains detailed results
    */
   public BookerResult book(BusinessDay businessDay);

   /**
    * Returns <code>true</code> if this certain {@link Ticket} can be booked by this {@link BookerAdapter} or <code>false</code> if not
    * A Ticket may be not bookable if there are some required attributes missing
    *
    * @param ticket the Ticket to verify
    * @return <code>true</code> if this certain {@link Ticket} can be booked by this {@link BookerAdapter} or <code>false</code> if not
    */
   boolean isTicketBookable(Ticket ticket);
}