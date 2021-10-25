package com.adcubum.timerecording.core.book.abacus;

import com.adcubum.timerecording.core.book.result.BookResultType;
import com.adcubum.timerecording.core.book.result.BookerResult;
import com.adcubum.timerecording.core.work.businessday.BusinessDay;

/**
 * The {@link AbacusBookerAdapterResult} contains a specific type of result, depending how worse or how good the booking went,
 * as well as a message to describe the result. Note that if you using this {@link BookerResult}, the {@link BookerResult#hasBooked()} is
 * always set to true, even if it was a total disaster. As long as there was an attempt to book, we set the flag to true in order to inform
 * the user about what happened
 * 
 * @author DStalder
 *
 */
public class AbacusBookerAdapterResult implements BookerResult {

   private boolean hasBooked;
   private String message;
   private BookResultType bookResultType;
   private BusinessDay bookedBusinessDay;

   public AbacusBookerAdapterResult(boolean hasBooked, BookResultType bookResultType, String message, BusinessDay bookedBusinessDay) {
      this.hasBooked = hasBooked;
      this.bookResultType = bookResultType;
      this.message = message;
      this.bookedBusinessDay = bookedBusinessDay;
   }

   @Override
   public BusinessDay getBookedBusinessDay() {
      return bookedBusinessDay;
   }

   @Override
   public boolean hasBooked() {
      return hasBooked;
   }

   @Override
   public BookResultType getBookResultType() {
      return bookResultType;
   }

   @Override
   public String getMessage() {
      return message;
   }
}
