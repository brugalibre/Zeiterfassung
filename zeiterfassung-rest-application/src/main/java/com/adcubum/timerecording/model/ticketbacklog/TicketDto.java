package com.adcubum.timerecording.model.ticketbacklog;

import java.util.ArrayList;
import java.util.List;

import com.adcubum.timerecording.core.work.businessday.ValueTypes;
import com.adcubum.timerecording.jira.data.ticket.Ticket;

public class TicketDto {

   private String ticketNr;
   private String ticketRepresentation;
   private boolean isBookable;
   private long projectNr;

   @SuppressWarnings("unused")
   private TicketDto() {
      // private used for deserialization
   }

   public TicketDto(String ticketNr, String ticketRepresentation, boolean isBookable, long projectNr) {
      this.ticketNr = ticketNr;
      this.ticketRepresentation = ticketRepresentation;
      this.isBookable = isBookable;
      this.projectNr = projectNr;
   }

   public String getTicketNr() {
      return ticketNr;
   }

   public String getTicketRepresentation() {
      return ticketRepresentation;
   }

   public long getProjectNr() {
      return projectNr;
   }

   public boolean isBookable() {
      return isBookable;
   }

   @Override
   public String toString() {
      return "TicketDto [ticketNr=" + ticketNr + ", ticketRepresentation=" + ticketRepresentation + ", isBookable=" + isBookable + ", projectNr="
            + projectNr + "]";
   }

   public List<ValueTypes> compareAndEvalChangedValues(TicketDto otherTicketDto) {
      List<ValueTypes> changedAttrs = new ArrayList<>();
      if (!ticketNr.equals(otherTicketDto.getTicketNr())) {
         changedAttrs.add(ValueTypes.TICKET_NR);
      }

      return changedAttrs;
   }

   /**
    * @param valueType
    *        the given {@link ValueTypes}
    * @return the value of this {@link TicketDto} according to the given {@link ValueTypes} or <code>null</code> if there is no value for
    *         the given ValueTypes
    */
   public String getChangedValue(ValueTypes valueType) {
      if (valueType == ValueTypes.TICKET_NR) {
         return ticketNr;
      }
      return null;
   }

   /**
    * Creates a new {@link TicketDto} for the given {@link Ticket}
    * 
    * @param ticket
    *        the {@link Ticket}
    * @return a new {@link TicketDto}
    */
   public static TicketDto of(Ticket ticket) {
      return new TicketDto(ticket.getNr(), ticket.getTicketRep(), ticket.isBookable(), ticket.getTicketAttrs().getProjectNr());
   }
}
