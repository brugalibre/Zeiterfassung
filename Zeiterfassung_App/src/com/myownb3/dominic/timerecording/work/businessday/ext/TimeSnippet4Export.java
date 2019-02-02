/**
 * 
 */
package com.myownb3.dominic.timerecording.work.businessday.ext;

import com.myownb3.dominic.timerecording.work.businessday.TimeSnippet;
import com.myownb3.dominic.timerecording.work.date.Time;

/**
 * The {@link TimeSnippet4Export} is used whenever a {@link TimeSnippet} is
 * going to be exported. Either on a UI or on a text file
 * 
 * @author Dominic
 *
 */
public class TimeSnippet4Export {

    public TimeSnippet4Export(TimeSnippet timeSnippet) {
	this.beginTimeStamp = timeSnippet.getBeginTimeStamp();
	this.endTimeStamp = timeSnippet.getEndTimeStamp();
    }

    private Time beginTimeStamp;
    private Time endTimeStamp;

    public final Time getBeginTimeStamp() {
	return this.beginTimeStamp;
    }

    public final Time getEndTimeStamp() {
	return this.endTimeStamp;
    }
}
