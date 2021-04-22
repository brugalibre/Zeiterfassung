/**
 * 
 */
package com.adcubum.timerecording.ui.app.pages.comeandgo.view;

import com.adcubum.timerecording.ui.app.pages.comeandgo.control.ComeAndGoOverviewController;
import com.adcubum.timerecording.ui.app.pages.comeandgo.model.ComeAndGoOverviewPageModel;
import com.adcubum.timerecording.ui.core.model.PageModel;
import com.adcubum.timerecording.ui.core.view.impl.AbstractFXSubPage;

/**
 * @author Dominic
 *
 */
public class ComeAndGoOverviewPage extends AbstractFXSubPage<PageModel, ComeAndGoOverviewPageModel> {
   /**
    * @param stopBusinessDayIncrementController
    */
   public ComeAndGoOverviewPage(ComeAndGoOverviewController controller) {
      super();
      setController(controller);
   }
}
