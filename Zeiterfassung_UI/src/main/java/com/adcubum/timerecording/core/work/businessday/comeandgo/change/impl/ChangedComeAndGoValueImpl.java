package com.adcubum.timerecording.core.work.businessday.comeandgo.change.impl;

import static java.util.Objects.requireNonNull;

import java.util.UUID;

import com.adcubum.timerecording.core.work.businessday.comeandgo.change.ChangedComeAndGoValue;
import com.adcubum.timerecording.work.date.DateTime;

public class ChangedComeAndGoValueImpl implements ChangedComeAndGoValue {

   private DateTime newGoValue;
   private DateTime newComeValue;
   private UUID id;

   private ChangedComeAndGoValueImpl(UUID id, DateTime newComeValue, DateTime newGoValue) {
      this.newComeValue = requireNonNull(newComeValue);
      this.newGoValue = newGoValue;
      this.id = requireNonNull(id);
   }

   @Override
   public UUID getId() {
      return id;
   }

   @Override
   public DateTime getNewComeValue() {
      return newComeValue;
   }

   @Override
   public DateTime getNewGoValue() {
      return newGoValue;
   }

   public static ChangedComeAndGoValue of(UUID id, DateTime newComeValue, DateTime newGoValue) {
      return new ChangedComeAndGoValueImpl(id, newComeValue, newGoValue);
   }
}
