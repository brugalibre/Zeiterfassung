/**
 * 
 */
package com.adcubum.timerecording.ui.app.pages.overview.control.callback;

import com.adcubum.timerecording.ui.app.pages.overview.control.descriptionchange.DescriptionAddHelper;
import com.adcubum.timerecording.ui.app.pages.stopbusinessday.control.FinishAction;

/**
 * The {@link BDChangeCallbackHandler} is like a callback handler in order to refresh the UI
 * 
 * @author Dominic
 *
 */
@FunctionalInterface
public interface BDChangeCallbackHandler {

   /**
    * Does further action on the caller side after the
    * {@link DescriptionAddHelper} or the {@link BusinessDayChangeHelper} has
    * done their job The given {@link FinishAction} defines weather the process
    * was canceled or finished successfully
    * 
    * @param finishAction
    *        the finish action
    */
   public void onFinish(FinishAction finishAction);
}
