package com.adcubum.timerecording.core.importexport.util;

import com.adcubum.timerecording.jira.data.ticket.TicketActivity;

public class BusinessDayImportExportUtil {

   private static final String TICKET_ACTIVITY_CODE_AND_NAME_COMBINER = " - ";

   /**
    * Returns a String representing the given {@link TicketActivity} for an export
    *
    * @param ticketActivity the {@link TicketActivity}
    * @return a String representing the given {@link TicketActivity} for an export
    */
   public static String getTicketActivityString(TicketActivity ticketActivity) {
      return ticketActivity.getActivityCode() + TICKET_ACTIVITY_CODE_AND_NAME_COMBINER + ticketActivity.getActivityName();
   }

   /**
    * Extracts and returns the service code, which is contained in the string representing a {@link TicketActivity} for an export
    * @param ticketActivityRep
    *                         the {@link TicketActivity}
    * @return the service code which is contained in the string, representing a {@link TicketActivity} for an export
    */
   public static String getTicketActivityCodeFromStringRep(String ticketActivityRep) {
      return ticketActivityRep.split(BusinessDayImportExportUtil.TICKET_ACTIVITY_CODE_AND_NAME_COMBINER)[0];
   }
}
