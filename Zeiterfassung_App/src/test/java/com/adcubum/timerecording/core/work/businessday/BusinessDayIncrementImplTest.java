package com.adcubum.timerecording.core.work.businessday;

import com.adcubum.timerecording.jira.data.ticket.Ticket;
import com.adcubum.timerecording.jira.data.ticket.TicketActivity;
import com.adcubum.timerecording.work.date.DateTimeFactory;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;

class BusinessDayIncrementImplTest {

   @Test
   void testFullCopy() {
      // Given
      BusinessDayIncrement businessDayIncrement = new BusinessDayIncrementImpl()
              .setDescription("test")
              .startCurrentTimeSnippet(DateTimeFactory.createNew())
              .flagAsBooked();

      // When
      BusinessDayIncrement businessDayIncrementCopy = BusinessDayIncrementImpl.of(businessDayIncrement, true);

      // Then
      assertThat(businessDayIncrementCopy.getDescription(), is(businessDayIncrement.getDescription()));
      assertThat(businessDayIncrementCopy.getCurrentTimeSnippet(), is(businessDayIncrement.getCurrentTimeSnippet()));
      assertThat(businessDayIncrementCopy.isBooked(), is(businessDayIncrement.isBooked()));
   }

   @Test
   void testImmutabilityOfBookedIncrement() {
      // Given
      BusinessDayIncrement businessDayIncrement = new BusinessDayIncrementImpl()
              .setDescription("test")
              .startCurrentTimeSnippet(DateTimeFactory.createNew())
              .stopCurrentTimeSnippet(DateTimeFactory.createNew())
              .setTicketActivity(mock(TicketActivity.class))
              .setTicket(mock(Ticket.class))
              .flagAsBooked();

      // When
      BusinessDayIncrement changedBusinessDayIncrement = businessDayIncrement.setDescription("NewDescription")
              .updateBeginTimeSnippetAndCalculate("01:00")
              .updateEndTimeSnippetAndCalculate("17:00")
              .addAdditionallyTime(1000)
              .setTicket(mock(Ticket.class))
              .setTicketActivity(mock(TicketActivity.class));

      // Then
      assertThat(changedBusinessDayIncrement.getDescription(), is(businessDayIncrement.getDescription()));
      assertThat(changedBusinessDayIncrement.getCurrentTimeSnippet(), is(businessDayIncrement.getCurrentTimeSnippet()));
      assertThat(changedBusinessDayIncrement.getTicket(), is(businessDayIncrement.getTicket()));
      assertThat(changedBusinessDayIncrement.getTicketActivity(), is(businessDayIncrement.getTicketActivity()));
      assertThat(changedBusinessDayIncrement.getTotalDuration(), is(0.0f));
   }
}