package com.adcubum.timerecording.ticketbacklog.factory;

import com.adcubum.timerecording.ticketbacklog.TicketBacklog;
import com.adcubum.timerecording.ticketbacklog.TicketBacklogImpl;
import com.adcubum.timerecording.ticketbacklog.proles.ProlesTicketBacklogImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;

import static java.util.Objects.requireNonNull;

public enum TicketBacklogType {

   /** A web based booker which books directly via a jira-ticket, using a proprietary plugin from adc */
   ADC_JIRA_WEB("adc_jira-web", TicketBacklogImpl.class),

   /** A web based booker which books directly via a jira-ticket, using a plugin from atlassian itself */
   JIRA_API("jira-api", TicketBacklogImpl.class),

   /** A web based booker which books directly via the proles-website */
   PROLES_WEB("proles-web", ProlesTicketBacklogImpl.class);

   private String name;
   private Class<? extends TicketBacklog> ticketBacklogClass;
   private static final Logger LOG = LoggerFactory.getLogger(TicketBacklogType.class);

   TicketBacklogType(String name, Class<? extends TicketBacklog> ticketBacklogClass) {
      this.name = requireNonNull(name);
      this.ticketBacklogClass = requireNonNull(ticketBacklogClass);
   }

   public String getName() {
      return name;
   }

   public TicketBacklog createTicketBacklog() {
      try {
         return ticketBacklogClass.getDeclaredConstructor().newInstance();
      } catch (InstantiationException | IllegalAccessException | IllegalArgumentException
            | InvocationTargetException | NoSuchMethodException | SecurityException e) {
         throw new IllegalArgumentException("Could not create new instance of TicketBacklog '" + ticketBacklogClass + "!'", e);
      }
   }

   public static TicketBacklogType getForName(String ticketBacklogName) {
      for (TicketBacklogType ticketBacklogType : TicketBacklogType.values()) {
         if (ticketBacklogType.name.equals(ticketBacklogName)) {
            return ticketBacklogType;
         }
      }
      LOG.warn("No TicketBacklogType found for name '{}'. Fallback to the default '{}'", ticketBacklogName, TicketBacklogType.ADC_JIRA_WEB.name);
      // Fallback due to backwards compatibility
      return TicketBacklogType.ADC_JIRA_WEB;
   }
}
