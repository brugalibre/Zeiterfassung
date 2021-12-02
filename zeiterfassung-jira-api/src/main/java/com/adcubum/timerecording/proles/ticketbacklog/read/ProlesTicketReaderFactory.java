package com.adcubum.timerecording.proles.ticketbacklog.read;

import com.adcubum.timerecording.core.factory.AbstractFactory;

/**
 * Factory used to create and instantiate new {@link ProlesTicketReader} instances
 * 
 * @author DStalder
 *
 */
public class ProlesTicketReaderFactory extends AbstractFactory {
   private static final String BEAN_NAME = "prolesticketreader";
   private static final ProlesTicketReaderFactory INSTANCE = new ProlesTicketReaderFactory();

   private ProlesTicketReaderFactory() {
      super("jira-module-configuration.xml");
   }

   /**
    * Creates a new Instance of the {@link ProlesTicketReader} or returns an already created instance
    * 
    * @return a new Instance of the {@link ProlesTicketReader} or returns an already created instance
    */
   public static ProlesTicketReader createNew() {
      return INSTANCE.createNewWithAgruments(BEAN_NAME);
   }
}
