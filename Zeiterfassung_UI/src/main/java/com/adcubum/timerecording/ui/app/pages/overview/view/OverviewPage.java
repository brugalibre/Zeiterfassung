/**
 * 
 */
package com.adcubum.timerecording.ui.app.pages.overview.view;

import com.adcubum.timerecording.ui.app.pages.overview.control.OverviewController;
import com.adcubum.timerecording.ui.app.pages.overview.model.OverviewPageModel;
import com.adcubum.timerecording.ui.core.view.impl.AbstractFXSubPage;

/**
 * @author Dominic
 *
 */
public class OverviewPage extends AbstractFXSubPage<OverviewPageModel, OverviewPageModel> {

   /**
    * @param controller
    *        the {@link OverviewController} of this page
    */
   public OverviewPage(OverviewController controller) {
      super();
      setController(controller);
   }
}
