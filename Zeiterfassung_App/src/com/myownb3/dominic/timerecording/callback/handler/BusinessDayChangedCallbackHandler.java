package com.myownb3.dominic.timerecording.callback.handler;

import com.myownb3.dominic.timerecording.callback.handler.impl.ChangedValue;
import com.myownb3.dominic.timerecording.work.businessday.BusinessDayIncrement;

public interface BusinessDayChangedCallbackHandler {

    /**
     * Informs any listener that the {@link BusinessDayIncrement} for the given order no.
     * has changed
     * 
     * @param changeValue
     *            the changed Value
     */
    public void handleBusinessDayChanged(ChangedValue changeValue);

    /**
     * Removes the {@link BusinessDayIncrement} at the given index
     * @param index the given index
     */
    public void handleBusinessDayIncrementDeleted (int index);
}
