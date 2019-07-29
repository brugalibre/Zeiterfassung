/**
 * 
 */
package com.myownb3.dominic.timerecording.callback.handler.impl;

import java.util.ArrayList;
import java.util.List;

import com.myownb3.dominic.timerecording.work.businessday.BusinessDay;
import com.myownb3.dominic.timerecording.work.businessday.BusinessDayIncrement;
import com.myownb3.dominic.timerecording.work.businessday.TimeSnippet;

/**
 * 
 * The {@link BusinessDayIncrementImport} is used whenever a {@link BusinessDay}
 * and also a new {@link BusinessDayIncrement} is imported
 * 
 * @author Dominic
 *
 */
public class BusinessDayIncrementImport {

    private String ticketNo;
    private String description;
    private String amountOfHours;
    private int kindOfService;
    private List<TimeSnippet> timeSnippets;

    public BusinessDayIncrementImport() {
	timeSnippets = new ArrayList<TimeSnippet>();
    }

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

    public List<TimeSnippet> getTimeSnippets() {
	return timeSnippets;
    }

    public void setTimeSnippets(List<TimeSnippet> timeSnippets) {
	this.timeSnippets = timeSnippets;
    }

    public final void setAmountOfHours(String amountOfHours) {
	this.amountOfHours = amountOfHours;
    }

    public final void setKindOfService(int kindOfService) {
	this.kindOfService = kindOfService;
    }
}
