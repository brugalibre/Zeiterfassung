/**
 * 
 */
package com.myownb3.dominic.ui.app.pages.overview.view;

import java.net.URL;

import com.myownb3.dominic.ui.app.pages.overview.control.OverviewController;
import com.myownb3.dominic.ui.app.pages.overview.model.OverviewPageModel;
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
