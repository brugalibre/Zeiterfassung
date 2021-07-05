package com.adcubum.timerecording.core.work.businessday.update.callback.impl;

import com.adcubum.timerecording.core.factory.AbstractFactory;
import com.adcubum.timerecording.core.work.businessday.update.callback.BusinessDayChangedCallbackHandler;

public class BusinessDayChangedCallbackHandlerFactory extends AbstractFactory {

   private static final String BEAN_NAME = "bdaychangedcallbackhandler";
   private static final BusinessDayChangedCallbackHandlerFactory INSTANCE = new BusinessDayChangedCallbackHandlerFactory();

   private BusinessDayChangedCallbackHandlerFactory() {
      super("modul-configration.xml");
   }

   /**
    * Creates a new {@link BusinessDayChangedCallbackHandler} instance
    */
   public static BusinessDayChangedCallbackHandler createNew() {
      return INSTANCE.createNewWithAgruments(BEAN_NAME);
   }
}
