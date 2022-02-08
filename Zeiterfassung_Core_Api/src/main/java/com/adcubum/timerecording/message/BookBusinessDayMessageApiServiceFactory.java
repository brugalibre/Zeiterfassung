package com.adcubum.timerecording.message;

import com.adcubum.timerecording.core.factory.AbstractFactory;

/**
 * Factory in order to create {@link BookBusinessDayMessageApiAdapterService}s
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
    * Creates a new / empty {@link BookBusinessDayMessageApiAdapterService} instance
    */
   public static BookBusinessDayMessageApiAdapterService createNew() {
      return INSTANCE.createNewWithAgruments(BEAN_NAME);
   }
}
