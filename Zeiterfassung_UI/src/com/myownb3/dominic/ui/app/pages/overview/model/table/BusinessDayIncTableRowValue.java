/**
 * 
 */
package com.myownb3.dominic.ui.app.pages.overview.model.table;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.myownb3.dominic.timerecording.work.businessday.ValueTypes;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * @author Dominic
 *
 */
public class BusinessDayIncTableRowValue {

    private StringProperty numberProperty;
    private StringProperty totalDurationProperty;
    private StringProperty ticketNumberProperty;
    private StringProperty descriptionProperty;
    private StringProperty chargeTypeProperty;
    private StringProperty isChargedProperty;

    private List<TimeSnippetCellValue> timeSnippets;
    private Map<Integer, ValueTypes> valueTypesForIndex;

    /**
     * Creates a new empty {@link BusinessDayIncTableRowValue}
     */
    public BusinessDayIncTableRowValue() {
	numberProperty = new SimpleStringProperty();
	totalDurationProperty = new SimpleStringProperty();
	ticketNumberProperty = new SimpleStringProperty();
	descriptionProperty = new SimpleStringProperty();
	chargeTypeProperty = new SimpleStringProperty();
	isChargedProperty = new SimpleStringProperty();
	valueTypesForIndex = new HashMap<>();
	timeSnippets = new ArrayList<>();
    }

    /**
     * @param isDescriptionTitleNecessary
     */
    public void setValueTypes(boolean isDescriptionTitleNecessary) {
	int index = 0;
	valueTypesForIndex.put(index, ValueTypes.NONE);
	index++;
	valueTypesForIndex.put(index, ValueTypes.NONE);
	index++;
	valueTypesForIndex.put(index, ValueTypes.TICKET_NR);
	index++;
	if (isDescriptionTitleNecessary) {
	    valueTypesForIndex.put(index, ValueTypes.DESCRIPTION);
	    index++;
	}
	for (TimeSnippetCellValue timeSnippetCellValue : timeSnippets) {
	    valueTypesForIndex.put(index, timeSnippetCellValue.getValueType());
	    index++;
	}
	valueTypesForIndex.put(index, ValueTypes.CHARGE_TYPE);
	index++;
    }

    /**
     * @param tablePosition
     * @return
     */
    public ValueTypes getChangeValueTypeForIndex(int index) {
	return valueTypesForIndex.get(index);
    }

    /**
     * Returns the {@link TimeSnippetCellValue} for the given index
     * 
     * @param index
     * @return the {@link TimeSnippetCellValue} for the given index
     */
    public TimeSnippetCellValue getTimeSnippet(int index) {
	return timeSnippets.get(index);
    }

    /**
     * Returns the {@link TimeSnippetCellValue} for the given index
     * 
     * @param index
     * @return the {@link TimeSnippetCellValue} for the given index
     */
    public TimeSnippetCellValue getTimeSnippe4RowIndex(int index) {
	ValueTypes valueTypeForIndex = getChangeValueTypeForIndex(3);
	int offset = 3;
	int timeSnippetIndex = index;
	switch (valueTypeForIndex) {
	case DESCRIPTION:
	    offset = 4;
	    break;
	default:
	    break;
	}
	timeSnippetIndex = index - offset;
	if (timeSnippetIndex < timeSnippets.size() && timeSnippetIndex >= 0) {
	    return timeSnippets.get(timeSnippetIndex);
	}
	return null;
    }

    /**
     * @param timeSnippets
     */
    public void setTimeSnippets(List<TimeSnippetCellValue> timeSnippets) {
	this.timeSnippets = timeSnippets;
    }

    public final String getNumber() {
	return this.numberProperty.get();
    }

    public final String getTotalDuration() {
	return this.totalDurationProperty.get();
    }

    public final String getTicketNumber() {
	return this.ticketNumberProperty.get();
    }

    public final String getDescription() {
	return this.descriptionProperty.get();
    }

    public final String getChargeType() {
	return this.chargeTypeProperty.get();
    }
    
    public StringProperty chargeTypeProperty() {
	return chargeTypeProperty;
    }

    public final String getIsCharged() {
	return this.isChargedProperty.get();
    }

    public final void setIsCharged(String isCharged) {
	this.isChargedProperty.set(isCharged);
    }

    public final void setNumber(String number) {
	this.numberProperty.set(number);
    }

    public final void setTotalDuration(String totalDuration) {
	this.totalDurationProperty.set(totalDuration);
    }

    public final void setTicketNumber(String ticketNumber) {
	this.ticketNumberProperty.set(ticketNumber);
    }

    public final void setDescription(String description) {
	this.descriptionProperty.set(description);
    }

    public final void setChargeType(String chargeType) {
	this.chargeTypeProperty.set(chargeType);
    }

    public int getNumberAsInt() {
	return Integer.valueOf(getNumber());
    }
}
