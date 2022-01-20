package com.adcubum.timerecording.messaging.api.model;

import java.util.Objects;

public class BusinessDayIncrementDto {
   private TimeSnippetDto timeSnippetDto;
   private String description;
   private String ticketNr;
   private String ticketActivityCode;

   @Override
   public String toString() {
      return "BusinessDayIncrementDto{" +
              "timeSnippetDto=" + timeSnippetDto +
              ", description='" + description + '\'' +
              ", ticketNr='" + ticketNr + '\'' +
              ", ticketActivityCode='" + ticketActivityCode + '\'' +
              '}';
   }

   @Override
   public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      BusinessDayIncrementDto that = (BusinessDayIncrementDto) o;
      return timeSnippetDto.equals(that.timeSnippetDto) && description.equals(that.description) && ticketNr.equals(that.ticketNr) && ticketActivityCode.equals(that.ticketActivityCode);
   }

   @Override
   public int hashCode() {
      return Objects.hash(timeSnippetDto, description, ticketNr, ticketActivityCode);
   }

   public TimeSnippetDto getTimeSnippetDto() {
      return timeSnippetDto;
   }

   public void setTimeSnippetDto(TimeSnippetDto timeSnippetDto) {
      this.timeSnippetDto = timeSnippetDto;
   }

   public String getDescription() {
      return description;
   }

   public void setDescription(String description) {
      this.description = description;
   }

   public String getTicketNr() {
      return ticketNr;
   }

   public void setTicketNr(String ticketNr) {
      this.ticketNr = ticketNr;
   }

   public String getTicketActivityCode() {
      return ticketActivityCode;
   }

   public void setTicketActivityCode(String ticketActivityCode) {
      this.ticketActivityCode = ticketActivityCode;
   }

   public static class BusinessDayIncrementDtoBuilder {
      private final BusinessDayIncrementDto businessDayIncrementDto;

      private BusinessDayIncrementDtoBuilder() {
         this.businessDayIncrementDto = new BusinessDayIncrementDto();
      }

      public static BusinessDayIncrementDtoBuilder of() {
         return new BusinessDayIncrementDtoBuilder();
      }

      public BusinessDayIncrementDtoBuilder withTimeSnippetDto(TimeSnippetDto timeSnippetDto) {
         this.businessDayIncrementDto.setTimeSnippetDto(timeSnippetDto);
         return this;
      }

      public BusinessDayIncrementDtoBuilder withTicketActivityCode(String ticketActivityCode) {
         this.businessDayIncrementDto.setTicketActivityCode(ticketActivityCode);
         return this;
      }

      public BusinessDayIncrementDtoBuilder withTicketNr(String ticketNr) {
         this.businessDayIncrementDto.setTicketNr(ticketNr);
         return this;
      }

      public BusinessDayIncrementDtoBuilder withDescription(String description) {
         this.businessDayIncrementDto.setDescription(description);
         return this;
      }

      public BusinessDayIncrementDto build() {
         return this.businessDayIncrementDto;
      }
   }
}
