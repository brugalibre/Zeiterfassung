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
import com.myownb3.dominic.timerecording.callback.handler.impl.BusinessDayIncrementAdd;
import com.myownb3.dominic.timerecording.charge.ChargeType;
import com.myownb3.dominic.timerecording.charge.InvalidChargeTypeRepresentationException;
import com.myownb3.dominic.timerecording.work.date.Time;
import com.myownb3.dominic.timerecording.work.date.TimeType.TIME_TYPE;
import com.myownb3.dominic.util.parser.NumberFormat;
import com.myownb3.dominic.util.utils.StringUtil;

/**
 * A {@link BusinessDay} consist of one or more {@link BusinessDayIncrement}.
 * Where as a {@link BusinessDayIncrement} consist of one or more
 * {@link TimeSnippet}. Two different {@link BusinessDayIncrement} are <i> not
 * </i> dependent!
 * 
 * @author Dominic
 */
public class BusinessDayIncrement {
    private List<TimeSnippet> timeSnippets;
    private TimeSnippet currentTimeSnippet;

    private Date date;
    private String description;
    private String ticketNumber;
    private int chargeType;
    private boolean isCharged;

    public BusinessDayIncrement(Date date) {
	this.date = date;
	timeSnippets = new ArrayList<TimeSnippet>();
	ticketNumber = "SYRIUS-";
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
     * Returns <code>true</code> if this {@link BusinessDayIncrement} and the other
     * has the same Ticketnumber, and same charge-type and the same description. The
     * description can be <code>null</code> for both {@link BusinessDayIncrement}
     * and this method still returns <code>true</code>
     * 
     * @param other
     *            the other BusinessDayIncremental
     * @return <code>true</code> if this {@link BusinessDayIncrement} is the same as
     *         the other one
     */
    public boolean isSame(BusinessDayIncrement other) {
	if (other == null) {
	    return false;
	}
	if (this == other) {
	    return true;
	}
	return this.getTicketNumber().equals(other.getTicketNumber()) && this.getChargeType() == other.getChargeType()//
		&& this.isCharged == other.isCharged() && hasSameDescription(other);
    }

    private boolean hasSameDescription(BusinessDayIncrement other) {
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

    public String getDescription() {
	return description;
    }

    public void setDescription(String description) {
	this.description = description;
    }

    /**
     * Sets the charge-type as int from the given representation. 
     * @param chargeTypeRep the new representation of a charge type
     * @throws InvalidChargeTypeRepresentationException 
     */
    public void setChargeType(String chargeTypeRep) throws InvalidChargeTypeRepresentationException {
	this.chargeType = ChargeType.getLeistungsartForRep(chargeTypeRep);
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

    /**
     * Moves all {@link TimeSnippet} of the current {@link BusinessDayIncrement} to
     * the given {@link BusinessDayIncrement}. The {@link TimeSnippet}s of the
     * current {@link BusinessDayIncrement} are removed!
     * 
     * @param incrementToAddTimeSnippets
     */
    /*package */ void transferAllTimeSnipetsToBussinessDayIncrement(BusinessDayIncrement incrementToAddTimeSnippets) {
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
    public void updateBeginTimeSnippetAndCalculate(BusinessDayIncrement businessDayIncremental, int fromUptoSequence,
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
    public void updateEndTimeSnippetAndCalculate(BusinessDayIncrement businessDayIncremental, int fromUptoSequence,
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
     * Creates a new {@link BusinessDayIncrement} for the given
     * {@link BusinessDayIncrementAdd}
     * 
     * @param update
     *            the {@link BusinessDayIncrementAdd} with the new values
     * @return a new {@link BusinessDayIncrement}
     */
    public static BusinessDayIncrement of(BusinessDayIncrementAdd update) {

	BusinessDayIncrement businessDayIncremental = new BusinessDayIncrement(update.getTimeSnippet().getDate());
	businessDayIncremental.description = update.getDescription();
	businessDayIncremental.ticketNumber = update.getTicketNo();
	businessDayIncremental.chargeType = update.getKindOfService();
	businessDayIncremental.startCurrentTimeSnippet(update.getTimeSnippet().getBeginTimeStamp());
	businessDayIncremental.stopCurrentTimeSnippet(update.getTimeSnippet().getEndTimeStamp());
	return businessDayIncremental;
    }

    /**
     * Returns <code>true</code> if this {@link BusinessDayIncrement} has started
     * before the given date. This method returns <code>true</code> if this {@link BusinessDayIncrement}
     * was created on the same day the given Time instance has.
     * 
     * @param time2Check the {@link Time} to check
     * @return <code>true</code> if this {@link BusinessDayIncrement} was created. Otherwise return <code>false</code>
      */
    public boolean isBefore(Time time2Check) {

	long days = time2Check.getDays();
	Time bdTime = new Time(date.getTime());
	return days < bdTime.getDays();
    }
}
