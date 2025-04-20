package com.adcubum.timerecording.settings.common;

/**
 * Contains some common constans for settings
 * 
 * @author Dominic
 *
 */
public class Const {

   private Const() {
      // private 
   }

   /** The ticket-name within proles for postfinance related work. We define at a central location this here */
   public static final String PROLES_PF_TICKET_NAME = "ProlesPFTicketName";

   /** The properites for the proles-ticket and proles-ticket-activities*/
   public static final String PROLES_TICKET_ACTIVITIES_PROPERTIES = "proles-ticket.properties";

   /**
    * The properties file for the turbo-bucher
    */
   public static final String TURBO_BUCHER_PROPERTIES = "turbo-bucher.properties";

   /** The properties file for the main application */
   public static final String ZEITERFASSUNG_PROPERTIES = "application.yml";

   /** The properties file for the ticket and booking-system  */
   public static final String TICKET_SYSTEM_PROPERTIES = "ticketSystem.properties";
}
