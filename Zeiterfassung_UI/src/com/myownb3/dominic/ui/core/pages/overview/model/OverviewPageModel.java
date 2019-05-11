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
    private Property<Boolean> isClearButtonDisabled;

    /**
     * Creates a new {@link OverviewPageModel}
     */
    public OverviewPageModel(BusinessDay4Export businessDay4Export) {
	this.businessDay4Export = businessDay4Export;
	isChargeButtonDisabled = new SimpleBooleanProperty(!TimeRecorder.hasNotChargedElements());
	isClearButtonDisabled = new SimpleBooleanProperty(isChargeButtonDisabled.getValue());
    }

    public final BusinessDay4Export getBusinessDay4Export() {
	return this.businessDay4Export;
    }

    /**
     * @return
     */
    public Property<Boolean> getIsChargeButtonDisabled() {
	return isChargeButtonDisabled;
    }

    public Property<Boolean> getIsClearButtonDisabled() {
	return isClearButtonDisabled;
    }
}
