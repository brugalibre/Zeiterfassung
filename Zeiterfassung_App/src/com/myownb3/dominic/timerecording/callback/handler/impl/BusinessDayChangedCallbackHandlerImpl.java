package com.myownb3.dominic.timerecording.callback.handler.impl;

import java.util.Optional;

import com.myownb3.dominic.timerecording.callback.handler.BusinessDayChangedCallbackHandler;
import com.myownb3.dominic.timerecording.callback.handler.BusinessDayChangedOwner;
import com.myownb3.dominic.timerecording.work.businessday.BusinessDay;
import com.myownb3.dominic.timerecording.work.businessday.BusinessDayIncremental;

public class BusinessDayChangedCallbackHandlerImpl implements BusinessDayChangedCallbackHandler {

    private BusinessDay bussinessDay;
    private BusinessDayChangedOwner businessDayChangedOwner;

    public BusinessDayChangedCallbackHandlerImpl(BusinessDay bussinessDay,
	    BusinessDayChangedOwner businessDayChangedOwner) {
	this.bussinessDay = bussinessDay;
	this.businessDayChangedOwner = businessDayChangedOwner;
    }

    @Override
    public void handleBusinessDayChanged(ChangedValue changeValue) {

	Optional<BusinessDayIncremental> businessDayIncOpt = getBusinessIncrement(changeValue.getSequence());
	businessDayIncOpt.ifPresent(businessDayIncrement -> {
	    handleBusinessDayChangedInternal(changeValue, businessDayIncrement);
	});
    }

    private void handleBusinessDayChangedInternal(ChangedValue changedValue,
	    BusinessDayIncremental businessDayIncremental) {
	switch (changedValue.getValueTypes()) {
	case DESCRIPTION:
	    businessDayIncremental.setDescription(changedValue.getNewValue());
	    break;
	case BEGIN:
	    businessDayIncremental.updateBeginTimeSnippetAndCalculate(businessDayIncremental, changedValue.getFromUptoSequence(), changedValue.getNewValue());
	    break;
	case END:
	    businessDayIncremental.updateEndTimeSnippetAndCalculate(businessDayIncremental, changedValue.getFromUptoSequence(), changedValue.getNewValue());
	    break;
	case TICKET_NR:
	    businessDayIncremental.setTicketNumber(changedValue.getNewValue());
	    break;
	default:
	    break;
	}
	businessDayChangedOwner.afterBusinessDayChanged(changedValue);
    }

    private Optional<BusinessDayIncremental> getBusinessIncrement(int orderNr) {
	BusinessDayIncremental businessDayIncremental = null;
	for (int i = 0; i < bussinessDay.getIncrements().size(); i++) {
	    if (orderNr == i + 1) {
		businessDayIncremental = bussinessDay.getIncrements().get(i);
	    }
	}
	return Optional.ofNullable(businessDayIncremental);
    }
}
