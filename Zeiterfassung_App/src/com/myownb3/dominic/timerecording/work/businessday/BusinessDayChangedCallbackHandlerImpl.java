package com.myownb3.dominic.timerecording.work.businessday;

import java.util.Optional;

import com.myownb3.dominic.timerecording.callback.handler.BusinessDayChangedCallbackHandler;
import com.myownb3.dominic.timerecording.callback.handler.impl.ChangedValue;

public class BusinessDayChangedCallbackHandlerImpl implements BusinessDayChangedCallbackHandler {

    private BusinessDay businessDay;

    public BusinessDayChangedCallbackHandlerImpl(BusinessDay bussinessDay){
	this.businessDay = bussinessDay;
    }

    @Override
    public void handleBusinessDayChanged(ChangedValue changeValue) {

	Optional<BusinessDayIncremental> businessDayIncOpt = businessDay.getBusinessIncrement(changeValue.getSequence());
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
	businessDay.checkForRedundancys();
    }
}
