package com.adcubum.timerecording.integtest;

import static java.util.Objects.requireNonNull;

import java.util.UUID;

import com.adcubum.timerecording.core.work.businessday.comeandgo.change.ChangedComeAndGoValue;
import com.adcubum.timerecording.work.date.Time;

public class TestChangedComeAndGoValueImpl implements ChangedComeAndGoValue {

   private Time newGoValue;
   private Time newComeValue;
   private UUID id;

   public TestChangedComeAndGoValueImpl(UUID id, Time newComeValue, Time newGoValue) {
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
}
