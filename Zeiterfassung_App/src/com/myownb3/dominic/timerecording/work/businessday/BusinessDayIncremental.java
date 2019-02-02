/**
 * 
 */
package com.myownb3.dominic.timerecording.work.businessday;

import java.util.ArrayList;
import java.util.List;

import com.myownb3.dominic.timerecording.app.TimeRecorder;
import com.myownb3.dominic.timerecording.work.date.Date;
import com.myownb3.dominic.timerecording.work.date.Time;
import com.myownb3.dominic.timerecording.work.date.TimeType.TIME_TYPE;

/**
 * A {@link BusinessDay} consist of one or more {@link BusinessDayIncremental}.
 * Where as a {@link BusinessDayIncremental} consist of one or more
 * {@link TimeSnippet}. Two different {@link BusinessDayIncremental} are <i> not
 * </i> dependent!
 * 
 * @author Dominic
 */
public class BusinessDayIncremental {
    private List<TimeSnippet> timeSnippets;
    private TimeSnippet currentTimeSnippet;

    private Date date;
    private String description;
    private String ticketNumber;
    private String chargeType;
    private boolean isCharged; // true if this BussinessDayIncremental is
			       // charged and it's data send to the account
			       // staff

    public BusinessDayIncremental(Date date) {
	this.date = date;
	timeSnippets = new ArrayList<TimeSnippet>();
	ticketNumber = "SYRIUS- ";
	description = " - ";
    }

    /**
     * @param beginTimeStamp
     */
    public void startCurrentTimeSnippet(Time beginTimeStamp) {
	createNewTimeSnippet();
	currentTimeSnippet.setBeginTimeStamp(beginTimeStamp);
    }

    /**
     * @param endTimeStamp
     */
    public void stopCurrentTimeSnippet(Time endTimeStamp) {
	currentTimeSnippet.setEndTimeStamp(endTimeStamp);
	timeSnippets.add(currentTimeSnippet);
    }

    public void resmeLastTimeSnippet() {
	currentTimeSnippet.setEndTimeStamp(null);
	timeSnippets.remove(currentTimeSnippet);
    }

    /**
    * 
    */
    private void createNewTimeSnippet() {
	currentTimeSnippet = new TimeSnippet(date);
    }

    public Date getDate() {
	return date;
    }

    public List<TimeSnippet> getTimeSnippets() {
	return timeSnippets;
    }

    public float getTotalDuration() {
	return getTotalDuration(TimeRecorder.GLOBAL_TIME_TYPE);
    }

    /**
     * Calculates the total amount of working minuts of all its increments
     * 
     * @param type
     * @return
     */
    public float getTotalDuration(TIME_TYPE type) {
	float sum = 0;

	for (TimeSnippet timeSnippet : timeSnippets) {
	    sum = sum + timeSnippet.getDuration(type);
	}
	return Float.parseFloat(TimeRecorder.formater.format(sum));
    }

    public boolean isCharged() {
	return isCharged;
    }

    public void setCharged(boolean isCharged) {
	this.isCharged = isCharged;
    }

    public TimeSnippet getCurrentTimeSnippet() {
	return currentTimeSnippet;
    }

    public void setCurrentTimeSnippet(TimeSnippet currentTimeSnippet) {
	this.currentTimeSnippet = currentTimeSnippet;
    }

    public String getDescription() {
	return description;
    }

    public void setDescription(String description) {
	this.description = description;
    }

    public String getTicketNumber() {
	return ticketNumber;
    }

    public void setTicketNumber(String ticketNumber) {
	this.ticketNumber = ticketNumber;
    }

    public String getChargeType() {
	return chargeType;
    }

    public void setChargeType(String chargeType) {
	this.chargeType = chargeType;
    }

    /**
     * Moves all {@link TimeSnippet} of the current {@link BusinessDayIncremental}
     * to the given {@link BusinessDayIncremental}. The {@link TimeSnippet}s of the
     * current {@link BusinessDayIncremental} are removed!
     * 
     * @param incrementToAddTimeSnippets
     */
    public void transferAllTimeSnipetsToBussinessDayIncrement(BusinessDayIncremental incrementToAddTimeSnippets) {
	incrementToAddTimeSnippets.getTimeSnippets().addAll(timeSnippets);
	timeSnippets.clear();
    }
}
