package com.adcubum.timerecording.core.work.businessday.update.callback.impl;

import com.adcubum.timerecording.core.work.businessday.BusinessDayIncrement;
import com.adcubum.timerecording.core.work.businessday.ValueTypes;
import com.adcubum.timerecording.jira.data.ticket.Ticket;
import com.adcubum.timerecording.jira.data.ticket.TicketActivity;

import java.util.UUID;

import static java.util.Objects.*;

/**
 * The {@link ChangedValue} contains information about a value of a
 * {@link BusinessDayIncrement} which was changed on the UI by a user
 * 
 * @author DStalder
 *
 */
public class ChangedValue {

   private UUID id; // the id of the increment
   private String newValue; // When description, begin or end has changed
   private Ticket newTicket; // When the Ticket itself has changed
   private TicketActivity newTicketActivity; // When the Ticket itself has changed
   private ValueTypes valueTypes;

   private ChangedValue(UUID id, String newValue, Ticket newTicket, TicketActivity ticketActivity, ValueTypes valueTypes) {
      if (isNull(newTicket) && isNull(newValue)) {
         throw new IllegalStateException("New Ticket and new String value must not both be null!");
      }
      this.id = requireNonNull(id);
      this.newTicket = newTicket;
      this.newTicketActivity = ticketActivity;
      this.newValue = newValue;
      this.valueTypes = valueTypes;
   }

   public UUID getId() {
      return id;
   }

   public String getNewValue() {
      return newValue;
   }

   public ValueTypes getValueTypes() {
      return valueTypes;
   }

   public Ticket getNewTicket() {
      return newTicket;
   }

   public TicketActivity getNewTicketActivity() {
      return newTicketActivity;
   }

   public static ChangedValue of(UUID id, Object newValue, ValueTypes valueType) {
      Ticket ticket = castNewValue2Ticket(valueType, newValue);
      TicketActivity ticketActivity = castNewValue2TicketActivity(valueType, newValue);
      String newValue4Type = castNewValue2String(newValue);
      return new ChangedValue(id, newValue4Type, ticket, ticketActivity, valueType);
   }

   public static ChangedValue of(Object newValue, ValueTypes valueType) {
      return of(UUID.randomUUID(), newValue, valueType);
   }

   private static Ticket castNewValue2Ticket(ValueTypes valueType, Object newValue) {
      if (valueType == ValueTypes.TICKET) {
         return (Ticket) newValue;
      }
      return null;
   }
   private static TicketActivity castNewValue2TicketActivity(ValueTypes valueType, Object newValue) {
     if (valueType == ValueTypes.TICKET_ACTIVITY) {
         return (TicketActivity) newValue;
      }
      return null;
   }

   private static String castNewValue2String(Object newValue) {
      return nonNull(newValue) ? newValue.toString() : null;
   }
}
