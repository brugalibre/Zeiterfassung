/**
 * 
 */
package com.myownb3.dominic.ui.core.pages.overview.model.table;

import java.util.ArrayList;
import java.util.List;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * @author Dominic
 *
 */
public class BusinessDayIncTableCellValue {

    private StringProperty numberProperty;
    private StringProperty totalDurationProperty;
    private StringProperty ticketNumberProperty;
    private StringProperty descriptionProperty;
    private StringProperty chargeTypeProperty;
    private StringProperty isChargedProperty;

    private List<TimeSnippetCellValue> timeSnippets;

    /**
     * Creates a new empty {@link BusinessDayIncTableCellValue}
     */
    public BusinessDayIncTableCellValue() {
	numberProperty = new SimpleStringProperty();
	totalDurationProperty = new SimpleStringProperty();
	ticketNumberProperty = new SimpleStringProperty();
	descriptionProperty = new SimpleStringProperty();
	chargeTypeProperty = new SimpleStringProperty();
	isChargedProperty = new SimpleStringProperty();
	timeSnippets = new ArrayList<>();
    }

    /**
     * Returns the {@link TimeSnippetCellValue} for the given index
     * 
     * @param index
     * @return the {@link TimeSnippetCellValue} for the given index
     */
    public TimeSnippetCellValue getTimeSnippet4Index(int index) {
	return timeSnippets.get(index);
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
}
