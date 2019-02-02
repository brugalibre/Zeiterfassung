/**
 * 
 */
package com.myownb3.dominic.timerecording.work.businessday.ext;

import java.util.List;
import java.util.stream.Collectors;

import com.myownb3.dominic.timerecording.work.businessday.BusinessDay;
import java.util.Date;

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

    public final Date getDate() {
	return this.date;
    }

    public BusinessDay4Export(BusinessDay businessDay) {

	totalDuration = businessDay.getTotalDuration();
	date = businessDay.getDate();

	businessDayIncrements = businessDay.getIncrements()//
		.stream()//
		.map(BusinessDayInc4Export::new)//
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

    public final float getTotalDuration() {
	return this.totalDuration;
    }
}
