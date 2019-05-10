/**
 * 
 */
package com.myownb3.dominic.timerecording.work.businessday.update;

import com.myownb3.dominic.timerecording.work.businessday.TimeSnippet;

/**
 * @author Dominic
 *
 */
public class BusinessDayIncrementUpdate {

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
