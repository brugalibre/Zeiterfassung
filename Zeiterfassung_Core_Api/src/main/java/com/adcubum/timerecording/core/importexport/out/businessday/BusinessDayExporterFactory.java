package com.adcubum.timerecording.core.importexport.out.businessday;

import com.adcubum.timerecording.core.factory.AbstractFactory;

/**
 * The {@link BusinessDayExporterFactory} is used in order to create or get {@link BusinessDayExporter} instances
 * 
 * @author DStalder
 *
 */
public class BusinessDayExporterFactory extends AbstractFactory {

   private static final String BEAN_NAME = "businesdayexporter";
   private static final BusinessDayExporterFactory INSTANCE = new BusinessDayExporterFactory();

   private BusinessDayExporterFactory() {
      super("core-modul-configuration.xml");
   }

   /**
    * @return a new {@link BusinessDayExporterFactory} instance using it's default constructor
    */
   public static BusinessDayExporter createNew() {
      return INSTANCE.createNew(BEAN_NAME);
   }

}
