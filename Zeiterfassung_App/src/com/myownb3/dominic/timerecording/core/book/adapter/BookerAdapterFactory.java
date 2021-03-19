package com.myownb3.dominic.timerecording.core.book.adapter;


import com.myownb3.dominic.timerecording.core.book.abacus.AbacusBookerAdapter;
import com.myownb3.dominic.timerecording.core.book.coolguys.BookerHelper;

public class BookerAdapterFactory {
   private static BookerAdapter bookerAdapter;

   private BookerAdapterFactory() {
      // private 
   }

   /**
    * @return the {@link BookerAdapter} for doing the actual booking
    */
   public static synchronized BookerAdapter getAdapter() {
      if (bookerAdapter == null) {
         AbacusBookerAdapter abacusBookerAdapter = new AbacusBookerAdapter();
         bookerAdapter = abacusBookerAdapter.isInitialized() ? abacusBookerAdapter : new BookerHelper(); // BookerHelper is the fallback in case abacus is not reachable..
      }
      return bookerAdapter;
   }
}