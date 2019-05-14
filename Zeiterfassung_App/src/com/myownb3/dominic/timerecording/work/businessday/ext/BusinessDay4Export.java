/**
 * 
 */
package com.myownb3.dominic.timerecording.work.businessday.ext;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import com.myownb3.dominic.timerecording.work.businessday.BusinessDay;

/**
 * The {@link BusinessDay4Export} is used whenever a {@link BusinessDay} is
 * going to be exported. Either on a UI or on a text file
 * 
 * @author Dominic
 *
 */
public class BusinessDay4Export {

    private List<BusinessDayInc4Export> businessDayIncrements;
    private float totalDuration;
    private Date date;

    /**
     * Returns the default representation of a date using the pattern 'dd.MM.yyyy'
     * 
     * @return the default representation of a date using the pattern 'dd.MM.yyyy'
     */
    public String getDateRep() {
	return getDateRep("dd.MM.yyyy");
    }

    /**
     * Returns the default representation of a date using the given pattern
     * 
     * @param pattern
     *            the pattern to use
     * @return the default representation of a date using the given pattern
     */
    public String getDateRep(String pattern) {
	SimpleDateFormat df = (SimpleDateFormat) DateFormat.getTimeInstance(DateFormat.SHORT);
	df.applyPattern(pattern);
	return df.format(date);
    }

    private BusinessDay4Export(BusinessDay businessDay) {

	totalDuration = businessDay.getTotalDuration();
	date = businessDay.getDate();

	businessDayIncrements = businessDay.getIncrements()//
		.stream()//
		.map(BusinessDayInc4Export::of)//
		.collect(Collectors.toList());
	businessDayIncrements.stream()//
		.forEach(businessDayInc -> businessDayInc.addPlaceHolderForMissingCell(this));
    }

    public final List<BusinessDayInc4Export> getBusinessDayIncrements() {
	return this.businessDayIncrements;
    }

    /**
     * Returns <code>true</code> if this {@link BusinessDay} has at least one
     * element with a description <code>false</code> if not
     * 
     * @return <code>true</code> if this {@link BusinessDay} has at least one
     *         element with a description <code>false</code> if not
     */
    public boolean hasIncrementWithDescription() {
	return businessDayIncrements//
		.stream()//
		.anyMatch(BusinessDayInc4Export::hasDescription);
    }

    /**
     * @return <code>true</code> if this {@link BusinessDay4Export} has at least one
     *         {@link BusinessDayInc4Export} which is not charged yet otherwise
     *         <code>false</code>
     */
    public boolean hasNotChargedElements() {
	if (businessDayIncrements.isEmpty()) {
	    return false;
	}
	return !businessDayIncrements//
		.stream()//
		.anyMatch(BusinessDayInc4Export::isCharged);
    }

    /**
     * Returns the amount of Begin/End Elements this {@link BusinessDay} has
     * 
     * @return the amount of Begin/End Elements this {@link BusinessDay} has
     */
    public int getAmountOfVonBisElements() {
	int counter = 0;
	for (BusinessDayInc4Export businessDayIncremental : businessDayIncrements) {
	    counter = Math.max(counter, businessDayIncremental.getTimeSnippets().size());
	}
	return counter;
    }

    public String getTotalDurationRep() {
	return String.valueOf(totalDuration);
    }

    /**
     * Creates a new {@link BusinessDay4Export} for the given {@link BusinessDay}
     * 
     * @param businessDay
     *            the given {@link BusinessDay}
     * @return a new {@link BusinessDay4Export}
     */
    public static BusinessDay4Export of(BusinessDay businessDay) {
	return new BusinessDay4Export(businessDay);
    }
}
