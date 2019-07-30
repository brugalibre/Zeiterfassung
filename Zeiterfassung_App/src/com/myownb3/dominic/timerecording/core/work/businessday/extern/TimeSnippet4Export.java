/**
 * 
 */
package com.myownb3.dominic.timerecording.core.work.businessday.extern;

import java.util.Comparator;

import com.myownb3.dominic.timerecording.core.work.businessday.TimeSnippet;
import com.myownb3.dominic.timerecording.core.work.date.Time;

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

    public String getBeginTimeStampRep() {
	return String.valueOf(beginTimeStamp);
    }

    public final String getEndTimeStamp() {
	return String.valueOf(endTimeStamp);
    }

    public String getEndTimeStampRep() {
	return String.valueOf(endTimeStamp);
    }

    public static class TimeStampComparator implements Comparator<TimeSnippet4Export> {
	@Override
	public int compare(TimeSnippet4Export timeSnippet, TimeSnippet4Export timeSnippet2) {
	    Time beginTimeStamp1 = timeSnippet.beginTimeStamp;
	    Time beginTimeStamp2 = timeSnippet2.beginTimeStamp;
	    return beginTimeStamp1.compareTo(beginTimeStamp2);
	}
    }
}
