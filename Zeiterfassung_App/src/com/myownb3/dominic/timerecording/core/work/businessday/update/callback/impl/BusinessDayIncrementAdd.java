/**
 * 
 */
package com.myownb3.dominic.timerecording.core.work.businessday.update.callback.impl;

import com.myownb3.dominic.timerecording.core.work.businessday.BusinessDayIncrement;
import com.myownb3.dominic.timerecording.core.work.businessday.TimeSnippet;
import com.myownb3.dominic.timerecording.ticketbacklog.data.Ticket;

/**
 * 
 * The {@link BusinessDayIncrementAdd} is used whenever a new
 * {@link BusinessDayIncrement} is added
 * 
 * @author Dominic
 *
 */
public class BusinessDayIncrementAdd {

   private Ticket ticket;
   private String description;
   private String amountOfHours;
   private int kindOfService;
   private TimeSnippet timeSnippet;

   private BusinessDayIncrementAdd() {
      // private 
   }

   public final Ticket getTicket() {
      return this.ticket;
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

   public final void setTicket(Ticket ticket) {
      this.ticket = ticket;
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
      private Ticket ticket;
      private String description;
      private String amountOfHours;
      private int kindOfService;
      private TimeSnippet timeSnippet;

      public BusinessDayIncrementAddBuilder withTicket(Ticket ticket) {
         this.ticket = ticket;
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
         businessDayIncrementAdd.setTicket(ticket);
         businessDayIncrementAdd.setKindOfService(kindOfService);
         businessDayIncrementAdd.setAmountOfHours(amountOfHours);
         return businessDayIncrementAdd;
      }
   }
}
