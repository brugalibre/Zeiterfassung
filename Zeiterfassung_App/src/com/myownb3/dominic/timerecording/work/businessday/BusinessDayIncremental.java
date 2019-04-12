/**
 * 
 */
package com.myownb3.dominic.timerecording.work.businessday;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.myownb3.dominic.timerecording.app.TimeRecorder;
import com.myownb3.dominic.timerecording.work.date.Time;
import com.myownb3.dominic.timerecording.work.date.TimeType.TIME_TYPE;
import com.myownb3.dominic.util.parser.NumberFormat;
import com.myownb3.dominic.util.utils.StringUtil;

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
    private int chargeType;
    private boolean isCharged;

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

    public void resumeLastTimeSnippet() {
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
     * Returns <code>true</code> if this {@link BusinessDayIncremental} and the
     * other has the same Ticketnumber, and same charge-type and the same
     * description. The description can be <code>null</code> for both
     * {@link BusinessDayIncremental} and this method still returns
     * <code>true</code>
     * 
     * @param other
     *            the other BusinessDayIncremental
     * @return <code>true</code> if this {@link BusinessDayIncremental} is the same
     *         as the other one
     */
    public boolean isSame(BusinessDayIncremental other) {
	if (other == null) {
	    return false;
	}
	if (this == other) {
	    return true;
	}
	return this.getTicketNumber().equals(other.getTicketNumber()) && this.getChargeType() == other.getChargeType()//
		&& this.isCharged == other.isCharged() && hasSameDescription(other);
    }

    private boolean hasSameDescription(BusinessDayIncremental other) {
	return StringUtil.isEmptyOrNull(other.getDescription()) && StringUtil.isEmptyOrNull(this.getDescription())
		|| (StringUtil.isNotEmptyOrNull(other.getDescription())
			&& StringUtil.isNotEmptyOrNull(other.getDescription())
			&& other.getDescription().equals(this.getDescription()));
    }

    public void flagAsCharged() {
	this.isCharged = true;
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
	return NumberFormat.parseFloat(TimeRecorder.formater.format(sum));
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

    public int getChargeType() {
	return chargeType;
    }

    public void setChargeType(int chargeType) {
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

    public boolean isCharged() {
	return isCharged;
    }
}
