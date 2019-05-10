/**
 * 
 */
package com.myownb3.dominic.ui.core.pages.overview.view;

import java.net.URL;

import com.myownb3.dominic.ui.core.pages.overview.control.OverviewController;
import com.myownb3.dominic.ui.core.pages.overview.model.OverviewPageModel;
import com.myownb3.dominic.ui.core.view.impl.AbstractFXSubPage;

/**
 * @author Dominic
 *
 */
public class OverviewPage extends AbstractFXSubPage<OverviewPageModel, OverviewPageModel> {

    /**
     * @param overviewController
     * @param arg0
     */
    public OverviewPage(OverviewController controller, URL url) {
	super();
	setController(controller);
    }
}
