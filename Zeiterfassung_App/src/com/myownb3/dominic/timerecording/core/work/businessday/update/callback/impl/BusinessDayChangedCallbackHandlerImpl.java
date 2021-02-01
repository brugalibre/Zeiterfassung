package com.myownb3.dominic.timerecording.core.work.businessday.update.callback.impl;

import com.myownb3.dominic.timerecording.app.TimeRecorder;
import com.myownb3.dominic.timerecording.core.work.businessday.update.callback.BusinessDayChangedCallbackHandler;

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
}
