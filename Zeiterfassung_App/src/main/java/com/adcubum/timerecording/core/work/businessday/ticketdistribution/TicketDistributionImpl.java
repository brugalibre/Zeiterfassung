package com.adcubum.timerecording.core.work.businessday.ticketdistribution;

import java.util.List;

public class TicketDistributionImpl implements TicketDistribution {
   private List<TicketDistributionEntry> ticketDistributionEntries;

   public TicketDistributionImpl(List<TicketDistributionEntry> ticketDistributionEntries) {
      this.ticketDistributionEntries = ticketDistributionEntries;
   }

   @Override
   public double getAmountOfHoursByTicketNr(String ticketNr) {
      return ticketDistributionEntries.stream()
              .filter(ticketDistributionEntry -> ticketDistributionEntry.getTicketNr().equals(ticketNr))
              .findFirst()
              .map(TicketDistributionEntry::getAmountOfHours)
              .orElse(0.0);
   }

   @Override
   public double getPercentageByTicketNr(String ticketNr) {
      double amountOfHoursByTicketNr = getAmountOfHoursByTicketNr(ticketNr);
      double totalAmountOfHours = ticketDistributionEntries.stream()
              .map(TicketDistributionEntry::getAmountOfHours)
              .reduce(0.0, (a, b) -> a + b);
      if (totalAmountOfHours == 0.0) {
         return 0.0;
      }
      return (amountOfHoursByTicketNr / totalAmountOfHours) * 100;
   }

   @Override
   public List<TicketDistributionEntry> getTicketDistributionsEntries() {
      return ticketDistributionEntries;
   }

   @Override
   public String toString() {
      return "TicketDistributionImpl{" +
              "ticketDistributionEntries=" + ticketDistributionEntries +
              '}';
   }
}
