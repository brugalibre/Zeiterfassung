/**
 * 
 */
package com.myownb3.dominic.ui.app.pages.stopbusinessday.view;

import com.myownb3.dominic.ui.app.pages.stopbusinessday.control.StopBusinessDayIncrementController;
import com.myownb3.dominic.ui.app.pages.stopbusinessday.model.StopBusinessDayIncrementPageModel;
import com.myownb3.dominic.ui.core.view.impl.AbstractFXSubPage;

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
