package com.myownb3.dominic.timerecording.callback.handler;

import com.myownb3.dominic.timerecording.callback.handler.impl.ChangedValue;
import com.myownb3.dominic.timerecording.work.businessday.TimeSnippet;

public interface TimeSnippedChangedCallbackHandler {

    /**
     * Informs any listener that the {@link TimeSnippet} has changed
     * 
     * @param changeValue the changed Value
     */
    public void handleTimeSnippedChanged(ChangedValue changeValue);

}
