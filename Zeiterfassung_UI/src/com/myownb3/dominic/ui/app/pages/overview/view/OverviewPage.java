/**
 * 
 */
package com.myownb3.dominic.ui.app.pages.overview.view;

import com.myownb3.dominic.ui.app.pages.overview.control.OverviewController;
import com.myownb3.dominic.ui.app.pages.overview.model.OverviewPageModel;
import com.myownb3.dominic.ui.core.view.impl.AbstractFXSubPage;

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
