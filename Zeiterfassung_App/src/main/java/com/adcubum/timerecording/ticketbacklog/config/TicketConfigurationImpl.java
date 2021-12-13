package com.adcubum.timerecording.ticketbacklog.config;

public class TicketConfigurationImpl implements TicketConfiguration {
   private String ticketNamePattern;
   private String defaultTicketName;
   private String multiTicketNamePattern;

   public TicketConfigurationImpl(String ticketNamePattern, String defaultTicketName, String multiTicketNamePattern) {
      this.ticketNamePattern = ticketNamePattern;
      this.defaultTicketName = defaultTicketName;
      this.multiTicketNamePattern = multiTicketNamePattern;
   }

   @Override
   public String getTicketNamePattern() {
      return ticketNamePattern;
   }

   @Override
   public String getDefaultTicketName() {
      return defaultTicketName;
   }

   @Override
   public String getMultiTicketNamePattern() {
      return multiTicketNamePattern;
   }
}
