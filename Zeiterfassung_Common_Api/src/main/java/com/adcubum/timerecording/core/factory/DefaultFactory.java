package com.adcubum.timerecording.core.factory;

/**
 * Default static factory in order to create new objects
 * 
 * @author Dominic
 *
 */
public class DefaultFactory extends AbstractFactory {

   /**
    * Package private constructor used by the {@link StaticFactory}
    * 
    * @param xmlFile
    */
   DefaultFactory(String xmlFile) {
      super(xmlFile);
   }
}
