package com.adcubum.timerecording.ticketbacklog.factory;

import com.adcubum.timerecording.core.factory.AbstractFactory;
import com.adcubum.timerecording.ticketbacklog.TicketBacklog;

/**
 * Factory used to create and instantiate new {@link TicketBacklog} instances
 * 
 * @author DStalder
 *
 */
public class TicketBacklogFactory extends AbstractFactory {
   private static final String BEAN_NAME = "ticketbacklogfactorydelegate";
   private static final TicketBacklogFactory INSTANCE = new TicketBacklogFactory();

   private TicketBacklogFactory() {
      super("modul-configration.xml");
   }

   /**
    * Creates a new Instance of the {@link TicketBacklog} or returns an already created instance
    * <b>Note</b> that the specific implementation type of this {@link TicketBacklog} depends on
    * the configuration
    * 
    * @return a new Instance of the {@link TicketBacklog} or returns an already created instance
    */
   public static TicketBacklog createNew() {
      TicketBacklogFactoryDelegate ticketBacklogFactoryDelegate = INSTANCE.createNewWithAgruments(BEAN_NAME);
      return ticketBacklogFactoryDelegate.createTicketBacklog();
   }
}
