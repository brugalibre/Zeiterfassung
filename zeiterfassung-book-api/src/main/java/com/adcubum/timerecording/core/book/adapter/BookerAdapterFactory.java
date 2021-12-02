package com.adcubum.timerecording.core.book.adapter;


import com.adcubum.timerecording.core.book.adapter.type.BookerAdapterFactoryDelegate;
import com.adcubum.timerecording.core.factory.AbstractFactory;

public class BookerAdapterFactory extends AbstractFactory {
   private static final String BEAN_NAME = "bookeradapterfactorydelegate";
   private static final BookerAdapterFactory INSTANCE = new BookerAdapterFactory();

   private static BookerAdapter bookerAdapter;

   private BookerAdapterFactory() {
      super("book-modul-configration.xml");
   }

   /**
    * @return the {@link BookerAdapter} for doing the actual booking
    */
   public static synchronized BookerAdapter getAdapter() {
      if (bookerAdapter == null) {
         BookerAdapterFactoryDelegate bookerAdapterFactoryDelegate = INSTANCE.createNewWithAgruments(BEAN_NAME);
         bookerAdapter = bookerAdapterFactoryDelegate.createBookerAdapter();
      }
      return bookerAdapter;
   }

   /**
    * @return the {@link ServiceCodeAdapter} retrieved by the {@link BookerAdapter}
    */
   public static ServiceCodeAdapter getServiceCodeAdapter() {
      return getAdapter().getServiceCodeAdapter();
   }
}
