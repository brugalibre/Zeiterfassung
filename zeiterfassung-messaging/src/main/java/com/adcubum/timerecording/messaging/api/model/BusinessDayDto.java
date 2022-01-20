package com.adcubum.timerecording.messaging.api.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class BusinessDayDto {
   private List<BusinessDayIncrementDto> businessDayIncrementDtos;

   public BusinessDayDto() {
      this.businessDayIncrementDtos = new ArrayList<>();
   }

   @Override
   public String toString() {
      return "BusinessDayDto{" +
              "businessDayIncrementDtos=" + businessDayIncrementDtos +
              '}';
   }

   @Override
   public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      BusinessDayDto that = (BusinessDayDto) o;
      return businessDayIncrementDtos.equals(that.businessDayIncrementDtos);
   }

   @Override
   public int hashCode() {
      return Objects.hash(businessDayIncrementDtos);
   }

   public boolean hasBusinessDayIncrementDtos() {
      return !this.businessDayIncrementDtos.isEmpty();
   }

   public List<BusinessDayIncrementDto> getBusinessDayIncrementDtos() {
      return businessDayIncrementDtos;
   }

   public void setBusinessDayIncrementDtos(List<BusinessDayIncrementDto> businessDayIncrementDtos) {
      this.businessDayIncrementDtos = businessDayIncrementDtos;
   }

   public static class BusinessDayDtoBuilder {
      private final BusinessDayDto businessDayDto;

      private BusinessDayDtoBuilder() {
         this.businessDayDto = new BusinessDayDto();
      }

      public static BusinessDayDtoBuilder of() {
         return new BusinessDayDtoBuilder();
      }

      public BusinessDayDtoBuilder addBusinessDayIncrementDto(BusinessDayIncrementDto businessDayIncrementDto) {
         this.businessDayDto.getBusinessDayIncrementDtos().add(businessDayIncrementDto);
         return this;
      }

      public BusinessDayDto build() {
         return this.businessDayDto;
      }
   }
}
