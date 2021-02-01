package com.myownb3.dominic.timerecording.core.work.businessday.update.callback.impl;

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

   private ChangedValue(int sequence, String newValue, ValueTypes valueTypes) {
      this.sequence = sequence;
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

   public static ChangedValue of(int orderNumber, String newValueAsString, ValueTypes valueTypeForIndex) {
      return new ChangedValue(orderNumber, newValueAsString, valueTypeForIndex);
   }
}
