/**
 * 
 */
package com.myownb3.dominic.ui.app.pages.overview.model;

import com.myownb3.dominic.librarys.text.res.TextLabel;
import com.myownb3.dominic.timerecording.work.businessday.ext.BusinessDay4Export;
import com.myownb3.dominic.ui.core.model.PageModel;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * @author Dominic
 *
 */
public class OverviewPageModel implements PageModel {

    private BusinessDay4Export businessDay4Export;
    private Property<Boolean> isChargeButtonDisabled;
    private Property<Boolean> isClearButtonDisabled;
    private Property<Boolean> isExportButtonDisabled;

    private StringProperty bookButtonLabel;
    private StringProperty exportButtonLabel;
    private StringProperty clearButtonLabel;

    private StringProperty totalAmountOfTimeLabel;
    private StringProperty totalAmountOfTimeValue;

    /**
     * Creates a new {@link OverviewPageModel}
     */
    public OverviewPageModel(BusinessDay4Export businessDay4Export) {
	this.businessDay4Export = businessDay4Export;
	isChargeButtonDisabled = new SimpleBooleanProperty(businessDay4Export.hasNotChargedElements());
	isClearButtonDisabled = new SimpleBooleanProperty(businessDay4Export.hasNotChargedElements());
	isExportButtonDisabled = new SimpleBooleanProperty(businessDay4Export.hasNotChargedElements());

	bookButtonLabel = new SimpleStringProperty(TextLabel.CHARGE_LABEL);
	exportButtonLabel = new SimpleStringProperty(TextLabel.EXPORT_LABEL);
	clearButtonLabel = new SimpleStringProperty(TextLabel.CLEAR_LABEL);

	totalAmountOfTimeLabel = new SimpleStringProperty(TextLabel.TOTAL_AMOUNT_OF_HOURS_LABEL);
	totalAmountOfTimeValue = new SimpleStringProperty(businessDay4Export.getTotalDurationRep());
    }

    /**
     * @param inPageModel
     * @param of
     * @return
     */
    public static OverviewPageModel of(OverviewPageModel inPageModel, BusinessDay4Export businessDay4Export) {
	inPageModel.businessDay4Export = businessDay4Export;
	inPageModel.getIsChargeButtonDisabled().setValue(!businessDay4Export.hasNotChargedElements());
	inPageModel.getIsClearButtonDisabled().setValue(businessDay4Export.getBusinessDayIncrements().isEmpty());
	inPageModel.getIsExportButtonDisabled().setValue(businessDay4Export.getBusinessDayIncrements().isEmpty());

	inPageModel.getBookButtonLabel().set(TextLabel.CHARGE_LABEL);
	inPageModel.getExportButtonLabel().set(TextLabel.EXPORT_LABEL);
	inPageModel.getClearButtonLabel().set(TextLabel.CLEAR_LABEL);

	inPageModel.getTotalAmountOfTimeLabel().set(TextLabel.TOTAL_AMOUNT_OF_HOURS_LABEL);
	
	String totalDurationRep = businessDay4Export.getTotalDurationRep();
	if (businessDay4Export.getBusinessDayIncrements().isEmpty()) {
	    totalDurationRep = "0h (schaff endli emol!)";
	}
	inPageModel.getTotalAmountOfTimeValue().set(totalDurationRep);
	return inPageModel;
    }

    /**
     * @returns the {@link BusinessDay4Export}
     */
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

    public final StringProperty getBookButtonLabel() {
	return this.bookButtonLabel;
    }

    public final StringProperty getExportButtonLabel() {
	return this.exportButtonLabel;
    }

    public final StringProperty getClearButtonLabel() {
	return this.clearButtonLabel;
    }

    public final StringProperty getTotalAmountOfTimeLabel() {
	return this.totalAmountOfTimeLabel;
    }

    public final StringProperty getTotalAmountOfTimeValue() {
	return this.totalAmountOfTimeValue;
    }

    public final Property<Boolean> getIsExportButtonDisabled() {
	return this.isExportButtonDisabled;
    }
}
