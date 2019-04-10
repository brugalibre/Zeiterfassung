package com.myownb3.dominic.timerecording.callback.handler.impl;

import com.myownb3.dominic.timerecording.callback.handler.BusinessDayChangedCallbackHandler;
import com.myownb3.dominic.timerecording.work.businessday.BusinessDay;
import com.myownb3.dominic.timerecording.work.businessday.BusinessDayIncremental;
import com.myownb3.dominic.timerecording.work.businessday.ValueTypes;

public class BusinessDayChangedCallbackHandlerImpl implements BusinessDayChangedCallbackHandler {

    private BusinessDay bussinessDay;

    public BusinessDayChangedCallbackHandlerImpl(BusinessDay bussinessDay) {
	this.bussinessDay = bussinessDay;
    }

    @Override
    public void handleBusinessDayChanged(int orderNr, String newValue, ValueTypes valueTypes) {

	BusinessDayIncremental businessDayIncremental = getBusinessInc(orderNr);
	if (businessDayIncremental != null) {
	    handleBusinessDayChangedInternal(newValue, valueTypes, businessDayIncremental);
	}
    }

    private void handleBusinessDayChangedInternal(String newValue, ValueTypes valueTypes,
	    BusinessDayIncremental businessDayIncremental) {
	switch (valueTypes) {
	case BEGIN:
	    break;
	case DESCRIPTION:
	    businessDayIncremental.setDescription(newValue);
	    break;
	case END:
	    break;
	case TICKET_NR:
	    businessDayIncremental.setTicketNumber(newValue);
	    break;
	default:
	    break;

	}
    }

    private BusinessDayIncremental getBusinessInc(int orderNr) {
	BusinessDayIncremental businessDayIncremental = null;
	for (int i = 0; i < bussinessDay.getIncrements().size(); i++) {
	    if (orderNr == i + 1) {
		businessDayIncremental = bussinessDay.getIncrements().get(i);
	    }
	}
	return businessDayIncremental;
    }
}
