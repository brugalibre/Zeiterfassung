package com.adcubum.timerecording.message;

import com.adcubum.timerecording.core.factory.AbstractFactory;

/**
 * Factory in order to create {@link BookBusinessDayMessageApiService}s
 * 
 * @author Dominic
 *
 */
public class BookBusinessDayMessageApiServiceFactory extends AbstractFactory {
   private static final String BEAN_NAME = "book-businessday-message-api-service";
   private static final BookBusinessDayMessageApiServiceFactory INSTANCE = new BookBusinessDayMessageApiServiceFactory();

   private BookBusinessDayMessageApiServiceFactory() {
      super("modul-configration.xml");
   }

   /**
    * Creates a new / empty {@link BookBusinessDayMessageApiService} instance
    */
   public static BookBusinessDayMessageApiService createNew() {
      return INSTANCE.createNewWithAgruments(BEAN_NAME);
   }
}
