package com.adcubum.timerecording.test;

import com.adcubum.timerecording.jira.data.ticket.Ticket;
import com.adcubum.timerecording.jira.data.ticket.TicketActivity;
import com.adcubum.timerecording.jira.data.ticket.TicketAttrs;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class TestTicket implements Ticket {

   private final String ticketNr;

   public TestTicket(String ticketNr) {
      this.ticketNr = ticketNr;
   }

   @Override
   public String toString() {
      return "ticketNr='" + ticketNr + "'";
   }

   @Override
   public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      Ticket that = (Ticket) o;
      return Objects.equals(ticketNr, that.getNr());
   }

   @Override
   public int hashCode() {
      return Objects.hash(ticketNr, ticketNr);
   }

   @Override
   public List<TicketActivity> getTicketActivities() {
      return Collections.emptyList();
   }

   @Override
   public String getTicketRep() {
      return ticketNr;
   }

   @Override
   public boolean isCurrentUserAssigned() {
      return false;
   }

   @Override
   public boolean isSprintTicket() {
      return false;
   }

   @Override
   public boolean isDummyTicket() {
      return false;
   }

   @Override
   public boolean isBookable() {
      return false;
   }

   @Override
   public String getNr() {
      return ticketNr;
   }

   @Override
   public TicketAttrs getTicketAttrs() {
      return null;
   }
}
