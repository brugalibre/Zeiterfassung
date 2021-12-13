package com.adcubum.timerecording.jira.jiraapi.configuration;

/**
 * Contains tickets constants
 * 
 * @author dominic
 *
 */
public class TicketConst {
   private TicketConst() {
      // private 
   }
   /** The default ticket / project name */
   public static final String DEFAULT_TICKET_NAME = "SYRIUS";

   /** The regex pattern for a valid ticket number */
   public static final String TICKET_NO_PATTERN = "(([a-zA-Z0-9-?]+)[-][0-9]+)";

}
