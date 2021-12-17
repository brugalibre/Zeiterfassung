package com.adcubum.timerecording.ticketbacklog.config;

public class TicketConfigurationImpl implements TicketConfiguration {
   private String ticketNamePattern;
   private String defaultTicketName;
   private String multiTicketNamePattern;
   private boolean isDescriptionRequired;

   public TicketConfigurationImpl(String ticketNamePattern, String defaultTicketName, String multiTicketNamePattern, boolean isDescriptionRequired) {
      this.ticketNamePattern = ticketNamePattern;
      this.defaultTicketName = defaultTicketName;
      this.isDescriptionRequired = isDescriptionRequired;
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

   @Override
   public boolean getIsDescriptionRequired() {
      return isDescriptionRequired;
   }
}
