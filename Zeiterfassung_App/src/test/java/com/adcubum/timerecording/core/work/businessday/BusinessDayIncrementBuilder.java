package com.adcubum.timerecording.core.work.businessday;

import java.util.UUID;

import com.adcubum.timerecording.core.work.businessday.update.callback.impl.BusinessDayIncrementAdd.BusinessDayIncrementAddBuilder;
import com.adcubum.timerecording.jira.data.ticket.Ticket;

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

   public BusinessDayIncrementBuilder withServiceCode(int serviceCode) {
      businessDayIncrementAddBuilder.withServiceCode(serviceCode);
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
         businessDayIncrement.flagAsCharged();
      }
      return businessDayIncrement;
   }

   public static BusinessDayIncrementBuilder of() {
      return new BusinessDayIncrementBuilder();
   }
}
