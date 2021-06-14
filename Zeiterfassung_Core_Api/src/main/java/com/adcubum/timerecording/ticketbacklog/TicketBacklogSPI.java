package com.adcubum.timerecording.ticketbacklog;

/**
 * Provides access to the {@link TicketBacklog} implementation. Well it's not really an SPI yet
 * 
 * @author Dominic
 *
 */
public class TicketBacklogSPI {

   private static TicketBacklog ticketBacklog;

   private TicketBacklogSPI() {
      // private
   }

   /**
    * @return the {@link TicketBacklog}
    */
   public static synchronized TicketBacklog getTicketBacklog() {
      if (ticketBacklog == null) {
         ticketBacklog = TicketBacklogFactory.createNew();
      }
      return ticketBacklog;
   }
}
