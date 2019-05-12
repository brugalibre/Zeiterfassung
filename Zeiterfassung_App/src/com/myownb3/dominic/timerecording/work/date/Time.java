/**
 * 
 */
package com.myownb3.dominic.timerecording.work.date;

import org.joda.time.Duration;
import org.joda.time.Period;
import org.joda.time.PeriodType;

import com.myownb3.dominic.timerecording.settings.round.RoundMode;
import com.myownb3.dominic.timerecording.work.date.TimeType.TIME_TYPE;
import com.myownb3.dominic.util.parser.DateParser;

/**
 * The {@link Time} class represents the time, e.g. 18:55:45 It is used to add
 * additionally features such as add a amount of time to an existing
 * {@link Time} object
 * 
 * @author Dominic
 */
public class Time {

    private Duration duration;

    /**
     * Create a new {@link Time} object according to the given {@link Time} object
     * 
     * @param time
     */
    public Time(Time time) {
	this(time.getTime());
    }

    /**
     * @param time
     */
    public Time(long time) {
	this(time, RoundMode.ONE_MIN);
    }

    /**
     * @param time
     * @param roundMode
     */
    public Time(long time, RoundMode roundMode) {
	duration = new Duration(time);
	round(roundMode);
    }

    private void round(RoundMode roundMode) {

	switch (roundMode) {
	case ONE_MIN:
	    roundSeconds();
	    break;
	case FIVE_MIN:
	    // Fall through
	case TEN_MIN:
	    // Fall through
	case FIFTEEN_MIN:
	    roundSecondsAndMinutes(roundMode);
	    break;

	default:
	    break;
	}
    }

    private void roundSecondsAndMinutes(RoundMode roundMode) {
	roundSeconds();
	roundMinutes(roundMode);
    }

    private void roundMinutes(RoundMode roundMode) {

	int amount = roundMode.getAmount();
	Period period = duration.toPeriod(PeriodType.dayTime());
	float modulo = period.getMinutes() % amount;
	if (modulo >= amount / 2f) {
	    duration = duration.plus((amount - (int) modulo) * getTimeRefactorValue(roundMode.getTimeType()));
	} else {
	    duration = duration.minus((int)modulo * getTimeRefactorValue(roundMode.getTimeType()));
	}
    }

    private void roundSeconds() {
	Period period = duration.toPeriod(PeriodType.dayTime());
	if (period.getSeconds() >= 30) {
	    duration = duration.plus((60 - period.getSeconds()) * getTimeRefactorValue(TIME_TYPE.SEC));
	} else {
	    duration = duration.minus(period.getSeconds() * getTimeRefactorValue(TIME_TYPE.SEC));
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

    @Override
    public String toString() {
	return DateParser.parse2String(this);
    }

    public int compareTo(Time otherTime) {
	return duration.compareTo(otherTime.duration);
    }

    /**
     * Returns the amount of milliseconds of this {@link Time}
     * 
     * @return the amount of milliseconds
     */
    public long getTime() {
	return duration.getMillis();
    }
}
