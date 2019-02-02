/**
 * 
 */
package com.myownb3.dominic.util.comparator;

import java.util.Comparator;

import com.myownb3.dominic.timerecording.work.businessday.ext.TimeSnippet4Export;
import com.myownb3.dominic.timerecording.work.date.Time;

/**
 * @author Dominic
 *
 */
public class TimeStampComparator implements Comparator<TimeSnippet4Export> {
    @Override
    public int compare(TimeSnippet4Export timeSnippet, TimeSnippet4Export timeSnippet2) {
	Time beginTimeStamp1 = timeSnippet.getBeginTimeStamp();
	Time beginTimeStamp2 = timeSnippet2.getBeginTimeStamp();
	return beginTimeStamp1.compareTo(beginTimeStamp2);
    }
}
