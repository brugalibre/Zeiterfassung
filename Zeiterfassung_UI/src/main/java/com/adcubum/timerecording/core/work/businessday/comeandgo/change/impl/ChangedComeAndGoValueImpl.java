package com.adcubum.timerecording.core.work.businessday.comeandgo.change.impl;

import static java.util.Objects.requireNonNull;

import com.adcubum.timerecording.core.work.businessday.comeandgo.change.ChangedComeAndGoValue;
import com.adcubum.timerecording.work.date.Time;

public class ChangedComeAndGoValueImpl implements ChangedComeAndGoValue {

   private Time newGoValue;
   private Time newComeValue;
   private String id;

   private ChangedComeAndGoValueImpl(String id, Time newComeValue, Time newGoValue) {
      this.newComeValue = requireNonNull(newComeValue);
      this.newGoValue = newGoValue;
      this.id = requireNonNull(id);
   }

   @Override
   public String getId() {
      return id;
   }

   @Override
   public Time getNewComeValue() {
      return newComeValue;
   }

   @Override
   public Time getNewGoValue() {
      return newGoValue;
   }

   public static ChangedComeAndGoValue of(String id, Time newComeValue, Time newGoValue) {
      return new ChangedComeAndGoValueImpl(id, newComeValue, newGoValue);
   }
}
