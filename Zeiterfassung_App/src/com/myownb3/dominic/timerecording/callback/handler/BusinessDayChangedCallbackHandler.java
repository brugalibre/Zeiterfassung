package com.myownb3.dominic.timerecording.callback.handler;

import com.myownb3.dominic.timerecording.callback.handler.impl.ChangedValue;
import com.myownb3.dominic.timerecording.work.businessday.BusinessDayIncremental;

public interface BusinessDayChangedCallbackHandler {

    /**
     * Informs any listener that the {@link BusinessDayIncremental} for the given order no.
     * has changed
     * 
     * @param changeValue
     *            the changed Value
     */
    public void handleBusinessDayChanged(ChangedValue changeValue);

}
