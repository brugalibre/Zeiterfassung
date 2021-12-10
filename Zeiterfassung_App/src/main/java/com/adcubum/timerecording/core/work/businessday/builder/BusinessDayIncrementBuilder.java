package com.adcubum.timerecording.core.work.businessday.builder;

import java.util.UUID;

import com.adcubum.timerecording.core.work.businessday.BusinessDayIncrement;
import com.adcubum.timerecording.core.work.businessday.BusinessDayIncrementImpl;
import com.adcubum.timerecording.core.work.businessday.TimeSnippet;
import com.adcubum.timerecording.core.work.businessday.update.callback.impl.BusinessDayIncrementAdd.BusinessDayIncrementAddBuilder;
import com.adcubum.timerecording.jira.data.ticket.Ticket;
import com.adcubum.timerecording.jira.data.ticket.TicketActivity;

public class BusinessDayIncrementBuilder {
   private BusinessDayIncrementAddBuilder businessDayIncrementAddBuilder;
   private boolean doFlagAsBooked;

   private BusinessDayIncrementBuilder() {
      this.businessDayIncrementAddBuilder = new BusinessDayIncrementAddBuilder();
      // private
   }

   public BusinessDayIncrementBuilder withDescription(String description) {
      businessDayIncrementAddBuilder.withDescription(description);
      return this;
   }

   public BusinessDayIncrementBuilder withTicketActivity(TicketActivity ticketActivity) {
      businessDayIncrementAddBuilder.withTicketActivity(ticketActivity);
      return this;
   }

   public BusinessDayIncrementBuilder withTimeSnippet(TimeSnippet timeSnippet) {
      businessDayIncrementAddBuilder.withTimeSnippet(timeSnippet);
      return this;
   }

   public BusinessDayIncrementBuilder withTicket(Ticket ticket) {
      businessDayIncrementAddBuilder.withTicket(ticket);
      return this;
   }

   public BusinessDayIncrementBuilder withId(UUID id) {
      businessDayIncrementAddBuilder.withId(id);
      return this;
   }

   public BusinessDayIncrementBuilder withFlagAsBooked() {
      this.doFlagAsBooked = true;
      return this;
   }

   public BusinessDayIncrement build() {
      BusinessDayIncrement businessDayIncrement = BusinessDayIncrementImpl.of(businessDayIncrementAddBuilder
            .build());
      if (doFlagAsBooked) {
         return businessDayIncrement.flagAsBooked();
      }
      return businessDayIncrement;
   }

   public static BusinessDayIncrementBuilder of() {
      return new BusinessDayIncrementBuilder();
   }
}
