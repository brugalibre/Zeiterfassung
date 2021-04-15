package com.adcubum.timerecording.jira.constants;

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

   /** The regex pattern for a valid ticket number */
   public static final String TICKET_NO_PATTERN = "(([a-zA-Z0-9-?]+)[-][0-9]+)";

   /** The delimiter to separate multiple entered ticket numbers */
   public static final String MULTI_TICKET_DELIMITER = ";";

   /** The regex pattern for a valid ticket number separated by the delimiter ';' */
   public static final String MULTI_TICKET_NO_PATTERN = "(" + TICKET_NO_PATTERN + "(" + MULTI_TICKET_DELIMITER + "?)){1,}";

}
