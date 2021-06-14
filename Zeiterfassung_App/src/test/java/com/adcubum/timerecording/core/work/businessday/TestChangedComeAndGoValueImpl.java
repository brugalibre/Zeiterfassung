package com.adcubum.timerecording.core.work.businessday;

import static java.util.Objects.requireNonNull;

import com.adcubum.timerecording.core.work.businessday.comeandgo.change.ChangedComeAndGoValue;
import com.adcubum.timerecording.work.date.Time;

public class TestChangedComeAndGoValueImpl implements ChangedComeAndGoValue {

   private Time newGoValue;
   private Time newComeValue;
   private String id;

   public TestChangedComeAndGoValueImpl(String id, Time newComeValue, Time newGoValue) {
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
}
