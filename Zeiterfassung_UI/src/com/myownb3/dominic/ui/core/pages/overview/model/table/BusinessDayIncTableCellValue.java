/**
 * 
 */
package com.myownb3.dominic.ui.core.pages.overview.model.table;


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

    private List<TimeSnippetCellValue> beginEndValues;
    
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
    }

    /**
     * @param no
     */
    public void setNumber(int no) {
	numberProperty.set(String.valueOf(no));
    }

    /**
     * @param cellValue
     */
    public void setDescritpion(String description) {
	descriptionProperty.set(description);
    }

    /**
     * @param totalDurationRep
     */
    public void setTotalDuration(String totalDurationRep) {
	totalDurationProperty.set(totalDurationRep);
    }

    public String getNumber() {
        return numberProperty.get();
    }

    public String getTotalDuration() {
        return totalDurationProperty.get();
    }

    public String getTicketNumber() {
        return ticketNumberProperty.get();
    }

    public String getDescription() {
        return descriptionProperty.get();
    }

    public String getChargeType() {
        return chargeTypeProperty.get();
    }

    public String getIsCharged() {
        return isChargedProperty.get();
    }

    /**
     * @param ticketNumber
     */
    public void setTicketNumber(String ticketNumber) {
	ticketNumberProperty.set(ticketNumber);
    }

    /**
     * @param representation
     */
    public void setChargeType(String representation) {
	chargeTypeProperty.set(representation);
    }

    /**
     * @param object
     */
    public void setIsCharged(String isCharged) {
	isChargedProperty.set(isCharged);
    }
}
