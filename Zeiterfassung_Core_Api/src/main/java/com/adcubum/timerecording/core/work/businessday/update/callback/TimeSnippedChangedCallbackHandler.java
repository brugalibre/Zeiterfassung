package com.adcubum.timerecording.core.work.businessday.update.callback;

import com.adcubum.timerecording.core.work.businessday.TimeSnippet;
import com.adcubum.timerecording.core.work.businessday.update.callback.impl.ChangedValue;

public interface TimeSnippedChangedCallbackHandler {

   /**
    * Informs any listener that the {@link TimeSnippet} has changed
    * 
    * @param changeValue
    *        the changed Value
    */
   public void handleTimeSnippedChanged(ChangedValue changeValue);

}
