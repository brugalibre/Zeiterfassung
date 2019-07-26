/**
 * 
 */
package com.myownb3.dominic.timerecording.work.date;

/**
 * Defines what a specific type of time is, eg. a day, a minute and so on. This
 * is used to define in which format the total amount of time is calculated
 * 
 * @author Dominic
 */
public class TimeType {
    public enum TIME_TYPE {
	HOUR, MIN, SEC, MILI_SEC,
    }
}
