/**
 * 
 */
package com.myownb3.dominic.timerecording.core.importexport.out.businessday;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.myownb3.dominic.librarys.text.res.TextLabel;
import com.myownb3.dominic.timerecording.core.charge.ChargeType;
import com.myownb3.dominic.timerecording.core.work.businessday.BusinessDay;
import com.myownb3.dominic.timerecording.core.work.businessday.extern.BusinessDay4Export;
import com.myownb3.dominic.timerecording.core.work.businessday.extern.BusinessDayInc4Export;
import com.myownb3.dominic.timerecording.core.work.businessday.extern.TimeSnippet4Export;

/**
 * The {@link BusinessDayExporter} exports a {@link BusinessDay} into a
 * {@link List} of {@link String}.
 * 
 * @author Dominic
 *
 */
public class BusinessDayExporter {

    public static final String DATE_REP_PATTERN = "EEEEE, d MMM yyyy HH:mm:ss";
    public static final BusinessDayExporter INSTANCE = new BusinessDayExporter();

    private BusinessDayExporter() {
	// private constructor
    }

    private static final Object CONTENT_SEPPARATOR = "; ";
    private static final Object CONTENT_SEPPARATOR_TURBO_BUCHER = ";";

    /**
     * Selects line by line the content to export for the given
     * {@link BusinessDay4Export}
     * 
     * @param bussinessDay
     *            the {@link BusinessDay4Export} which has to be exported
     * @return a list of {@link String} to export
     */
    public List<String> collectContent(BusinessDay4Export bussinessDay) {
	StringBuilder builder = new StringBuilder();
	List<String> content = new ArrayList<>();

	// First line to mark the date, when the time was recorded
	builder.append(bussinessDay.getDateRep(DATE_REP_PATTERN));
	builder.append(System.getProperty("line.separator"));
	builder.append(System.getProperty("line.separator"));

	appendTitleHeaderCells(builder, bussinessDay);

	// = For each 'Ticket' or Increment of an entire Day
	for (BusinessDayInc4Export inc : bussinessDay.getBusinessDayIncrements()) {
	    builder.append(TextLabel.TICKET + ": ");
	    builder.append(inc.getTicketNumber());
	    builder.append(CONTENT_SEPPARATOR);
	    builder.append(inc.getDescription());
	    builder.append(CONTENT_SEPPARATOR);
	    builder.append(inc.getTotalDurationRep());
	    builder.append(CONTENT_SEPPARATOR);

	    List<TimeSnippet4Export> timeSnippets = inc.getTimeSnippets();

	    // = For each single work units of a Ticket
	    for (TimeSnippet4Export snippet : timeSnippets) {
		builder.append(snippet.getBeginTimeStampRep());
		builder.append(CONTENT_SEPPARATOR);
		builder.append(snippet.getEndTimeStampRep());
		builder.append(CONTENT_SEPPARATOR);
	    }
	    addPlaceHolderForMissingBeginEndElements(builder, inc);
	    builder.append(ChargeType.getRepresentation(inc.getChargeType()));
	    builder.append(CONTENT_SEPPARATOR);
	    builder.append(inc.isCharged() ? TextLabel.YES : TextLabel.NO);

	    builder.append(System.getProperty("line.separator"));
	    content.add(builder.toString());
	    builder.delete(0, builder.capacity());
	}
	builder.append(System.getProperty("line.separator"));
	builder.append(TextLabel.TOTAL_AMOUNT_OF_HOURS_LABEL + " " + bussinessDay.getTotalDurationRep());
	content.add(builder.toString());
	return content;
    }

    private void appendTitleHeaderCells(StringBuilder builder, BusinessDay4Export bussinessDay) {

	builder.append(TextLabel.TICKET);
	builder.append(CONTENT_SEPPARATOR);
	builder.append(TextLabel.DESCRIPTION_LABEL);
	builder.append(CONTENT_SEPPARATOR);
	builder.append(TextLabel.AMOUNT_OF_HOURS_LABEL);
	builder.append(CONTENT_SEPPARATOR);

	appendBeginEndHeader(builder, bussinessDay);

	builder.append(TextLabel.CHARGE_TYPE_LABEL);
	builder.append(CONTENT_SEPPARATOR);
	builder.append(TextLabel.CHARGED);
	builder.append(System.getProperty("line.separator"));
    }

    private void appendBeginEndHeader(StringBuilder builder, BusinessDay4Export bussinessDay) {

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
    private void addPlaceHolderForMissingBeginEndElements(StringBuilder builder, BusinessDayInc4Export inc) {
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
     * @return the content which is required by the {@link Booker} in order to book
     */
    public List<String> collectContent4TurboBucher(BusinessDay4Export bussinessDay) {

	StringBuilder builder = new StringBuilder();
	List<String> content = new ArrayList<>();

	List<BusinessDayInc4Export> notChargedIncrements = getNotChargedIncrements(bussinessDay);
	for (BusinessDayInc4Export inc : notChargedIncrements) {
	    builder.append(inc.getTicketNumber());
	    builder.append(CONTENT_SEPPARATOR_TURBO_BUCHER);
	    builder.append(inc.getChargeType());
	    builder.append(CONTENT_SEPPARATOR_TURBO_BUCHER);
	    builder.append(inc.getTotalDurationRep());
	    builder.append(CONTENT_SEPPARATOR_TURBO_BUCHER);

	    builder.append(bussinessDay.getDateRep());
	    if (inc.hasDescription()) {
		builder.append(CONTENT_SEPPARATOR_TURBO_BUCHER);
		builder.append(inc.getDescription());
	    }
	    builder.append(System.getProperty("line.separator"));
	    content.add(builder.toString());
	    builder.delete(0, builder.capacity());
	}
	return content;
    }

    private List<BusinessDayInc4Export> getNotChargedIncrements(BusinessDay4Export bussinessDay) {
	return bussinessDay.getBusinessDayIncrements()//
		.stream()//
		.filter(bDayInc -> !bDayInc.isCharged())//
		.collect(Collectors.toList());
    }
}
