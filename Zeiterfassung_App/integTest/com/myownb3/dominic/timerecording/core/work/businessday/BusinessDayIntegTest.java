package com.myownb3.dominic.timerecording.core.work.businessday;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Date;
import java.util.GregorianCalendar;

import org.junit.jupiter.api.Test;

import com.myownb3.dominic.timerecording.core.work.businessday.update.callback.impl.BusinessDayIncrementAdd;
import com.myownb3.dominic.timerecording.core.work.businessday.update.callback.impl.BusinessDayIncrementAdd.BusinessDayIncrementAddBuilder;
import com.myownb3.dominic.timerecording.core.work.businessday.update.callback.impl.ChangedValue;
import com.myownb3.dominic.timerecording.core.work.date.Time;
import com.myownb3.dominic.timerecording.test.BaseTestWithSettings;
import com.myownb3.dominic.timerecording.ticketbacklog.data.Ticket;

public class BusinessDayIntegTest extends BaseTestWithSettings {

   @Test
   public void testChangeDBIncTicketNr() {

      // Given
      String newTicketNr = "ABES-1324";
      TimeSnippet firstSnippet = createTimeSnippet(3600 * 1000, 10);

      BusinessDay businessDay = new BusinessDay();
      businessDay.addBusinessIncrement(createUpdate(firstSnippet, 113, getTicket4Nr()));

      ChangedValue changeValue = ChangedValue.of(0, newTicketNr, ValueTypes.TICKET_NR);

      // When
      businessDay.changeBusinesDayIncrement(changeValue);

      // Then
      BusinessDayIncrement businessDayIncrement = businessDay.getIncrements().get(0);
      assertThat(businessDayIncrement.getTicketNumber(), is(newTicketNr));
   }

   private BusinessDayIncrementAdd createUpdate(TimeSnippet timeSnippet, int kindOfService, Ticket ticket) {
      return new BusinessDayIncrementAddBuilder()
            .withTimeSnippet(timeSnippet)
            .withDescription("Default Description")
            .withTicket(ticket)
            .withKindOfService(kindOfService)
            .build();
   }

   private Ticket getTicket4Nr() {
      Ticket ticket = mock(Ticket.class);
      when(ticket.getNr()).thenReturn("SYRIUS-1324");
      return ticket;
   }

   private TimeSnippet createTimeSnippet(int timeBetweenBeginAndEnd, int hour) {
      GregorianCalendar startDate = new GregorianCalendar(2020, 1, 1, hour, 0, 0);// year, month, day, hours, min, second
      Time beginTimeStamp = new Time(startDate.getTimeInMillis());
      TimeSnippet timeSnippet = new TimeSnippet(new Date(beginTimeStamp.getTime()));
      timeSnippet.setBeginTimeStamp(beginTimeStamp);
      timeSnippet.setEndTimeStamp(new Time(startDate.getTimeInMillis() + timeBetweenBeginAndEnd));
      return timeSnippet;
   }
}
