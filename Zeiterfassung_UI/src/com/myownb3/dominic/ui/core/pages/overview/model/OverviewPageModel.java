/**
 * 
 */
package com.myownb3.dominic.ui.core.pages.overview.model;

import com.myownb3.dominic.timerecording.app.TimeRecorder;
import com.myownb3.dominic.timerecording.work.businessday.ext.BusinessDay4Export;
import com.myownb3.dominic.ui.core.model.PageModel;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleBooleanProperty;

/**
 * @author Dominic
 *
 */
public class OverviewPageModel implements PageModel {

    private BusinessDay4Export businessDay4Export;
    private Property<Boolean> isChargeButtonDisabled;

    /**
     * Creates a new {@link OverviewPageModel}
     */
    public OverviewPageModel(BusinessDay4Export businessDay4Export) {
	this.businessDay4Export = businessDay4Export;
	isChargeButtonDisabled = new SimpleBooleanProperty(!TimeRecorder.hasNotChargedElements());
    }

    public final BusinessDay4Export getBusinessDay4Export() {
	return this.businessDay4Export;
    }

    /**
     * @return
     */
    public Property<Boolean> isChargeButtonDisabled() {
	return isChargeButtonDisabled;
    }
}
