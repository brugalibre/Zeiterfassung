package com.adcubum.timerecording.core.work.businessday.update.callback.impl;

import com.adcubum.timerecording.app.TimeRecorder;
import com.adcubum.timerecording.core.work.businessday.update.callback.BusinessDayChangedCallbackHandler;

public class BusinessDayChangedCallbackHandlerImpl implements BusinessDayChangedCallbackHandler {

   @Override
   public void handleBusinessDayIncrementAdd(BusinessDayIncrementAdd businessDayIncrementAdd) {
      TimeRecorder.INSTANCE.addBusinessIncrement(businessDayIncrementAdd);
   }

   @Override
   public void handleBusinessDayIncrementDeleted(int index) {
      TimeRecorder.INSTANCE.removeIncrementAtIndex(index);
   }

   @Override
   public void handleBusinessDayChanged(ChangedValue changeValue) {
      TimeRecorder.INSTANCE.changeBusinesDayIncrement(changeValue);
   }

   @Override
   public void flagBusinessDayComeAndGoesAsRecorded() {
      TimeRecorder.INSTANCE.flagBusinessDayComeAndGoesAsRecorded();
   }
}
