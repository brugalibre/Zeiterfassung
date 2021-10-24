package com.adcubum.timerecording.integtest;

import static java.util.Objects.requireNonNull;

import java.util.UUID;

import com.adcubum.timerecording.core.work.businessday.comeandgo.change.ChangedComeAndGoValue;
import com.adcubum.timerecording.work.date.DateTime;

public class TestChangedComeAndGoValueImpl implements ChangedComeAndGoValue {

   private DateTime newGoValue;
   private DateTime newComeValue;
   private UUID id;

   public TestChangedComeAndGoValueImpl(UUID id, DateTime newComeValue, DateTime newGoValue) {
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
}
