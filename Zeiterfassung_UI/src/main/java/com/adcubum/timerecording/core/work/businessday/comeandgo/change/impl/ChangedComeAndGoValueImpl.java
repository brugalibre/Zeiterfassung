package com.adcubum.timerecording.core.work.businessday.comeandgo.change.impl;

import static java.util.Objects.requireNonNull;

import java.util.UUID;

import com.adcubum.timerecording.core.work.businessday.comeandgo.change.ChangedComeAndGoValue;
import com.adcubum.timerecording.work.date.Time;

public class ChangedComeAndGoValueImpl implements ChangedComeAndGoValue {

   private Time newGoValue;
   private Time newComeValue;
   private UUID id;

   private ChangedComeAndGoValueImpl(UUID id, Time newComeValue, Time newGoValue) {
      this.newComeValue = requireNonNull(newComeValue);
      this.newGoValue = newGoValue;
      this.id = requireNonNull(id);
   }

   @Override
   public UUID getId() {
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

   public static ChangedComeAndGoValue of(UUID id, Time newComeValue, Time newGoValue) {
      return new ChangedComeAndGoValueImpl(id, newComeValue, newGoValue);
   }
}
