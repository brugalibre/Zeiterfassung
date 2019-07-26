package com.myownb3.dominic.timerecording.callback.handler;

import com.myownb3.dominic.timerecording.callback.handler.impl.BusinessDayIncrementAdd;
import com.myownb3.dominic.timerecording.callback.handler.impl.ChangedValue;
import com.myownb3.dominic.timerecording.work.businessday.BusinessDay;
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
    
    /**
     * Adds the given {@link BusinessDayIncrement} to the current {@link BusinessDay}
     * @param businessDayIncrementAdd the {@link BusinessDayIncrement} to add
     */
    public void handleBusinessDayIncrementAdd (BusinessDayIncrementAdd businessDayIncrementAdd);
}