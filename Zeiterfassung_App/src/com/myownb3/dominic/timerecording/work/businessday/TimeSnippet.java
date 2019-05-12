/**
 * 
 */
package com.myownb3.dominic.timerecording.work.businessday;

import java.util.Date;

import com.myownb3.dominic.timerecording.app.TimeRecorder;
import com.myownb3.dominic.timerecording.callback.handler.TimeSnippedChangedCallbackHandler;
import com.myownb3.dominic.timerecording.callback.handler.impl.ChangedValue;
import com.myownb3.dominic.timerecording.work.date.Time;
import com.myownb3.dominic.timerecording.work.date.TimeType.TIME_TYPE;
import com.myownb3.dominic.util.parser.DateParser;
import com.myownb3.dominic.util.parser.NumberFormat;
import com.myownb3.dominic.util.utils.StringUtil;

/**
 * Defines a period, that begin with the {@link #beginTimeStamp} and ends with
 * the {@link #endTimeStamp}. The time between these two time-stamps can be
 * calculated by invoking the {@link #getDuration()} or
 * {@link #getDuration(TIME_TYPE)} method.
 * 
 * @author Dominic
 *
 */
public class TimeSnippet {
    private Date date; // the date of the day, when this TimeSnippet took place
    private Time beginTimeStamp;
    private Time endTimeStamp;
    private TimeSnippedChangedCallbackHandler callbackHandler;

    public TimeSnippet(Date date) {
	this.date = date;
    }

    /**
     * Trys to parse the given end time stamp (as String) and summs this value up to
     * the given begin Time Stamp
     * 
     * @param newEndAsString
     *            the new End-time stamp as String
     * @throws NumberFormatException
     *             if there goes anything wrong while parsing
     */
    public void addAdditionallyTime(String newEndAsString) throws NumberFormatException {
	float additionallyTime = NumberFormat.parseFloat(newEndAsString) - getDuration();

	long additionallyDuration = (long) (Time.getTimeRefactorValue(TimeRecorder.GLOBAL_TIME_TYPE)
		* additionallyTime);
	setEndTimeStamp(new Time(getEndTimeStamp().getTime() + additionallyDuration));
    }

    /**
     * Trys to parse a new {@link Date} from the given timestamp value and sets this
     * value as new begin-time stamp
     * 
     * @param newTimeStampValue
     *            the new begin-time-stamp as String
     */
    public void updateAndSetBeginTimeStamp(String newTimeStampValue) {
	if (!StringUtil.isEqual(newTimeStampValue, getBeginTimeStampRep())) {
	    Time time = DateParser.getTime(newTimeStampValue, getBeginTimeStamp());
	    setBeginTimeStamp(new Time(time));
	}
    }

    /**
     * Trys to parse a new {@link Date} from the given timestamp value and sets this
     * value as new end-time stamp
     * 
     * @param newTimeStampValue
     *            the new begin-time-stamp as String
     */
    public void updateAndSetEndTimeStamp(String newTimeStampValue) {

	if (!StringUtil.isEqual(newTimeStampValue, getEndTimeStampRep())) {
	    Time time = DateParser.getTime(newTimeStampValue, getEndTimeStamp());
	    setEndTimeStamp(new Time(time));
	}
    }

    /**
     * @return the amount of minutes between the start, and end-point
     */
    public float getDuration() {
	return getDuration(TimeRecorder.GLOBAL_TIME_TYPE);
    }

    public String getDurationRep() {
	return NumberFormat.format(getDuration());
    }

    /**
     * Return the amount of minutes between the start, and end-point. If there is no
     * ent-point yet, a new end-point at the most current now is created
     * 
     * @return the amount of minutes between the start, and end-point
     */
    public float getDuration(TIME_TYPE type) {
	Time endTimeSnippet = (endTimeStamp != null ? endTimeStamp : new Time(System.currentTimeMillis()));
	float time = endTimeSnippet.getTime() - getBeginTimeStamp().getTime();
	int factor = Time.getTimeRefactorValue(type);

	return NumberFormat.parse(time, factor);
    }

    private void notifyCallbackHandler(ChangedValue changedValue) {
	if (callbackHandler != null) {
	    callbackHandler.handleTimeSnippedChanged(changedValue);
	}
    }

    public Date getDate() {
	return date;
    }

    public void setDate(Date date) {
	this.date = date;
    }

    public void setBeginTimeStamp(Time beginTimeStamp) {
	this.beginTimeStamp = beginTimeStamp;
	notifyCallbackHandler(ChangedValue.of(-1, getBeginTimeStampRep(), ValueTypes.BEGIN));
    }

    public void setEndTimeStamp(Time endTimeStamp) {
	this.endTimeStamp = endTimeStamp;
	notifyCallbackHandler(ChangedValue.of(-1, getEndTimeStampRep(), ValueTypes.END));
    }

    public Time getEndTimeStamp() {
	return endTimeStamp;
    }

    public Time getBeginTimeStamp() {
	return beginTimeStamp;
    }

    public String getEndTimeStampRep() {
	return String.valueOf(endTimeStamp);
    }

    public String getBeginTimeStampRep() {
	return String.valueOf(beginTimeStamp);
    }

    public final void setCallbackHandler(TimeSnippedChangedCallbackHandler callbackHandler) {
	this.callbackHandler = callbackHandler;
    }

    /**
     * Creates a copy of the given timeSnippet
     * 
     * @param otherTimeSnippet
     *            the {@link TimeSnippet} to create a copy of
     * @return a copy of the given timeSnippet
     */
    public static TimeSnippet of(TimeSnippet otherTimeSnippet) {
	if (otherTimeSnippet == null) {
	    return null;
	}
	TimeSnippet timeSnippet = new TimeSnippet(otherTimeSnippet.getDate());
	timeSnippet.setBeginTimeStamp(otherTimeSnippet.getBeginTimeStamp());
	timeSnippet.setEndTimeStamp(otherTimeSnippet.getEndTimeStamp());
	return timeSnippet;
    }
}
