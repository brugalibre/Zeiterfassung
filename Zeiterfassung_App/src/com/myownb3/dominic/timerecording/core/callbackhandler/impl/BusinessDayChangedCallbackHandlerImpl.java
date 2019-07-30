package com.myownb3.dominic.timerecording.core.callbackhandler.impl;

import com.myownb3.dominic.timerecording.app.TimeRecorder;
import com.myownb3.dominic.timerecording.core.callbackhandler.BusinessDayChangedCallbackHandler;
import com.myownb3.dominic.timerecording.core.work.businessday.BusinessDay;

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
