/**
 * 
 */
package com.adcubum.timerecording.core.importexport.in.businessday;

import java.util.ArrayList;
import java.util.List;

import com.adcubum.timerecording.core.work.businessday.BusinessDay;
import com.adcubum.timerecording.core.work.businessday.BusinessDayIncrement;
import com.adcubum.timerecording.core.work.businessday.TimeSnippet;
import com.adcubum.timerecording.jira.data.ticket.TicketActivity;

/**
 * 
 * The {@link BusinessDayIncrementImport} is used whenever a {@link BusinessDay}
 * and also a new {@link BusinessDayIncrement} is imported
 * 
 * @author Dominic
 *
 */
public class BusinessDayIncrementImport {

   private String ticketNo;
   private String description;
   private TicketActivity ticketActivity;
   private List<TimeSnippet> timeSnippets;

   public BusinessDayIncrementImport() {
      timeSnippets = new ArrayList<>();
   }

   public final String getTicketNo() {
      return this.ticketNo;
   }

   public final String getDescription() {
      return this.description;
   }

   public final TicketActivity getTicketActivity() {
      return this.ticketActivity;
   }

   public final void setTicketNo(String ticketNo) {
      this.ticketNo = ticketNo;
   }

   public final void setDescription(String description) {
      this.description = description;
   }

   public List<TimeSnippet> getTimeSnippets() {
      return timeSnippets;
   }

   public final void setTicketActivity(TicketActivity ticketActivity) {
      this.ticketActivity = ticketActivity;
   }
}
