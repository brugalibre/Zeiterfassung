package com.myownb3.dominic.timerecording.core.charge.abacus;

import com.myownb3.dominic.timerecording.core.charge.result.BookerResult;
import com.myownb3.dominic.timerecording.core.charge.result.BookResultType;

public class AbacusBookerAdapterResult implements BookerResult {

   private boolean hasBooked;
   private String message;
   private BookResultType bookResultType;

   public AbacusBookerAdapterResult(boolean hasBooked, BookResultType bookResultType, String message) {
      this.hasBooked = hasBooked;
      this.bookResultType = bookResultType;
      this.message = message;
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
