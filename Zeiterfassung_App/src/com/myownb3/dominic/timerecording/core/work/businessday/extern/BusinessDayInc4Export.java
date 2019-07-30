/**
 * 
 */
package com.myownb3.dominic.timerecording.core.work.businessday.extern;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import com.myownb3.dominic.timerecording.core.work.businessday.BusinessDayIncrement;
import com.myownb3.dominic.timerecording.core.work.businessday.TimeSnippet;
import com.myownb3.dominic.util.parser.NumberFormat;
import com.myownb3.dominic.util.utils.StringUtil;

/**
 * The {@link BusinessDayInc4Export} is used whenever a
 * {@link BusinessDayIncrement} is going to be exported. Either on a UI or on a
 * text file
 * 
 * @author Dominic
 *
 */
public class BusinessDayInc4Export {

    private List<TimeSnippet4Export> timeSnippets;
    private List<TimeSnippetPlaceHolder> timeSnippetPlaceHolders;

    private TimeSnippet currentTimeSnippet;

    private float totalDuration;
    private String description;
    private String ticketNumber;
    private int chargeType;
    private boolean isCharged;

    private BusinessDayInc4Export(BusinessDayIncrement businessDayIncremental) {

	this.currentTimeSnippet = TimeSnippet.of(businessDayIncremental.getCurrentTimeSnippet());
	this.description = businessDayIncremental.getDescription();
	this.ticketNumber = businessDayIncremental.getTicketNumber();
	this.chargeType = businessDayIncremental.getChargeType();
	this.totalDuration = businessDayIncremental.getTotalDuration();
	this.isCharged = businessDayIncremental.isCharged();

	timeSnippets = businessDayIncremental.getTimeSnippets()//
		.stream()//
		.map(TimeSnippet4Export::new)//
		.collect(Collectors.toList());
	Collections.sort(timeSnippets, new TimeSnippet4Export.TimeStampComparator());
	timeSnippetPlaceHolders = Collections.emptyList();
    }

    /**
     * Returns <code>true</code> if this {@link BusinessDayIncrement} has a valid
     * description or <code>false</code> if not
     * 
     * @return<code>true</code> if this {@link BusinessDayIncrement} has a valid
     *                          description or <code>false</code> if not
     */
    public boolean hasDescription() {
	return StringUtil.isNotEmptyOrNull(description);
    }

    /**
     * All rows must fit with it content to the title header. Thats why we have to
     * add some placeholders if this row has less TimeSnipets then the maximum
     * amount of TimeSnippet-Cells
     * 
     * @param businessDayExportStruct the {@link BusinessDay4Export} this increment
     *                                belongs to
     * 
     */
    public void addPlaceHolderForMissingCell(BusinessDay4Export businessDayExportStruct) {

	int amountOfEmptyTimeSnippets = businessDayExportStruct.getAmountOfVonBisElements() - timeSnippets.size();
	List<TimeSnippetPlaceHolder> timeSnippetPlaceHolders = new ArrayList<>();
	for (int i = 0; i < amountOfEmptyTimeSnippets; i++) {
	    timeSnippetPlaceHolders.add(new TimeSnippetPlaceHolder());
	    timeSnippetPlaceHolders.add(new TimeSnippetPlaceHolder());
	}
	this.timeSnippetPlaceHolders = timeSnippetPlaceHolders;
    }

    public final String getTotalDurationRep() {
	return NumberFormat.format(this.totalDuration);
    }

    public final List<TimeSnippetPlaceHolder> getTimeSnippetPlaceHolders() {
	return this.timeSnippetPlaceHolders;
    }

    public final String getDescription() {
	return this.description != null ? description : "";
    }

    public final String getTicketNumber() {
	return this.ticketNumber;
    }

    public final int getChargeType() {
	return this.chargeType;
    }

    public final List<TimeSnippet4Export> getTimeSnippets() {
	return this.timeSnippets;
    }

    public boolean isCharged() {
	return isCharged;
    }

    public final TimeSnippet getCurrentTimeSnippet() {
	return this.currentTimeSnippet;
    }

    /**
     * Returns a new {@link BusinessDayInc4Export} for the given
     * {@link BusinessDayIncrement}
     * 
     * @param currentBussinessDayIncremental
     * @return a new {@link BusinessDayInc4Export} for the given
     *         {@link BusinessDayIncrement}
     */
    public static BusinessDayInc4Export of(BusinessDayIncrement currentBussinessDayIncremental) {
	BusinessDayInc4Export businessDayInc4Export = new BusinessDayInc4Export(currentBussinessDayIncremental);
	return businessDayInc4Export;
    }
}
