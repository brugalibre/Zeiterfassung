package com.myownb3.dominic.timerecording.work.businessday;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.Date;

import org.junit.Test;

import com.myownb3.dominic.timerecording.callback.handler.impl.BusinessDayIncrementAdd;
import com.myownb3.dominic.timerecording.work.date.Time;

public class BusinessDayTest {

    @Test
    public void testHasElementsFromPrecedentDays_PrecedentElements() {

	// Given
	boolean expectedHasElementsFromPrecedentDays = true;
	BusinessDay businessDay = new BusinessDay();

	TimeSnippet timeSnippet = createTimeSnippet(0);
	BusinessDayIncrementAdd updateWithTimeSnippet = createUpdate(timeSnippet, 113, "SYRIUS-1324");		

	TimeSnippet timeSnippetYesterday = createTimeSnippet(-24*60*3600*1000);// One day in Miliseconds
	BusinessDayIncrementAdd updateWithTimeSnippetTomorrow = createUpdate(timeSnippetYesterday, 114, "SYRIUS-1324");

	businessDay.addBusinessIncrement(updateWithTimeSnippet);
	businessDay.addBusinessIncrement(updateWithTimeSnippetTomorrow);

	// When
	boolean actualHasElementsFromPrecedentDays = businessDay.hasElementsFromPrecedentDays();

	// Then
	assertThat(actualHasElementsFromPrecedentDays, is(expectedHasElementsFromPrecedentDays));
    }

    @Test
    public void testHasElementsFromPrecedentDays_NoPrecedentElements() {

	// Given
	boolean expectedHasElementsFromPrecedentDays = false;
	BusinessDay businessDay = new BusinessDay();

	// When
	boolean actualHasElementsFromPrecedentDays = businessDay.hasElementsFromPrecedentDays();

	// Then
	assertThat(actualHasElementsFromPrecedentDays, is(expectedHasElementsFromPrecedentDays));
    }

    private BusinessDayIncrementAdd createUpdate(TimeSnippet timeSnippet, int kindOfService, String ticketNo) {
	BusinessDayIncrementAdd update = new BusinessDayIncrementAdd();
	update.setTimeSnippet(timeSnippet);
	update.setTicketNo(ticketNo);
	update.setKindOfService(kindOfService);
	return update;
    }

    private TimeSnippet createTimeSnippet(int additionalTime) {
	Time beginTimeStamp = new Time(System.currentTimeMillis() + additionalTime);
	TimeSnippet timeSnippet = new TimeSnippet(new Date (beginTimeStamp.getTime()));
	timeSnippet.setBeginTimeStamp(beginTimeStamp);
	timeSnippet.setEndTimeStamp(new Time(System.currentTimeMillis() + additionalTime + 10));
	return timeSnippet;
    }
}