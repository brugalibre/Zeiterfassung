package com.adcubum.timerecording.core.work.businessday.ticketdistribution;

final class BookedTicketNrDto implements Comparable<BookedTicketNrDto>{
   private String ticketNr;
   private double amountOfHours;

   public BookedTicketNrDto(String ticketNr, double amountOfHours) {
      this.ticketNr = ticketNr;
      this.amountOfHours = amountOfHours;
   }

   public double getAmountOfHours() {
      return amountOfHours;
   }

   public String getTicketNr() {
      return ticketNr;
   }

   @Override
   public String toString() {
      return "BookedTicketNrDto{" +
              "ticketNr='" + ticketNr + '\'' +
              ", amountOfHours=" + amountOfHours +
              '}';
   }

   @Override
   public int compareTo(BookedTicketNrDto bookedTicketNrDto) {
      return this.ticketNr.compareTo(bookedTicketNrDto.ticketNr);
   }
}
