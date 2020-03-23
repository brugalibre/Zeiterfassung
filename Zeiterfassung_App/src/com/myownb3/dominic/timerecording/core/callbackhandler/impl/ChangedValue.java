package com.myownb3.dominic.timerecording.core.callbackhandler.impl;

import com.myownb3.dominic.timerecording.core.work.businessday.BusinessDayIncrement;
import com.myownb3.dominic.timerecording.core.work.businessday.ValueTypes;

/**
 * The {@link ChangedValue} contains information about a value of a
 * {@link BusinessDayIncrement} which was changed on the UI by a user
 * 
 * @author DStalder
 *
 */
public class ChangedValue {

   private int sequence; // the sequence of the increment
   private String newValue;
   private ValueTypes valueTypes;
   private int fromUptoSequence; // the order if the changed value was 'from' or
   // 'upto'

   private ChangedValue(int sequence, String newValue, ValueTypes valueTypes, int vonBisOrder) {
      this.sequence = sequence;
      this.newValue = newValue;
      this.valueTypes = valueTypes;
      this.fromUptoSequence = vonBisOrder;
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

   public int getFromUptoSequence() {
      return fromUptoSequence;
   }

   public static ChangedValue of(int orderNumber, String newValueAsString, ValueTypes valueTypeForIndex) {
      return new ChangedValue(orderNumber, newValueAsString, valueTypeForIndex, -1);
   }

   public static ChangedValue of(int orderNumber, String newValueAsString, ValueTypes valueTypeForIndex,
         int fromUptoSequence) {
      return new ChangedValue(orderNumber, newValueAsString, valueTypeForIndex, fromUptoSequence);
   }
}
