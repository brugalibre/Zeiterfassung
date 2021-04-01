/**
 * 
 */
package com.adcubum.timerecording.ui.app.pages.stopbusinessday.view;

import com.adcubum.timerecording.ui.app.pages.stopbusinessday.control.StopBusinessDayIncrementController;
import com.adcubum.timerecording.ui.app.pages.stopbusinessday.model.StopBusinessDayIncrementPageModel;
import com.adcubum.timerecording.ui.core.view.impl.AbstractFXSubPage;

/**
 * @author Dominic
 *
 */
public class StopBusinessDayIncrementPage
      extends AbstractFXSubPage<StopBusinessDayIncrementPageModel, StopBusinessDayIncrementPageModel> {

   /**
    * @param stopBusinessDayIncrementController
    */
   public StopBusinessDayIncrementPage(StopBusinessDayIncrementController controller) {
      super();
      setController(controller);
   }
}
