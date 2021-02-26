package com.myownb3.dominic.timerecording.core.charge.adapter;


import com.myownb3.dominic.timerecording.core.charge.abacus.AbacusBookerAdapter;
import com.myownb3.dominic.timerecording.core.charge.coolguys.BookerHelper;

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
