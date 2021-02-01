package com.myownb3.dominic.timerecording.core.work.businessday.update.callback;

import com.myownb3.dominic.timerecording.core.work.businessday.TimeSnippet;
import com.myownb3.dominic.timerecording.core.work.businessday.update.callback.impl.ChangedValue;

public interface TimeSnippedChangedCallbackHandler {

   /**
    * Informs any listener that the {@link TimeSnippet} has changed
    * 
    * @param changeValue
    *        the changed Value
    */
   public void handleTimeSnippedChanged(ChangedValue changeValue);

}
