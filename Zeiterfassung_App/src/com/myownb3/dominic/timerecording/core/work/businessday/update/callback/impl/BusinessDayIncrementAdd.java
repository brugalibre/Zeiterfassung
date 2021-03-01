/**
 * 
 */
package com.myownb3.dominic.timerecording.core.work.businessday.update.callback.impl;

import com.myownb3.dominic.timerecording.core.work.businessday.BusinessDayIncrement;
import com.myownb3.dominic.timerecording.core.work.businessday.TimeSnippet;

/**
 * 
 * The {@link BusinessDayIncrementAdd} is used whenever a new
 * {@link BusinessDayIncrement} is added
 * 
 * @author Dominic
 *
 */
public class BusinessDayIncrementAdd {

   private String ticketNo;
   private String description;
   private String amountOfHours;
   private int kindOfService;
   private TimeSnippet timeSnippet;

   private BusinessDayIncrementAdd() {
      // private 
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

   public final TimeSnippet getTimeSnippet() {
      return this.timeSnippet;
   }

   public final void setTimeSnippet(TimeSnippet timeSnippet) {
      this.timeSnippet = timeSnippet;
   }

   public final void setAmountOfHours(String amountOfHours) {
      this.amountOfHours = amountOfHours;
   }

   public final void setKindOfService(int kindOfService) {
      this.kindOfService = kindOfService;
   }

   public static final class BusinessDayIncrementAddBuilder {
      private String ticketNo;
      private String description;
      private String amountOfHours;
      private int kindOfService;
      private TimeSnippet timeSnippet;

      public BusinessDayIncrementAddBuilder withTicketNo(String ticketNo) {
         this.ticketNo = ticketNo;
         return this;
      }

      public BusinessDayIncrementAddBuilder withDescription(String description) {
         this.description = description;
         return this;
      }

      public BusinessDayIncrementAddBuilder withAmountOfHours(String amountOfHours) {
         this.amountOfHours = amountOfHours;
         return this;
      }

      public BusinessDayIncrementAddBuilder withKindOfService(int kindOfService) {
         this.kindOfService = kindOfService;
         return this;
      }

      public BusinessDayIncrementAddBuilder withTimeSnippet(TimeSnippet timeSnippet) {
         this.timeSnippet = timeSnippet;
         return this;
      }

      public BusinessDayIncrementAdd build() {
         BusinessDayIncrementAdd businessDayIncrementAdd = new BusinessDayIncrementAdd();
         businessDayIncrementAdd.setTimeSnippet(timeSnippet);
         businessDayIncrementAdd.setDescription(description);
         businessDayIncrementAdd.setTicketNo(ticketNo);
         businessDayIncrementAdd.setKindOfService(kindOfService);
         businessDayIncrementAdd.setAmountOfHours(amountOfHours);
         return businessDayIncrementAdd;
      }
   }
}
