/**
 * 
 */
package com.myownb3.dominic.export;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.myownb3.dominic.librarys.text.res.TextLabel;
import com.myownb3.dominic.timerecording.charge.ChargeType;
import com.myownb3.dominic.timerecording.work.businessday.ext.BusinessDay4Export;
import com.myownb3.dominic.timerecording.work.businessday.ext.BusinessDayInc4Export;
import com.myownb3.dominic.timerecording.work.businessday.ext.TimeSnippet4Export;
import com.myownb3.dominic.util.utils.StringUtil;

/**
 * @author Dominic
 *
 */
public class ContentSelector {
    private static final Object CONTENT_SEPPARATOR = "; ";
    private static final Object CONTENT_SEPPARATOR_TURBO_BUCHER = ";";

    public static List<String> collectContent(BusinessDay4Export bussinessDay) {
	StringBuilder builder = new StringBuilder();
	List<String> content = new ArrayList<>();

	// First line to mark the date, when the time was recorded
	builder.append(bussinessDay.getDate());
	builder.append(System.getProperty("line.separator"));
	builder.append(System.getProperty("line.separator"));

	appendTitleHeaderCells(builder, bussinessDay);

	// = For each 'Ticket' or Increment of an entire Day
	for (BusinessDayInc4Export inc : getNotChargedIncrements(bussinessDay)) {
	    builder.append(TextLabel.TICKET + ": ");
	    builder.append(inc.getTicketNumber());
	    builder.append(CONTENT_SEPPARATOR);
	    if (StringUtil.isNotEmptyOrNull(inc.getDescription())) {
		builder.append(inc.getDescription());
		builder.append(CONTENT_SEPPARATOR);
	    }
	    builder.append(inc.getTotalDuration());
	    builder.append(CONTENT_SEPPARATOR);

	    List<TimeSnippet4Export> timeSnippets = inc.getTimeSnippets();

	    // = For each single work units of a Ticket
	    for (TimeSnippet4Export snippet : timeSnippets) {
		builder.append(snippet.getBeginTimeStamp());
		builder.append(CONTENT_SEPPARATOR);
		builder.append(snippet.getEndTimeStamp());
		builder.append(CONTENT_SEPPARATOR);
	    }
	    addPlaceHolderForMissingBeginEndElements(builder, inc);
	    builder.append(ChargeType.getRepresentation(inc.getChargeType()));
	    builder.append(CONTENT_SEPPARATOR);
	    builder.append(inc.isCharged() ? TextLabel.YES : TextLabel.NO);
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

    private static void appendTitleHeaderCells(StringBuilder builder, BusinessDay4Export bussinessDay) {

	builder.append(TextLabel.TICKET);
	builder.append(CONTENT_SEPPARATOR);
	builder.append(TextLabel.AMOUNT_OF_HOURS_LABEL);
	builder.append(CONTENT_SEPPARATOR);

	if (bussinessDay.hasIncrementWithDescription()) {
	    builder.append(TextLabel.DESCRIPTION_LABEL);
	    builder.append(CONTENT_SEPPARATOR);
	}

	appendBeginEndHeader(builder, bussinessDay);

	builder.append(TextLabel.CHARGE_TYPE_LABEL);
	builder.append(CONTENT_SEPPARATOR);
	builder.append(System.getProperty("line.separator"));
    }

    private static void appendBeginEndHeader(StringBuilder builder, BusinessDay4Export bussinessDay) {

	int counter = bussinessDay.getAmountOfVonBisElements();
	for (int i = 0; i < counter; i++) {
	    builder.append(TextLabel.VON_LABEL);
	    builder.append(CONTENT_SEPPARATOR);
	    builder.append(TextLabel.BIS_LABEL);
	    builder.append(CONTENT_SEPPARATOR);
	}
    }

    /*
     * All rows must fit with it content to the title header. Thats why we have to
     * add some placeholders if this row has less TimeSnipets then the maximum
     * amount of TimeSnippet-Cells
     * 
     */
    private static void addPlaceHolderForMissingBeginEndElements(StringBuilder builder, BusinessDayInc4Export inc) {
	for (int i = 0; i < inc.getTimeSnippetPlaceHolders().size(); i++) {

	    builder.append("");
	    builder.append(CONTENT_SEPPARATOR);
	}
    }

    /**
     * Collects all the necessary data in the proper format so the turbo-bucher can
     * charge-off the jira tickets
     * 
     * @param bussinessDay
     * @return
     */
    public static List<String> collectContent4TurboBucher(BusinessDay4Export bussinessDay) {

	StringBuilder builder = new StringBuilder();
	List<String> content = new ArrayList<>();

	List<BusinessDayInc4Export> notChargedIncrements = getNotChargedIncrements(bussinessDay);
	for (BusinessDayInc4Export inc : notChargedIncrements) {
	    builder.append(inc.getTicketNumber());
	    builder.append(CONTENT_SEPPARATOR_TURBO_BUCHER);
	    builder.append(inc.getChargeType());
	    builder.append(CONTENT_SEPPARATOR_TURBO_BUCHER);
	    builder.append(inc.getTotalDuration());
	    builder.append(CONTENT_SEPPARATOR_TURBO_BUCHER);

	    SimpleDateFormat df = (SimpleDateFormat) DateFormat.getTimeInstance(DateFormat.SHORT);
	    df.applyPattern("dd.MM.yyyy");
	    builder.append(df.format(bussinessDay.getDate()));
	    if (StringUtil.isNotEmptyOrNull(inc.getDescription())) {
		builder.append(CONTENT_SEPPARATOR_TURBO_BUCHER);
		builder.append(inc.getDescription());
	    }

	    builder.append(System.getProperty("line.separator"));
	    content.add(builder.toString());
	}
	return content;
    }

    private static List<BusinessDayInc4Export> getNotChargedIncrements(BusinessDay4Export bussinessDay) {
	return bussinessDay.getBusinessDayIncrements()//
		.stream().filter(bDayInc -> !bDayInc.isCharged())//
		.collect(Collectors.toList());
    }
}
