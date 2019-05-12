/**
 * 
 */
package com.myownb3.dominic.timerecording.work.businessday;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

import com.myownb3.dominic.timerecording.app.TimeRecorder;
import com.myownb3.dominic.timerecording.settings.round.TimeRounder;
import com.myownb3.dominic.timerecording.work.businessday.update.BusinessDayIncrementUpdate;
import com.myownb3.dominic.timerecording.work.date.Time;
import com.myownb3.dominic.timerecording.work.date.TimeType.TIME_TYPE;
import com.myownb3.dominic.util.parser.NumberFormat;

/**
 * The {@link BusinessDay} defines an entire day full of work. Such a day may
 * consist of several increments, called {@link BusinessDayIncrement}. <br>
 * Each such incremental is defined by several {@link TimeSnippet}s. And each of
 * those snippet's has a begin time-stamp, an end time-stamp and a specific
 * amount of hours <br>
 * 
 * A {@link BusinessDayIncrement} can have several of those snippet's -
 * scattered over the entire day. But each of this {@link BusinessDayIncrement}
 * belongs to the same <br>
 * project. One or more such {@link BusinessDayIncrement} are put together - to
 * a whole {@link BusinessDay}
 * 
 * @author Dominic
 * 
 */
public class BusinessDay {
    // all increments of this BusinessDay (those are all finished!)
    private CopyOnWriteArrayList<BusinessDayIncrement> increments;
    // the current increment which has been started but not yet finished so far
    private BusinessDayIncrement currentBussinessDayIncremental;

    public BusinessDay() {
	initialize();
    }

    /**
    * 
    */
    private void initialize() {
	increments = new CopyOnWriteArrayList<BusinessDayIncrement>();
	currentBussinessDayIncremental = new BusinessDayIncrement(new Date());
    }

    /**
     * Resumes the {@link #currentBussinessDayIncremental}
     */
    public void resumeLastIncremental() {

	currentBussinessDayIncremental.resumeLastTimeSnippet();
    }

    /**
     * Starts a new {@link BusinessDayIncrement}. That means to create a new
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
    public void stopCurrentIncremental() {
	Time endTimeStamp = new Time((long) (System.currentTimeMillis() + 31750 * Math.random()),
		TimeRounder.INSTANCE.getRoundMode());
	currentBussinessDayIncremental.stopCurrentTimeSnippet(endTimeStamp);
    }

    /**
     * If there exist two or more {@link BusinessDayIncrement} with the same
     * {@link BusinessDayIncrement#getDescription()} the {@link TimeSnippet} are
     * moved from the first {@link BusinessDayIncrement} to the other
     */
    public void checkForRedundancys() {
	for (BusinessDayIncrement incToCompareWith : increments) {
	    for (BusinessDayIncrement anotherIncrement : getIncrementsToCheck(incToCompareWith)) {
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
    private List<BusinessDayIncrement> getIncrementsToCheck(BusinessDayIncrement incToCompareWith) {
	return increments.stream()//
		.filter(inc -> inc != incToCompareWith)//
		.collect(Collectors.toList());
    }

    /**
     * After {@link TimeSnippet} are moved, the {@link BusinessDayIncrement} with no
     * {@link TimeSnippet} are removed
     */
    private void deleteEmptyIncrements() {
	for (BusinessDayIncrement inc : increments) {
	    if (inc.getTimeSnippets().isEmpty()) {
		increments.remove(inc);
	    }
	}
    }

    public void flagBusinessDayAsCharged() {
	increments.stream()//
		.forEach(BusinessDayIncrement::flagAsCharged);
    }

    /**
     * Returns <code>true</code> if this {@link BusinessDay} has at least one
     * element which is not yed charged. Otherwise returns <code>false</code>
     * 
     * @return <code>true</code> if this {@link BusinessDay} has at least one
     *         element which is not yed charged. Otherwise returns
     *         <code>false</code>
     */
    public boolean hasNotChargedElements() {
	return increments.stream()//
		.anyMatch(bDayInc -> !bDayInc.isCharged());
    }

    /**
    * 
    */
    private void createNewIncremental() {
	currentBussinessDayIncremental = new BusinessDayIncrement(new Date());
    }

    public float getTotalDuration(TIME_TYPE type) {
	float sum = 0;

	for (BusinessDayIncrement incremental : increments) {
	    sum = sum + incremental.getTotalDuration(type);
	}
	return NumberFormat.parseFloat(NumberFormat.format(sum));
    }

    /**
     * Returns the Local sensitive representation of the total duration for the
     * given {@link TIME_TYPE}
     * 
     * @param type
     *            the given type of time
     * @return the Local sensitive representation of the total duration for the
     *         given {@link TIME_TYPE}
     */
    public String getTotalDurationRep(TIME_TYPE type) {
	float totalDuration = getTotalDuration(type);
	return NumberFormat.format(totalDuration);
    }

    /**
     * Deletes all {@link BusinessDayIncrement} which are already finished
     */
    public void clearFinishedIncrements() {
	increments.clear();
    }

    public float getTotalDuration() {
	return getTotalDuration(TimeRecorder.GLOBAL_TIME_TYPE);
    }

    public BusinessDayIncrement getCurrentBussinessDayIncremental() {
	return currentBussinessDayIncremental;
    }

    public List<BusinessDayIncrement> getIncrements() {
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
     * {@link BusinessDayIncrement}, so the {@link #increments} is empty, a new
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

    /* package */ Optional<BusinessDayIncrement> getBusinessIncrement(int orderNr) {
	BusinessDayIncrement businessDayIncremental = null;
	for (int i = 0; i < increments.size(); i++) {
	    if (orderNr == i + 1) {
		businessDayIncremental = increments.get(i);
	    }
	}
	return Optional.ofNullable(businessDayIncremental);
    }

    /**
     * @param update
     */
    public void addBusinessIncrement(BusinessDayIncrementUpdate update) {
	BusinessDayIncrement newBusinessDayInc = BusinessDayIncrement.of(update);
	increments.add(newBusinessDayInc);
	checkForRedundancys();
    }
}
