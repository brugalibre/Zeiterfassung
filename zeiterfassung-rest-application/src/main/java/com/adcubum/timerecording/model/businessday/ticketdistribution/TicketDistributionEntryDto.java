package com.adcubum.timerecording.model.businessday.ticketdistribution;

public class TicketDistributionEntryDto {
   private String ticketNr;
   private double percentage;

   TicketDistributionEntryDto(String ticketNr, double percentage) {
      this.ticketNr = ticketNr;
      this.percentage = percentage;
   }

   /**
    * @return the ticket-nr
    */
   public String getTicketNr() {
      return ticketNr;
   }

   /**
    * @return the percentage of this {@link TicketDistributionEntryDto} to the total frequency of all ticket-nrs
    */
   public double getPercentage() {
      return percentage;
   }
}
