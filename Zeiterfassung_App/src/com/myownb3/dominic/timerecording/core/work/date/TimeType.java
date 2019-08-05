/**
 * 
 */
package com.myownb3.dominic.timerecording.core.work.date;

/**
 * Defines what a specific type of time is, eg. a day, a minute and so on. This
 * is used to define in which format the total amount of time is calculated
 * 
 * @author Dominic
 */
public class TimeType {

    /**
     * The default {@link TimeType} is {@link TIME_TYPE#HOUR}
     */
    public static final TIME_TYPE DEFAULT = TimeType.TIME_TYPE.HOUR;
    public enum TIME_TYPE {
	HOUR, MIN, SEC, MILI_SEC,
    }
}
