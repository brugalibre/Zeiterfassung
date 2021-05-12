package com.adcubum.timerecording.core.work.businessday.comeandgo.change.impl;

import static java.util.Objects.requireNonNull;

import com.adcubum.timerecording.app.TimeRecorder;
import com.adcubum.timerecording.core.work.businessday.comeandgo.ComeAndGoes;
import com.adcubum.timerecording.core.work.businessday.comeandgo.change.ChangedComeAndGoValue;
import com.adcubum.timerecording.core.work.businessday.comeandgo.change.ComeAndGoesUpdater;

public class ComeAndGoesUpdaterImpl implements ComeAndGoesUpdater {

   private TimeRecorder timeRecorder;

   /**
    * Creates a new {@link ComeAndGoesUpdaterImpl}
    */
   public ComeAndGoesUpdaterImpl() {
      this.timeRecorder = TimeRecorder.INSTANCE;
   }

   /**
    * Constructor for testing purpose!
    * 
    * @param timeRecorder
    */
   public ComeAndGoesUpdaterImpl(TimeRecorder timeRecorder) {
      this.timeRecorder = requireNonNull(timeRecorder);
   }

   @Override
   public ComeAndGoes changeComeAndGo(ChangedComeAndGoValue changedComeAndGoValue) {
      return timeRecorder.changeComeAndGo(changedComeAndGoValue);
   }
}
