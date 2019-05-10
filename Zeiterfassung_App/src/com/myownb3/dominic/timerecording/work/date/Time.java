/**
 * 
 */
package com.myownb3.dominic.timerecording.work.date;

import com.myownb3.dominic.timerecording.work.date.TimeType.TIME_TYPE;

/**
 * The {@link Time} class represents the time, e.g. 18:55:45 It is used to add
 * additionally features such as add a amount of time to an existing
 * {@link Time} object
 * 
 * @author Dominic
 */
public class Time extends java.sql.Time {

    /**
    * 
    */
    private static final long serialVersionUID = 1L;

    /**
     * @param time
     */
    public Time(long time) {
	super(time);
	roundSeconds();
    }

    @SuppressWarnings("deprecation")
    private void roundSeconds() {
	if (getSeconds() >= 30) {
	    setSeconds(60);
	} else {
	    setSeconds(0);
	}
    }

    /**
     * Returns the factor to make proper calculations with the given time. E.g. for
     * the TIME_TYPE 'HOUR' the value 3'600'000 is returned, since an hour has
     * 3'600'000 milliseconds
     * 
     * @param type
     * @return the appropriate calculating factor
     */
    public static int getTimeRefactorValue(TIME_TYPE type) {
	switch (type) {
	case HOUR:
	    return 3600000;
	case MIN:
	    return 60000;
	case SEC:
	    return 1000;
	default:
	    throw new RuntimeException("Unknown TIME_TYPE value '" + type + "'!");
	}
    }
}
