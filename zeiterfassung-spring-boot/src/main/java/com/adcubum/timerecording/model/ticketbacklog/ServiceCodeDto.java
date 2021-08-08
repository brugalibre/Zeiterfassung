package com.adcubum.timerecording.model.ticketbacklog;

public class ServiceCodeDto {

   private int value;
   private String representation;

   public ServiceCodeDto(int value, String representation) {
      this.value = value;
      this.representation = representation;
   }

   public ServiceCodeDto() {
      // empty for serialization
   }

   public int getValue() {
      return value;
   }

   public String getRepresentation() {
      return representation;
   }

   @Override
   public String toString() {
      return "ServiceCodeDto [value=" + value + ", representation=" + representation + "]";
   }
}
