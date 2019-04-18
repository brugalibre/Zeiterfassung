package com.myownb3.dominic.timerecording.callback.handler;

import com.myownb3.dominic.timerecording.callback.handler.impl.ChangedValue;
import com.myownb3.dominic.timerecording.work.businessday.BusinessDay;

/**
 * Represents a owner of a {@link BusinessDay} which is interested in changes of a {@link BusinessDay}
 * @author DStalder
 *
 */
public interface BusinessDayChangedOwner {

    /**
     * May be called after a {@link BusinessDay} has changed
     * @param changeValue the {@link ChangedValue} with which a {@link BusinessDayChangedCallbackHandler} was triggered in the first place
     */
    public void afterBusinessDayChanged(ChangedValue changeValue);

}
