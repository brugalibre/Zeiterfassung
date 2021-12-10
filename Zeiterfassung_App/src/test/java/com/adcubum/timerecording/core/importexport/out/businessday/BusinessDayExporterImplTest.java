package com.adcubum.timerecording.core.importexport.out.businessday;

import com.adcubum.timerecording.core.work.businessday.BusinessDay;
import com.adcubum.timerecording.core.work.businessday.BusinessDayImpl;
import com.adcubum.timerecording.core.work.businessday.TimeSnippet;
import com.adcubum.timerecording.core.work.businessday.TimeSnippetImpl.TimeSnippetBuilder;
import com.adcubum.timerecording.core.work.businessday.update.callback.impl.BusinessDayIncrementAdd;
import com.adcubum.timerecording.core.work.businessday.update.callback.impl.BusinessDayIncrementAdd.BusinessDayIncrementAddBuilder;
import com.adcubum.timerecording.jira.data.ticket.Ticket;
import com.adcubum.timerecording.jira.data.ticket.TicketActivity;
import com.adcubum.timerecording.work.date.DateTime;
import com.adcubum.timerecording.work.date.DateTimeFactory;
import org.junit.jupiter.api.Test;

import java.util.GregorianCalendar;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class BusinessDayExporterImplTest {

   @Test
   void testCollectContent4Booking() {

      // Given
      GregorianCalendar startDate = new GregorianCalendar(2021, 2, 2);// year, month (starts at zero!), day, hours, min, second
      long firstTimeStampStart = startDate.getTimeInMillis();
      int firstTimeBetweenStartAndStop = 3600 * 1000;
      int serviceCode = 113;
      TicketActivity ticketActivity = mock(TicketActivity.class);
      when(ticketActivity.getActivityCode()).thenReturn(serviceCode);
      String ticketNr = "SYRIUS-1324";
      String description = "Default Description";
      String expectedContentLine = ticketNr + ";" + serviceCode + ";1;02.03.2021;" + description + "\r\n";
      TimeSnippet firstSnippet = createTimeSnippet(firstTimeStampStart, firstTimeStampStart + firstTimeBetweenStartAndStop);
      BusinessDayIncrementAdd firstInc = createUpdate(firstSnippet, ticketActivity, getTicket4Nr(ticketNr), description);

      BusinessDay businessDay = new BusinessDayImpl()
            .addBusinessIncrement(firstInc);

      // When
      List<String> content4TurboBucher = BusinessDayExporterImpl.INSTANCE.collectContent4Booking(businessDay);

      // Then
      assertThat(content4TurboBucher.size(), is(1));
      assertThat(content4TurboBucher.get(0), is(expectedContentLine));
   }

   private Ticket getTicket4Nr(String ticketNr) {
      Ticket ticket = mock(Ticket.class);
      when(ticket.getNr()).thenReturn(ticketNr);
      return ticket;
   }

   private BusinessDayIncrementAdd createUpdate(TimeSnippet timeSnippet, TicketActivity ticketActivity, Ticket ticket, String description) {
      return new BusinessDayIncrementAddBuilder()
            .withTimeSnippet(timeSnippet)
            .withDescription(description)
            .withTicket(ticket)
            .withTicketActivity(ticketActivity)
            .build();
   }

   private TimeSnippet createTimeSnippet(long startTime, long stopTime) {
      DateTime beginTimeStamp = DateTimeFactory.createNew(startTime);
      DateTime endTimeStamp = DateTimeFactory.createNew(stopTime);
      return TimeSnippetBuilder.of()
            .withBeginTime(beginTimeStamp)
            .withEndTime(endTimeStamp)
            .build();
   }
}
