package com.myownb3.dominic.timerecording.core.work.businessday.update.callback.impl;

import static java.util.Objects.isNull;
import static java.util.Objects.requireNonNull;

import com.myownb3.dominic.timerecording.core.work.businessday.BusinessDayIncrement;
import com.myownb3.dominic.timerecording.core.work.businessday.ValueTypes;
import com.myownb3.dominic.timerecording.ticketbacklog.data.Ticket;

/**
 * The {@link ChangedValue} contains information about a value of a
 * {@link BusinessDayIncrement} which was changed on the UI by a user
 * 
 * @author DStalder
 *
 */
public class ChangedValue {

   private int sequence; // the sequence of the increment
   private String newValue; // When description, begin or end has changed
   private Ticket newTicket; // When the Ticket itself has changed
   private ValueTypes valueTypes;

   private ChangedValue(int sequence, String newValue, Ticket newTicket, ValueTypes valueTypes) {
      if (isNull(newTicket) && isNull(newValue)) {
         throw new IllegalStateException("New Ticket and new String value must not both be null!");
      }
      this.sequence = sequence;
      this.newTicket = newTicket;
      this.newValue = newValue;
      this.valueTypes = valueTypes;
   }

   public int getSequence() {
      return sequence;
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

   public static ChangedValue of(int orderNumber, Object newValue, ValueTypes valueType) {
      Ticket ticket = castNewValue2Ticket(valueType, newValue);
      String newValue4Type = castNewValue2String(valueType, newValue);
      return new ChangedValue(orderNumber, newValue4Type, ticket, valueType);
   }

   private static Ticket castNewValue2Ticket(ValueTypes valueType, Object newValue) {
      if (valueType == ValueTypes.TICKET) {
         return (Ticket) newValue;
      }
      return null;
   }

   private static String castNewValue2String(ValueTypes valueType, Object newValue) {
      requireNonNull(newValue, "newValue must not be null!");
      switch (valueType) {
         case AMOUNT_OF_TIME:
         case BEGIN:
         case END:
         case DESCRIPTION:
         case CHARGE_TYPE:
         case TICKET_NR:
            return newValue.toString();
         default:
            break;
      }
      return null;
   }
}
