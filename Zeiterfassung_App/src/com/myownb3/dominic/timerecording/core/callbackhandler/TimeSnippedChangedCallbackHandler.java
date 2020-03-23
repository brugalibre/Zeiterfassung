package com.myownb3.dominic.timerecording.core.callbackhandler;

import com.myownb3.dominic.timerecording.core.callbackhandler.impl.ChangedValue;
import com.myownb3.dominic.timerecording.core.work.businessday.TimeSnippet;

public interface TimeSnippedChangedCallbackHandler {

   /**
    * Informs any listener that the {@link TimeSnippet} has changed
    * 
    * @param changeValue
    *        the changed Value
    */
   public void handleTimeSnippedChanged(ChangedValue changeValue);

}
