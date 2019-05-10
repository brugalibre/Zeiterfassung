/**
 * 
 */
package com.myownb3.dominic.ui.core.pages.overview.model.table;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * @author Dominic
 *
 */
public class BusinessDayIncTableCellValue {

    private StringProperty numberProperty;
    private StringProperty totalDurationProperty;
    private StringProperty ticketNrProperty;
    private StringProperty descriptionProperty;
    private StringProperty chargeTypeProperty;
    private StringProperty isChargedProperty;

    /**
     * Creates a new empty {@link BusinessDayIncTableCellValue}
     */
    public BusinessDayIncTableCellValue() {
	numberProperty = new SimpleStringProperty();
	totalDurationProperty = new SimpleStringProperty();
	ticketNrProperty = new SimpleStringProperty();
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

    /**
     * @param ticketNumber
     */
    public void setTicketNumber(String ticketNumber) {
	ticketNrProperty.set(ticketNumber);
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
