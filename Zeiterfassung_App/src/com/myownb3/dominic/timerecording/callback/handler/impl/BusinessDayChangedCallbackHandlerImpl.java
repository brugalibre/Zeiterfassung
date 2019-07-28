package com.myownb3.dominic.timerecording.callback.handler.impl;

import com.myownb3.dominic.timerecording.app.TimeRecorder;
import com.myownb3.dominic.timerecording.callback.handler.BusinessDayChangedCallbackHandler;
import com.myownb3.dominic.timerecording.work.businessday.BusinessDay;

public class BusinessDayChangedCallbackHandlerImpl implements BusinessDayChangedCallbackHandler {

    @Override
    public void handleBusinessDayIncrementAdd(BusinessDayIncrementAdd businessDayIncrementAdd) {
	BusinessDay businessDay = TimeRecorder.INSTANCE.getBussinessDay();
	businessDay.addBusinessIncrement(businessDayIncrementAdd);
    }

    @Override
    public void handleBusinessDayIncrementDeleted(int index) {
	BusinessDay businessDay = TimeRecorder.INSTANCE.getBussinessDay();
	businessDay.removeIncrementAtIndex(index);
    }

    @Override
    public void handleBusinessDayChanged(ChangedValue changeValue) {

	BusinessDay businessDay = TimeRecorder.INSTANCE.getBussinessDay();
	businessDay.changeBusinesDayIncrement(changeValue);
    }
}
