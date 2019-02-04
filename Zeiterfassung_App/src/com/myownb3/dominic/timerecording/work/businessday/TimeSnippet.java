/**
 * 
 */
package com.myownb3.dominic.timerecording.work.businessday;

import com.myownb3.dominic.timerecording.app.TimeRecorder;
import java.util.Date;
import com.myownb3.dominic.timerecording.work.date.Time;
import com.myownb3.dominic.timerecording.work.date.TimeType.TIME_TYPE;

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

    public TimeSnippet(Date date) {
	this.date = date;
    }

    public Date getDate() {
	return date;
    }

    public void setDate(Date date) {
	this.date = date;
    }

    public void setBeginTimeStamp(Time beginTimeStamp) {
	this.beginTimeStamp = beginTimeStamp;
    }

    public void setEndTimeStamp(Time endTimeStamp) {
	this.endTimeStamp = endTimeStamp;
    }

    /**
     * @return the amount of minutes between the start, and end-point
     */
    public float getDuration() {
	return getDuration(TimeRecorder.GLOBAL_TIME_TYPE);
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
	return Float.parseFloat(TimeRecorder.formater.format((time = time / factor)));
    }

    public Time getEndTimeStamp() {
	return endTimeStamp;
    }

    public Time getBeginTimeStamp() {
	return beginTimeStamp;
    }
}
