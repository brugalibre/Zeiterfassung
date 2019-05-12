/**
 * 
 */
package com.myownb3.dominic.timerecording.settings.round;

/**
 * @author Dominic
 *
 */
public class TimeRounder {

    public static final TimeRounder INSTANCE = new TimeRounder();

    private TimeRounder() {
	// private
    }

    private RoundMode roundMode;

    public void setRoundMode(RoundMode roundMode) {
	this.roundMode = roundMode;
    }

    public final RoundMode getRoundMode() {
	return this.roundMode;
    }
}
