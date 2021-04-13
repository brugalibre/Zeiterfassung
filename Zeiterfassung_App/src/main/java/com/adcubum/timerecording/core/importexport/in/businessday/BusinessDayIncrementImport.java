/**
 * 
 */
package com.adcubum.timerecording.core.importexport.in.businessday;

import java.util.ArrayList;
import java.util.List;

import com.adcubum.timerecording.core.work.businessday.BusinessDay;
import com.adcubum.timerecording.core.work.businessday.BusinessDayIncrement;
import com.adcubum.timerecording.core.work.businessday.TimeSnippet;

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
   private String amountOfHours;
   private int kindOfService;
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

   public final String getAmountOfHours() {
      return this.amountOfHours;
   }

   public final int getKindOfService() {
      return this.kindOfService;
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

   public void setTimeSnippets(List<TimeSnippet> timeSnippets) {
      this.timeSnippets = timeSnippets;
   }

   public final void setAmountOfHours(String amountOfHours) {
      this.amountOfHours = amountOfHours;
   }

   public final void setKindOfService(int kindOfService) {
      this.kindOfService = kindOfService;
   }
}
