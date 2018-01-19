/**
 * 
 */
package com.myownb3.dominic.util.comparator;

import java.util.Comparator;

import com.myownb3.dominic.timerecording.work.businessday.TimeSnippet;
import com.myownb3.dominic.timerecording.work.date.Time;

/**
 * @author Dominic
 *
 */
public class TimeStampComparator implements Comparator<TimeSnippet> {
    @Override
    public int compare(TimeSnippet timeSnippet, TimeSnippet timeSnippet2) {
	Time beginTimeStamp1 = timeSnippet.getBeginTimeStamp();
	Time beginTimeStamp2 = timeSnippet2.getBeginTimeStamp();
	return beginTimeStamp1.compareTo(beginTimeStamp2);
    }
}
