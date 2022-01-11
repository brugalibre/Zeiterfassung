package com.adcubum.timerecording.core.book.common;

import com.adcubum.timerecording.core.book.result.BookResultType;
import com.adcubum.timerecording.core.book.result.BookerResult;
import com.adcubum.timerecording.core.work.businessday.BusinessDay;

import static java.util.Objects.requireNonNull;

/**
 * The {@link CommonBookResult} is a basic implementation for the {@link BookerResult}
 * 
 * @author Dominic
 *
 */
public class CommonBookResult implements BookerResult {

   private final BookResultType bookResultType;
   private BusinessDay bookedBusinessDay;
   private String message;
   private boolean hasBooked;

   public CommonBookResult(BusinessDay bookedBusinessDay, BookResultType bookResultType, String message) {
      this.bookedBusinessDay = requireNonNull(bookedBusinessDay);
      this.bookResultType = requireNonNull(bookResultType);
      this.message = requireNonNull(message);
      this.hasBooked = bookResultType != BookResultType.NOT_BOOKED;
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
   public BusinessDay getBookedBusinessDay() {
      return bookedBusinessDay;
   }

   @Override
   public String getMessage() {
      return message;
   }
}
