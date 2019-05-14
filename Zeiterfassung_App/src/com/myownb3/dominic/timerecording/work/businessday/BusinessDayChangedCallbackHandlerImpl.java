package com.myownb3.dominic.timerecording.work.businessday;

import java.util.Optional;

import com.myownb3.dominic.timerecording.app.TimeRecorder;
import com.myownb3.dominic.timerecording.callback.handler.BusinessDayChangedCallbackHandler;
import com.myownb3.dominic.timerecording.callback.handler.impl.ChangedValue;

public class BusinessDayChangedCallbackHandlerImpl implements BusinessDayChangedCallbackHandler {

    @Override
    public void handleBusinessDayIncrementDeleted(int index) {
        BusinessDay businessDay = TimeRecorder.getBussinessDay();
        businessDay.removeIncrementAtIndex(index);
    }
    
    @Override
    public void handleBusinessDayChanged(ChangedValue changeValue) {

	BusinessDay businessDay = TimeRecorder.getBussinessDay();

	Optional<BusinessDayIncrement> businessDayIncOpt = businessDay
		.getBusinessIncrement(changeValue.getSequence());
	businessDayIncOpt.ifPresent(businessDayIncrement -> {
	    handleBusinessDayChangedInternal(changeValue, businessDay, businessDayIncrement);
	});
    }

    private void handleBusinessDayChangedInternal(ChangedValue changedValue, BusinessDay businessDay,
	    BusinessDayIncrement businessDayIncremental) {

	switch (changedValue.getValueTypes()) {
	case DESCRIPTION:
	    businessDayIncremental.setDescription(changedValue.getNewValue());
	    break;
	case BEGIN:
	    businessDayIncremental.updateBeginTimeSnippetAndCalculate(businessDayIncremental,
		    changedValue.getFromUptoSequence(), changedValue.getNewValue());
	    break;
	case END:
	    businessDayIncremental.updateEndTimeSnippetAndCalculate(businessDayIncremental,
		    changedValue.getFromUptoSequence(), changedValue.getNewValue());
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
