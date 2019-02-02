/**
 * 
 */
package com.myownb3.dominic.timerecording.work.businessday;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

import com.myownb3.dominic.timerecording.app.TimeRecorder;
import com.myownb3.dominic.timerecording.work.date.Date;
import com.myownb3.dominic.timerecording.work.date.Time;
import com.myownb3.dominic.timerecording.work.date.TimeType.TIME_TYPE;

/**
 * The {@link BusinessDay} defines an entire day full of work. Such a day may
 * consist of several increments, called {@link BusinessDayIncremental}. <br>
 * Each such incremental is defined by several {@link TimeSnippet}s. And each of
 * those snippet's has a begin time-stamp, an end time-stamp and a specific
 * amount of hours <br>
 * 
 * A {@link BusinessDayIncremental} can have several of those snippet's -
 * scattered over the entire day. But each of this
 * {@link BusinessDayIncremental} belongs to the same <br>
 * project. One or more such {@link BusinessDayIncremental} are put together -
 * to a whole {@link BusinessDay}
 * 
 * @author Dominic
 * 
 */
public class BusinessDay {
    // all increments of this BusinessDay (those are all finished!)
    private CopyOnWriteArrayList<BusinessDayIncremental> increments;
    // the current increment which has been started but not yet finished so far
    private BusinessDayIncremental currentBussinessDayIncremental;

    public BusinessDay() {
	initialize();
    }

    /**
    * 
    */
    private void initialize() {
	increments = new CopyOnWriteArrayList<BusinessDayIncremental>();
	currentBussinessDayIncremental = new BusinessDayIncremental(new Date());
    }

    /**
     * 
     */
    public void resumeLastIncremental() {

	increments.remove(currentBussinessDayIncremental);
	currentBussinessDayIncremental.resmeLastTimeSnippet();
    }

    /**
     * Starts a new {@link BusinessDayIncremental}. That means to create a new
     * instance of {@link Time} <br>
     * and forward that to the
     */
    public void startNewIncremental() {
	Time time = new Time(System.currentTimeMillis());
	createNewIncremental();
	currentBussinessDayIncremental.startCurrentTimeSnippet(time);
    }

    /**
     * Stops the current incremental and add the
     * {@link #currentBussinessDayIncremental} to the list with increments. After
     * that, a new incremental is created
     */
    public void stopCurrentIncremental(boolean isSilendMode) {
	Time endTimeStamp = new Time(System.currentTimeMillis());
	increments.add(currentBussinessDayIncremental);

	// Per Default das vom Vorgänger übernehmen
	if (isSilendMode && !increments.isEmpty()) {
	    BusinessDayIncremental businessDayIncremental = increments.get(0);
	    currentBussinessDayIncremental.setDescription(businessDayIncremental.getDescription());
	    currentBussinessDayIncremental.setTicketNumber(businessDayIncremental.getTicketNumber());
	    currentBussinessDayIncremental.setChargeType(businessDayIncremental.getChargeType());
	    TimeRecorder.checkForRedundancy();
	}
	currentBussinessDayIncremental.stopCurrentTimeSnippet(endTimeStamp);
    }

    /**
     * If there exist two or more {@link BusinessDayIncremental} with the same
     * {@link BusinessDayIncremental#getDescription()} the {@link TimeSnippet} are
     * moved from the first {@link BusinessDayIncremental} to the other
     */
    public void checkForRedundancys() {
	for (BusinessDayIncremental incToCompareWith : increments) {
	    for (BusinessDayIncremental anotherIncrement : getIncrementsToCheck(incToCompareWith)) {
		if (incToCompareWith.isSame(anotherIncrement)) {
		    incToCompareWith.transferAllTimeSnipetsToBussinessDayIncrement(anotherIncrement);
		}
	    }
	}
	deleteEmptyIncrements();
    }

    /**
     * don't check the increment with itself!
     */
    private List<BusinessDayIncremental> getIncrementsToCheck(BusinessDayIncremental incToCompareWith) {
	return increments.stream()//
		.filter(inc -> inc != incToCompareWith)//
		.collect(Collectors.toList());
    }

    /**
     * After {@link TimeSnippet} are moved, the {@link BusinessDayIncremental} with
     * no {@link TimeSnippet} are removed
     */
    private void deleteEmptyIncrements() {
	for (BusinessDayIncremental inc : increments) {
	    if (inc.getTimeSnippets().isEmpty()) {
		increments.remove(inc);
	    }
	}
    }

    /**
    * 
    */
    private void createNewIncremental() {
	currentBussinessDayIncremental = new BusinessDayIncremental(new Date());
    }

    public float getTotalDuration(TIME_TYPE type) {
	float sum = 0;

	for (BusinessDayIncremental incremental : increments) {
	    sum = sum + incremental.getTotalDuration(type);
	}
	return Float.parseFloat(TimeRecorder.formater.format(sum));
    }

    /**
     * Deletes all {@link BusinessDayIncremental} which are already finished
     */
    public void clearFinishedIncrements() {
	increments.clear();
    }

    public float getTotalDuration() {
	return getTotalDuration(TimeRecorder.GLOBAL_TIME_TYPE);
    }

    public BusinessDayIncremental getCurrentBussinessDayIncremental() {
	return currentBussinessDayIncremental;
    }

    public List<BusinessDayIncremental> getIncrements() {
	return increments;
    }

    /**
     * Returns a String representation of {@link #getDate()}
     * 
     * @return a String representation of {@link #getDate()}
     */
    public String getDateAsString() {
	return getDate().toString();
    }

    /**
     * Returns the {@link Date} of this {@link BusinessDay}. If this
     * {@link BusinessDay} has no <br>
     * {@link BusinessDayIncremental}, so the {@link #increments} is empty, a new
     * instance of {@link Date} is returned.
     * 
     * @return the {@link Date} of this BussinessDay.
     */
    public Date getDate() {
	if (increments.isEmpty()) {
	    return new Date();
	}
	return increments.get(0).getDate();
    }
}
