package com.myownb3.dominic.timerecording.callback.handler;

import com.myownb3.dominic.timerecording.work.businessday.BusinessDayIncremental;
import com.myownb3.dominic.timerecording.work.businessday.ValueTypes;

public interface BusinessDayChangedCallbackHandler {

    /**
     * Informs any listener that the {@link BusinessDayIncremental} for the given order no.
     * has changed
     * 
     * @param orderNr
     *            the order-number as unique identifier
     * @param newValue
     *            the new value
     * @param valueTypes
     *            the type of value
     */
    public void handleBusinessDayChanged(int orderNr, String newValue, ValueTypes valueTypes);

}
