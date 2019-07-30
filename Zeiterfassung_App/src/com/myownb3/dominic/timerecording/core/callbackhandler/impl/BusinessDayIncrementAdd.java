/**
 * 
 */
package com.myownb3.dominic.timerecording.core.callbackhandler.impl;

import com.myownb3.dominic.timerecording.core.work.businessday.BusinessDayIncrement;
import com.myownb3.dominic.timerecording.core.work.businessday.TimeSnippet;

/**
 * 
 * The {@link BusinessDayIncrementAdd} is used whenever a new
 * {@link BusinessDayIncrement} is added
 * 
 * @author Dominic
 *
 */
public class BusinessDayIncrementAdd {

    private String ticketNo;
    private String description;
    private String amountOfHours;
    private int kindOfService;
    private TimeSnippet timeSnippet;

    public final String getTicketNo() {
	return this.ticketNo;
    }

    public final String getDescription() {
	return this.description;
    }

    public final String getAmountOfHours() {
	return this.amountOfHours;
    }

    public final int getKindOfService() {
	return this.kindOfService;
    }

    public final void setTicketNo(String ticketNo) {
	this.ticketNo = ticketNo;
    }

    public final void setDescription(String description) {
	this.description = description;
    }

    public final TimeSnippet getTimeSnippet() {
	return this.timeSnippet;
    }

    public final void setTimeSnippet(TimeSnippet timeSnippet) {
	this.timeSnippet = timeSnippet;
    }

    public final void setAmountOfHours(String amountOfHours) {
	this.amountOfHours = amountOfHours;
    }

    public final void setKindOfService(int kindOfService) {
	this.kindOfService = kindOfService;
    }
}
