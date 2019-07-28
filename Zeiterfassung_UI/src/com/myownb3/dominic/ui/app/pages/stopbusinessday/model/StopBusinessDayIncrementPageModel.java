/**
 * 
 */
package com.myownb3.dominic.ui.app.pages.stopbusinessday.model;

import static com.myownb3.dominic.ui.app.pages.stopbusinessday.util.StopBusinessDayUtil.areMultipleTicketsEntered;

import java.util.Date;

import com.myownb3.dominic.librarys.text.res.TextLabel;
import com.myownb3.dominic.timerecording.callback.handler.TimeSnippedChangedCallbackHandler;
import com.myownb3.dominic.timerecording.callback.handler.impl.BusinessDayChangedCallbackHandlerImpl;
import com.myownb3.dominic.timerecording.callback.handler.impl.BusinessDayIncrementAdd;
import com.myownb3.dominic.timerecording.callback.handler.impl.ChangedValue;
import com.myownb3.dominic.timerecording.charge.ChargeType;
import com.myownb3.dominic.timerecording.work.businessday.BusinessDayIncrement;
import com.myownb3.dominic.timerecording.work.businessday.TimeSnippet;
import com.myownb3.dominic.timerecording.work.businessday.ext.BusinessDayInc4Export;
import com.myownb3.dominic.timerecording.work.date.Time;
import com.myownb3.dominic.ui.core.model.PageModel;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Tooltip;

/**
 * @author Dominic
 *
 */
public class StopBusinessDayIncrementPageModel implements PageModel, TimeSnippedChangedCallbackHandler {

    private Property<String> ticketNoProperty;
    private Property<String> descriptionProperty;
    private StringProperty beginTextFieldProperty;
    private StringProperty endTextFieldProperty;
    private StringProperty amountOfHoursTextFieldProperty;
    private Property<ObservableList<String>> kindOfServiceTextFieldProperty;

    private StringProperty ticketNoLabelProperty;
    private StringProperty descriptionLabelProperty;
    private StringProperty beginLabelProperty;
    private StringProperty endLabelProperty;
    private StringProperty amountOfHoursLabelProperty;
    private StringProperty kindOfServiceLabelProperty;

    private StringProperty finishButtonText;
    private StringProperty abortButtonText;
    private StringProperty cancelButtonText;
    private ObservableValue<? extends Tooltip> abortButtonToolTipText;
    private ObservableValue<? extends Tooltip> cancelButtonToolTipText;
    public TimeSnippet timeSnippet;

    /**
     * Creates a new {@link StopBusinessDayIncrementPageModel}
     */
    public StopBusinessDayIncrementPageModel(BusinessDayInc4Export businessDayInc4Export) {

	ticketNoLabelProperty = new SimpleStringProperty(TextLabel.TICKET_NUMBER_LABEL);
	descriptionLabelProperty = new SimpleStringProperty(TextLabel.DESCRIPTION_LABEL);
	beginLabelProperty = new SimpleStringProperty(TextLabel.VON_LABEL);
	endLabelProperty = new SimpleStringProperty(TextLabel.BIS_LABEL);
	amountOfHoursLabelProperty = new SimpleStringProperty(TextLabel.AMOUNT_OF_HOURS_LABEL);
	kindOfServiceLabelProperty = new SimpleStringProperty(TextLabel.CHARGE_TYPE_LABEL);
	finishButtonText = new SimpleStringProperty(TextLabel.FINISH_BUTTON_TEXT);
	abortButtonText = new SimpleStringProperty(TextLabel.ABORT_BUTTON_TEXT);
	cancelButtonText = new SimpleStringProperty(TextLabel.CANCEL_BUTTON_TEXT);
	abortButtonToolTipText = new SimpleObjectProperty<Tooltip>(new Tooltip(TextLabel.ABORT_BUTTON_TOOLTIP_TEXT));
	cancelButtonToolTipText = new SimpleObjectProperty<Tooltip>(new Tooltip(TextLabel.CANCEL_BUTTON_TOOLTIP_TEXT));

	amountOfHoursTextFieldProperty = new SimpleStringProperty(businessDayInc4Export.getTotalDurationRep());
	ticketNoProperty = new SimpleObjectProperty<String>(businessDayInc4Export.getTicketNumber());
	descriptionProperty = new SimpleObjectProperty<String>(businessDayInc4Export.getDescription());

	kindOfServiceTextFieldProperty = new SimpleListProperty<>(
		FXCollections.observableArrayList(ChargeType.getLeistungsartenRepresentation()));

	this.timeSnippet = businessDayInc4Export.getCurrentTimeSnippet();
	beginTextFieldProperty = new SimpleStringProperty(
		timeSnippet != null ? timeSnippet.getBeginTimeStampRep() : "");
	endTextFieldProperty = new SimpleStringProperty(timeSnippet != null ? timeSnippet.getEndTimeStampRep() : "");
    }

    public BusinessDayIncrement map() {
	return new BusinessDayIncrement(null);
    }

    /**
     * Trys to parse a new {@link Date} from the given timestamp value and sets this
     * value as new begin-time stamp
     * 
     * @param newTimeStampValue the new begin-time-stamp as String
     */
    public void updateAndSetBeginTimeStamp(String newTimeStampValue) {
	timeSnippet.updateAndSetBeginTimeStamp(newTimeStampValue);
    }

    /**
     * Trys to parse a new {@link Date} from the given timestamp value and sets this
     * value as new end-time stamp
     * 
     * @param newTimeStampValue the new begin-time-stamp as String
     */
    public void updateAndSetEndTimeStamp(String newTimeStampValue) {
	timeSnippet.updateAndSetEndTimeStamp(newTimeStampValue);
    }

    /**
     * Trys to parse the given end time stamp (as String) and summs this value up to
     * the given begin Time Stamp
     * 
     * @param newEndAsString the new End-time stamp as String
     * @throws NumberFormatException if there goes anything wrong while parsing
     */
    public void addAdditionallyTime(String newEndAsString) {
	timeSnippet.addAdditionallyTime(newEndAsString);
    }

    /**
     * Updates the value of the given {@link StopBusinessDayIncrementPageModel}
     * 
     * @param inPageModel the given {@link StopBusinessDayIncrementPageModel}
     * @return the given {@link StopBusinessDayIncrementPageModel}
     */
    public static StopBusinessDayIncrementPageModel of(StopBusinessDayIncrementPageModel inPageModel,
	    BusinessDayInc4Export businessDayInc4Export) {

	TimeSnippet timeSnippet = businessDayInc4Export.getCurrentTimeSnippet();
	inPageModel.timeSnippet = timeSnippet;
	inPageModel.getTicketNoLabelProperty().set(TextLabel.TICKET_NUMBER_LABEL);
	inPageModel.getDescriptionLabelProperty().set(TextLabel.DESCRIPTION_LABEL);
	inPageModel.getBeginLabelProperty().set(TextLabel.VON_LABEL);
	inPageModel.getEndLabelProperty().set(TextLabel.BIS_LABEL);
	inPageModel.getAmountOfHoursLabelProperty().set(TextLabel.AMOUNT_OF_HOURS_LABEL);
	inPageModel.getKindOfServiceLabelProperty().set(TextLabel.CHARGE_TYPE_LABEL);
	inPageModel.getFinishButtonText().set(TextLabel.FINISH_BUTTON_TEXT);
	inPageModel.getAbortButtonText().set(TextLabel.ABORT_BUTTON_TEXT);
	inPageModel.getCancelButtonText().set(TextLabel.CANCEL_BUTTON_TEXT);

	inPageModel.abortButtonToolTipText = new SimpleObjectProperty<Tooltip>(
		new Tooltip(TextLabel.ABORT_BUTTON_TOOLTIP_TEXT));
	inPageModel.cancelButtonToolTipText = new SimpleObjectProperty<Tooltip>(
		new Tooltip(TextLabel.CANCEL_BUTTON_TOOLTIP_TEXT));

	inPageModel.getAmountOfHoursTextFieldProperty().set(businessDayInc4Export.getTotalDurationRep());
	inPageModel.getTicketNoProperty().setValue(businessDayInc4Export.getTicketNumber());
	inPageModel.getDescriptionProperty().setValue(businessDayInc4Export.getDescription());

	inPageModel.getKindOfServiceTextFieldProperty()
		.setValue(FXCollections.observableArrayList(ChargeType.getLeistungsartenRepresentation()));

	timeSnippet.setCallbackHandler(inPageModel);
	inPageModel.getBeginTextFieldProperty().set(timeSnippet.getBeginTimeStampRep());
	inPageModel.getEndTextFieldProperty().set(timeSnippet.getEndTimeStampRep());
	return inPageModel;
    }

    @Override
    public void handleTimeSnippedChanged(ChangedValue changeValue) {

	switch (changeValue.getValueTypes()) {

	case BEGIN:
	    amountOfHoursTextFieldProperty.set(timeSnippet.getDurationRep());
	    beginTextFieldProperty.set(timeSnippet.getBeginTimeStampRep());
	    break;
	case END:
	    amountOfHoursTextFieldProperty.set(timeSnippet.getDurationRep());
	    endTextFieldProperty.set(timeSnippet.getEndTimeStampRep());
	    break;
	case AMOUNT_OF_TIME:
	    endTextFieldProperty.set(timeSnippet.getEndTimeStampRep());
	    break;
	default:
	    // ignore
	    break;
	}
    }

    /**
     * Adds the recorded informations as new {@link BusinessDayIncrement}
     * 
     * @param kindOfService the kind of service
     */
    public void addIncrement2BusinessDay(int kindOfService) {

	String ticketNoPropValue = ticketNoProperty.getValue();
	boolean multipleTicketsEntered = areMultipleTicketsEntered(ticketNoPropValue);
	if (multipleTicketsEntered) {
	    addMultipleaIncrement2BusinessDay(kindOfService, ticketNoPropValue);
	} else {
	    addIncrement2BusinessDayInternal(kindOfService, ticketNoProperty.getValue(), timeSnippet);
	}
    }

    private void addMultipleaIncrement2BusinessDay(int kindOfService, String ticketNoPropValue) {

	String[] tickets = ticketNoPropValue.split(";");
	Time currentBeginTimeStamp = timeSnippet.getBeginTimeStamp();

	for (String ticket : tickets) {

	    TimeSnippet currentTimeSnippet = timeSnippet.createTimeStampForIncrement(currentBeginTimeStamp,
		    tickets.length);
	    addIncrement2BusinessDayInternal(kindOfService, ticket, currentTimeSnippet);

	    currentBeginTimeStamp = currentTimeSnippet.getEndTimeStamp();
	}
    }

    private void addIncrement2BusinessDayInternal(int kindOfService, String ticketNr, TimeSnippet timeSnippet) {
	BusinessDayIncrementAdd update = new BusinessDayIncrementAdd();
	update.setTimeSnippet(timeSnippet);
	update.setDescription(descriptionProperty.getValue());
	update.setTicketNo(ticketNr);
	update.setKindOfService(kindOfService);

	new BusinessDayChangedCallbackHandlerImpl().handleBusinessDayIncrementAdd(update);
    }

    public final Property<String> getTicketNoProperty() {
	return this.ticketNoProperty;
    }

    public final Property<String> getDescriptionProperty() {
	return this.descriptionProperty;
    }

    public final StringProperty getBeginTextFieldProperty() {
	return this.beginTextFieldProperty;
    }

    public final StringProperty getEndTextFieldProperty() {
	return this.endTextFieldProperty;
    }

    public final Property<ObservableList<String>> getKindOfServiceTextFieldProperty() {
	return this.kindOfServiceTextFieldProperty;
    }

    public final StringProperty getTicketNoLabelProperty() {
	return this.ticketNoLabelProperty;
    }

    public final StringProperty getDescriptionLabelProperty() {
	return this.descriptionLabelProperty;
    }

    public final StringProperty getBeginLabelProperty() {
	return this.beginLabelProperty;
    }

    public final StringProperty getEndLabelProperty() {
	return this.endLabelProperty;
    }

    public final StringProperty getKindOfServiceLabelProperty() {
	return this.kindOfServiceLabelProperty;
    }

    public final StringProperty getAmountOfHoursTextFieldProperty() {
	return this.amountOfHoursTextFieldProperty;
    }

    public final StringProperty getAmountOfHoursLabelProperty() {
	return this.amountOfHoursLabelProperty;
    }

    public final StringProperty getFinishButtonText() {
	return this.finishButtonText;
    }

    public final StringProperty getAbortButtonText() {
	return this.abortButtonText;
    }

    public final StringProperty getCancelButtonText() {
	return this.cancelButtonText;
    }

    public ObservableValue<? extends Tooltip> getAbortButtonToolTipText() {
	return abortButtonToolTipText;
    }

    public ObservableValue<? extends Tooltip> getCancelButtonToolTipText() {
	return cancelButtonToolTipText;
    }
}
