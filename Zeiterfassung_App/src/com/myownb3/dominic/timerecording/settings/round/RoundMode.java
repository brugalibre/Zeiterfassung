/**
 * 
 */
package com.myownb3.dominic.timerecording.settings.round;

import com.myownb3.dominic.timerecording.work.date.TimeType.TIME_TYPE;

/**
 * @author Dominic
 *
 */
public enum RoundMode {

    ONE_MIN(TIME_TYPE.MIN, 1), FIVE_MIN(TIME_TYPE.MIN, 5), TEN_MIN(TIME_TYPE.MIN, 10);

    private TIME_TYPE timeType;
    private int amount;

    private RoundMode(TIME_TYPE timeType, int amount) {
	this.timeType = timeType;
	this.amount = amount;
    }

    public final TIME_TYPE getTimeType() {
	return this.timeType;
    }

    public final int getAmount() {
	return this.amount;
    }

}
