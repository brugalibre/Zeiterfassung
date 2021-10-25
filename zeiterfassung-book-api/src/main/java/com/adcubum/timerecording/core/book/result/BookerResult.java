package com.adcubum.timerecording.core.book.result;

import com.adcubum.timerecording.core.work.businessday.BusinessDay;

/**
 * Contains the result of a booking
 * 
 * @author Dominic
 *
 */
public interface BookerResult {

   /**
    * @return <code>true</code> if the booking was fully or partial successfull or <code>false</code> if there was no booking at all
    */
   boolean hasBooked();

   /**
    * @return the {@link BookResultType} according to the booking
    */
   BookResultType getBookResultType();

   /**
    * @return the result message of the booking
    */
   String getMessage();

   /**
    * @return an instance to the booked {@link BusinessDay}
    */
   BusinessDay getBookedBusinessDay();
}
