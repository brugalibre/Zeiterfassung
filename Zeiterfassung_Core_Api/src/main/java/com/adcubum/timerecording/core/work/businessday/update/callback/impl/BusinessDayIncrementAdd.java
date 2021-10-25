/**
 * 
 */
package com.adcubum.timerecording.core.work.businessday.update.callback.impl;

import static java.util.Objects.requireNonNull;

import java.util.UUID;

import com.adcubum.timerecording.core.work.businessday.BusinessDayIncrement;
import com.adcubum.timerecording.core.work.businessday.TimeSnippet;
import com.adcubum.timerecording.jira.data.ticket.Ticket;

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
   private UUID id;

   private BusinessDayIncrementAdd() {
      // private 
   }

   public final Ticket getTicket() {
      return this.ticket;
   }

   public UUID getId() {
      return id;
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

   public final void setServiceCode(int kindOfService) {
      this.kindOfService = kindOfService;
   }

   public static final class BusinessDayIncrementAddBuilder {
      private Ticket ticket;
      private String description;
      private String amountOfHours;
      private int kindOfService;
      private TimeSnippet timeSnippet;
      private UUID id;

      public BusinessDayIncrementAddBuilder withTicket(Ticket ticket) {
         this.ticket = requireNonNull(ticket);
         return this;
      }

      /**
       * Sets an id if it's requried (e.g. for testing purpose) to provide an id for a non persisten {@link BusinessDayIncrement}
       * 
       * @param id
       *        id
       * @return
       */
      public BusinessDayIncrementAddBuilder withId(UUID id) {
         this.id = id;
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

      public BusinessDayIncrementAddBuilder withServiceCode(int kindOfService) {
         this.kindOfService = kindOfService;
         return this;
      }

      public BusinessDayIncrementAddBuilder withTimeSnippet(TimeSnippet timeSnippet) {
         this.timeSnippet = timeSnippet;
         return this;
      }

      /**
       * Creates a new {@link BusinessDayIncrementAdd} from the given {@link BusinessDayIncrement}
       */
      public BusinessDayIncrementAddBuilder from(BusinessDayIncrement businessDayIncrement) {
         return withDescription(businessDayIncrement.getDescription())
               .withServiceCode(businessDayIncrement.getChargeType())
               .withTicket(businessDayIncrement.getTicket())
               .withTimeSnippet(businessDayIncrement.getCurrentTimeSnippet())
               .withId(null);
      }

      public BusinessDayIncrementAdd build() {
         BusinessDayIncrementAdd businessDayIncrementAdd = new BusinessDayIncrementAdd();
         businessDayIncrementAdd.setTimeSnippet(timeSnippet);
         businessDayIncrementAdd.setDescription(description);
         businessDayIncrementAdd.setTicket(ticket);
         businessDayIncrementAdd.setServiceCode(kindOfService);
         businessDayIncrementAdd.setAmountOfHours(amountOfHours);
         businessDayIncrementAdd.id = id;
         return businessDayIncrementAdd;
      }
   }
}
