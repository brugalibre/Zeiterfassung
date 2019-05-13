/**
 * 
 */
package com.myownb3.dominic.ui.app.pages.stopbusinessday.view;

import java.net.URL;

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
     * @param url
     */
    public StopBusinessDayIncrementPage(StopBusinessDayIncrementController controller, URL url) {
	super();
	setController(controller);
    }
}
