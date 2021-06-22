package com.adcubum.timerecording.core.factory;

import java.util.HashMap;
import java.util.Map;

/**
 * Default static factory in order to create new objects
 * 
 * @author Dominic
 *
 */
public class StaticFactory {

   private static final Map<String, DefaultFactory> defaultFactorys = new HashMap<>();

   private StaticFactory() {
      // private 
   }

   public synchronized static <T> T createNewObject(String name, String springXmlConfigFile) {
      if (defaultFactorys.containsKey(springXmlConfigFile)) {
         return defaultFactorys.get(springXmlConfigFile).createNew(name);
      }
      DefaultFactory defaultFactory = createAndAddNewFactory(springXmlConfigFile);
      return defaultFactory.createNew(name);
   }

   private static DefaultFactory createAndAddNewFactory(String springXmlConfigFile) {
      DefaultFactory defaultFactory = new DefaultFactory(springXmlConfigFile);
      defaultFactorys.put(springXmlConfigFile, defaultFactory);
      return defaultFactory;
   }
}
