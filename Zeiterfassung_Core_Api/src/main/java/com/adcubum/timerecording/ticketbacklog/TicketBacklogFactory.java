package com.adcubum.timerecording.ticketbacklog;

import com.adcubum.timerecording.core.factory.AbstractFactory;

/**
 * Factory used to create and instantiate new {@link TicketBacklog} instances
 * 
 * @author DStalder
 *
 */
public class TicketBacklogFactory extends AbstractFactory {
   private static final String BEAN_NAME = "ticketbacklog";
   private static final TicketBacklogFactory INSTANCE = new TicketBacklogFactory();

   private TicketBacklogFactory() {
      super("modul-configration.xml");
   }

   /**
    * Creates a new Instance of the {@link TicketBacklog} or returns an already created instance
    * 
    * @return a new Instance of the {@link TicketBacklog} or returns an already created instance
    */
   public static TicketBacklog createNew() {
      return INSTANCE.createNewWithAgruments(BEAN_NAME);
   }
}
