/**
 * 
 */
package com.myownb3.dominic.export;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.myownb3.dominic.librarys.text.res.TextLabel;
import com.myownb3.dominic.timerecording.work.businessday.BusinessDay;
import com.myownb3.dominic.timerecording.work.businessday.BusinessDayIncremental;
import com.myownb3.dominic.timerecording.work.businessday.TimeSnippet;
import com.myownb3.dominic.util.comparator.TimeStampComparator;
import com.myownb3.dominic.util.utils.StringUtil;

/**
 * @author Dominic
 *
 */
public class ContentSelector {
    private static final Object CONTENT_SEPPARATOR = "; ";

    public static List<String> collectContent(BusinessDay bussinessDay) {
	StringBuilder builder = new StringBuilder();
	List<String> content = new ArrayList<>();

	// First line to mark the date, when the time was recorded
	builder.append(bussinessDay.getDate());
	builder.append(System.getProperty("line.separator"));
	builder.append(System.getProperty("line.separator"));

	// = For each 'Ticket' or Increment of an entire Day
	for (BusinessDayIncremental inc : bussinessDay.getIncrements()) {
	    builder.append(TextLabel.TICKET + ": ");
	    builder.append(inc.getTicketNumber());
	    builder.append(CONTENT_SEPPARATOR);
	    if (StringUtil.isNotEmptyOrNull(inc.getDescription())) {
		builder.append(TextLabel.DESCRIPTION_LABEL + ": ");
		builder.append(inc.getDescription());
		builder.append(CONTENT_SEPPARATOR);
	    }
	    builder.append(TextLabel.AMOUNT_OF_HOURS_LABEL + ": " + inc.getTotalDuration());
	    builder.append(CONTENT_SEPPARATOR);

	    List<TimeSnippet> timeSnippets = inc.getTimeSnippets();
	    Collections.sort(timeSnippets, new TimeStampComparator());

	    // = For each single work units of a Ticket
	    for (TimeSnippet snippet : timeSnippets) {
		builder.append(TextLabel.VON_LABEL + ": ");
		builder.append(snippet.getBeginTimeStamp());
		builder.append(CONTENT_SEPPARATOR);
		builder.append(TextLabel.BIS_LABEL + ": ");
		builder.append(snippet.getEndTimeStamp());
		builder.append(CONTENT_SEPPARATOR);
	    }
	    builder.append(TextLabel.CHARGE_TYPE_LABEL + ": " + inc.getChargeType());
	    builder.append(CONTENT_SEPPARATOR);

	    builder.append(System.getProperty("line.separator"));
	    content.add(builder.toString());
	    builder.delete(0, builder.capacity());
	}
	builder.append(System.getProperty("line.separator"));
	builder.append(TextLabel.TOTAL_AMOUNT_OF_HOURS_LABEL + " " + bussinessDay.getTotalDuration());
	content.add(builder.toString());
	return content;
    }
}
