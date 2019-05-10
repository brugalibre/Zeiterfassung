/**
 * 
 */
package com.myownb3.dominic.ui.core.pages.userinput.view;

import java.net.URL;

import com.myownb3.dominic.ui.core.pages.userinput.control.StopBusinessDayIncrementController;
import com.myownb3.dominic.ui.core.pages.userinput.model.StopBusinessDayIncrementPageModel;
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
