package com.adcubum.timerecording.core.work.businessday.ticketdistribution;

public class TicketDistributionEntryImpl implements TicketDistributionEntry {
   private String ticketNr;
   private double amountOfHours;

   TicketDistributionEntryImpl(String ticketNr, double amountOfHours) {
      this.ticketNr = ticketNr;
      this.amountOfHours = amountOfHours;
   }

   @Override
   public String getTicketNr() {
      return ticketNr;
   }

   @Override
   public double getAmountOfHours() {
      return amountOfHours;
   }

   @Override
   public String toString() {
      return "TicketDistributionEntryImpl{" +
              "ticketNr='" + ticketNr + '\'' +
              ", amountOfHours=" + amountOfHours +
              '}';
   }
}
