package com.adcubum.timerecording.app.businessday.businessdayconfig.factory;

import com.adcubum.timerecording.app.businessday.businessdayconfig.BusinessDayConfig;
import com.adcubum.timerecording.core.factory.AbstractFactory;

/**
 * Factory used to create and instantiate new {@link BusinessDayConfig} instances
 * 
 * @author DStalder
 *
 */
public class BusinessDayConfigFactory extends AbstractFactory {
   private static final String BEAN_NAME = "businessdayconfig";
   private static final BusinessDayConfigFactory INSTANCE = new BusinessDayConfigFactory();

   private BusinessDayConfigFactory() {
      super("modul-configration.xml");
   }

   /**
    * Creates a new Instance of the {@link BusinessDayConfig} or returns an already created instance
    * 
    * @return a new Instance of the {@link BusinessDayConfig} or returns an already created instance
    */
   public static BusinessDayConfig createNew() {
      return INSTANCE.createNewWithAgruments(BEAN_NAME);
   }
}
