/**
 * 
 */
package com.myownb3.dominic.timerecording.work.businessday;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import com.myownb3.dominic.timerecording.app.TimeRecorder;
import com.myownb3.dominic.timerecording.work.businessday.update.BusinessDayIncrementUpdate;
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
	ticketNumber = "SYRIUS-";
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
	Collections.sort(timeSnippets, new TimeStampComparator());
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
	return NumberFormat.parseFloat(NumberFormat.format(sum));
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
	incrementToAddTimeSnippets.addTimeSnippets(timeSnippets);
	timeSnippets.clear();
    }

    private void addTimeSnippets(List<TimeSnippet> newTimeSnippets) {
	timeSnippets.addAll(newTimeSnippets);
	Collections.sort(timeSnippets, new TimeStampComparator());
    }

    public boolean isCharged() {
	return isCharged;
    }

    /**
     * Returns the {@link TimeSnippet} at the given positions or <code>null</code>
     * if there isn't any at this location
     */
    private Optional<TimeSnippet> getTimeSnippet4Index(int fromUptoSequence) {
	TimeSnippet timeSnippet = null;
	for (int i = 0; i < timeSnippets.size(); i++) {
	    if (i == fromUptoSequence) {
		timeSnippet = timeSnippets.get(i);
	    }
	}
	return Optional.ofNullable(timeSnippet);
    }

    /**
     * Updates the {@link TimeSnippet} at the given index and recalulates the entire
     * {@link BusinessDay}
     * 
     * @param newTimeStampValue
     *            the new value for the time stamp
     */
    public void updateBeginTimeSnippetAndCalculate(BusinessDayIncremental businessDayIncremental, int fromUptoSequence,
	    String newTimeStampValue) {

	Optional<TimeSnippet> timeSnippetOpt = getTimeSnippet4Index(fromUptoSequence);
	timeSnippetOpt.ifPresent(timeSnippet -> {
	    timeSnippet.updateAndSetBeginTimeStamp(newTimeStampValue);
	});
    }

    /**
     * Updates the {@link TimeSnippet} at the given index and recalulates the entire
     * {@link BusinessDay}
     * 
     * @param newTimeStampValue
     *            the new value for the time stamp
     */
    public void updateEndTimeSnippetAndCalculate(BusinessDayIncremental businessDayIncremental, int fromUptoSequence,
	    String newTimeStampValue) {
	Optional<TimeSnippet> timeSnippetOpt = getTimeSnippet4Index(fromUptoSequence);
	timeSnippetOpt.ifPresent(timeSnippet -> {
	    timeSnippet.updateAndSetEndTimeStamp(newTimeStampValue);
	});
    }

    public static class TimeStampComparator implements Comparator<TimeSnippet> {
	@Override
	public int compare(TimeSnippet timeSnippet, TimeSnippet timeSnippet2) {
	    Time beginTimeStamp1 = timeSnippet.getBeginTimeStamp();
	    Time beginTimeStamp2 = timeSnippet2.getBeginTimeStamp();
	    return beginTimeStamp1.compareTo(beginTimeStamp2);
	}
    }

    /**
     * Creates a new {@link BusinessDayIncremental} for the given
     * {@link BusinessDayIncrementUpdate}
     * 
     * @param update
     *            the {@link BusinessDayIncrementUpdate} with the new values
     * @return a new {@link BusinessDayIncremental}
     */
    public static BusinessDayIncremental of(BusinessDayIncrementUpdate update) {

	BusinessDayIncremental businessDayIncremental = new BusinessDayIncremental(update.getTimeSnippet().getDate());
	businessDayIncremental.description = update.getDescription();
	businessDayIncremental.ticketNumber = update.getTicketNo();
	businessDayIncremental.chargeType = update.getKindOfService();
	businessDayIncremental.startCurrentTimeSnippet(update.getTimeSnippet().getBeginTimeStamp());
	businessDayIncremental.stopCurrentTimeSnippet(update.getTimeSnippet().getEndTimeStamp());
	return businessDayIncremental;
    }
}
