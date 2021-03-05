package com.myownb3.dominic.timerecording.core.book.result;

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
}
