package com.adcubum.timerecording.core.work.businessday.update.callback.impl;

import java.util.UUID;

import com.adcubum.timerecording.app.TimeRecorder;
import com.adcubum.timerecording.core.work.businessday.comeandgo.ComeAndGoes;
import com.adcubum.timerecording.core.work.businessday.comeandgo.change.ChangedComeAndGoValue;
import com.adcubum.timerecording.core.work.businessday.update.callback.BusinessDayChangedCallbackHandler;

public class BusinessDayChangedCallbackHandlerImpl implements BusinessDayChangedCallbackHandler {

   private TimeRecorder timeRecorder;

   /**
    * Called by the {@link BusinessDayChangedCallbackHandlerFactory}
    */
   @SuppressWarnings("unused")
   private BusinessDayChangedCallbackHandlerImpl() {
      this.timeRecorder = TimeRecorder.INSTANCE;
   }

   /**
    * Constructor only for testing purpose
    */
   public BusinessDayChangedCallbackHandlerImpl(TimeRecorder timeRecorder) {
      this.timeRecorder = timeRecorder;
   }

   @Override
   public void handleBusinessDayIncrementAdd(BusinessDayIncrementAdd businessDayIncrementAdd) {
      timeRecorder.addBusinessIncrement(businessDayIncrementAdd);
   }

   @Override
   public void handleBusinessDayIncrementDeleted(UUID id) {
      timeRecorder.removeIncrement4Id(id);
   }

   @Override
   public void handleBusinessDayChanged(ChangedValue changeValue) {
      timeRecorder.changeBusinesDayIncrement(changeValue);
   }

   @Override
   public void flagBusinessDayComeAndGoesAsRecorded() {
      timeRecorder.flagBusinessDayComeAndGoesAsRecorded();
   }

   @Override
   public ComeAndGoes changeComeAndGo(ChangedComeAndGoValue changedComeAndGoValue) {
      return timeRecorder.changeComeAndGo(changedComeAndGoValue);
   }

   @Override
   public void clear() {
      timeRecorder.clear();
   }

   @Override
   public void clearComeAndGoes() {
      timeRecorder.clearComeAndGoes();
   }
}
