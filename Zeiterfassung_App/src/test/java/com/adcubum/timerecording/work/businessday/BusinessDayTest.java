package com.adcubum.timerecording.work.businessday;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Date;
import java.util.GregorianCalendar;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import com.adcubum.timerecording.core.work.businessday.BusinessDay;
import com.adcubum.timerecording.core.work.businessday.BusinessDayIncrement;
import com.adcubum.timerecording.core.work.businessday.TimeSnippet;
import com.adcubum.timerecording.core.work.businessday.ValueTypes;
import com.adcubum.timerecording.core.work.businessday.update.callback.TimeSnippedChangedCallbackHandler;
import com.adcubum.timerecording.core.work.businessday.update.callback.impl.BusinessDayIncrementAdd;
import com.adcubum.timerecording.core.work.businessday.update.callback.impl.BusinessDayIncrementAdd.BusinessDayIncrementAddBuilder;
import com.adcubum.timerecording.core.work.businessday.update.callback.impl.ChangedValue;
import com.adcubum.timerecording.jira.data.Ticket;
import com.adcubum.timerecording.work.date.Time;

class BusinessDayTest {
   @Test
   public void testChangeDBIncTicketNr() {

      // Given
      String newTicketNr = "ABES-1324";
      BusinessDay businessDay = new BusinessDay();
      businessDay.addBusinessIncrement(createUpdate(createTimeSnippet(3600 * 1000, 10), 113, getTicket4Nr()));

      ChangedValue changeValue = ChangedValue.of(0, newTicketNr, ValueTypes.TICKET_NR);

      // When
      Executable exe = () -> businessDay.changeBusinesDayIncrement(changeValue);

      // Then
      assertThrows(UnsupportedOperationException.class, exe);
   }

   @Test
   public void testChangeNothingJustNullValues() {

      // Given
      BusinessDay businessDay = new BusinessDay();
      businessDay.addBusinessIncrement(createUpdate(createTimeSnippet(3600 * 1000, 10), 113, getTicket4Nr()));


      // When
      Executable exe = () -> ChangedValue.of(0, "", ValueTypes.NONE);

      // Then
      assertThrows(IllegalStateException.class, exe);
   }

   @Test
   public void testChangeDBIncTicket() {

      // Given
      Ticket newTicket = Ticket.dummy("ABES-1324");
      TimeSnippet firstSnippet = createTimeSnippet(3600 * 1000, 10);

      BusinessDay businessDay = new BusinessDay();
      businessDay.addBusinessIncrement(createUpdate(firstSnippet, 113, getTicket4Nr()));

      ChangedValue changeValue = ChangedValue.of(0, newTicket, ValueTypes.TICKET);

      // When
      businessDay.changeBusinesDayIncrement(changeValue);

      // Then
      BusinessDayIncrement businessDayIncrement = businessDay.getIncrements().get(0);
      assertThat(businessDayIncrement.getTicket().getNr(), is(newTicket.getNr()));
   }

   @Test
   public void testRefreshCurrentTicketIsDummyTickets() {

      // Given
      long firstTimeStampStart = System.currentTimeMillis();
      TimeSnippet firstSnippet = createTimeSnippet(firstTimeStampStart, firstTimeStampStart + 3600 * 1000);
      Ticket currentTicket = getTicket4Nr();
      when(currentTicket.isDummyTicket()).thenReturn(true);
      BusinessDayIncrementAdd firstInc = createUpdate(firstSnippet, 113, currentTicket);

      BusinessDay businessDay = new BusinessDay();
      businessDay.addBusinessIncrement(firstInc);

      // When
      businessDay.refreshDummyTickets();

      // Then
      BusinessDayIncrement businessDayIncrement = businessDay.getIncrements().get(0);
      assertThat(businessDayIncrement.getTicket(), is(not(currentTicket)));
      assertThat(businessDayIncrement.getTicket().getNr(), is(currentTicket.getNr()));
   }

   @Test
   public void testRefreshCurrentTicketIsNotDummyTickets() {

      // Given
      long firstTimeStampStart = System.currentTimeMillis();
      TimeSnippet firstSnippet = createTimeSnippet(firstTimeStampStart, firstTimeStampStart + 3600 * 1000);
      Ticket currentTicket = getTicket4Nr();
      when(currentTicket.isDummyTicket()).thenReturn(false);
      BusinessDayIncrementAdd firstInc = createUpdate(firstSnippet, 113, currentTicket);

      BusinessDay businessDay = new BusinessDay();
      businessDay.addBusinessIncrement(firstInc);

      // When
      businessDay.refreshDummyTickets();

      // Then
      BusinessDayIncrement businessDayIncrement = businessDay.getIncrements().get(0);
      assertThat(businessDayIncrement.getTicket(), is(currentTicket));
      assertThat(businessDayIncrement.getTicket().getNr(), is(currentTicket.getNr()));
   }

   @Test
   public void testRefreshCurrentTicketNoTicketAtAll() {

      // Given
      long firstTimeStampStart = System.currentTimeMillis();
      TimeSnippet firstSnippet = createTimeSnippet(firstTimeStampStart, firstTimeStampStart + 3600 * 1000);
      BusinessDayIncrementAdd firstInc = createUpdate(firstSnippet, 113, null);

      BusinessDay businessDay = new BusinessDay();
      businessDay.addBusinessIncrement(firstInc);

      // When
      businessDay.refreshDummyTickets();

      // Then
      BusinessDayIncrement businessDayIncrement = businessDay.getIncrements().get(0);
      assertThat(businessDayIncrement.getTicket(), is(nullValue()));
   }

   @Test
   public void testChangeDBIncDurationIncreaseDuration() {

      // Given
      String newTotalBDDuration = "2";
      long firstTimeStampStart = System.currentTimeMillis();
      int firstTimeBetweenStartAndStop = 3600 * 1000;
      TimeSnippet firstSnippet = createTimeSnippet(firstTimeStampStart, firstTimeStampStart + firstTimeBetweenStartAndStop);
      BusinessDayIncrementAdd firstInc = createUpdate(firstSnippet, 113, getTicket4Nr());

      BusinessDay businessDay = new BusinessDay();
      businessDay.addBusinessIncrement(firstInc);

      ChangedValue changeValue = ChangedValue.of(0, newTotalBDDuration, ValueTypes.AMOUNT_OF_TIME);

      // When
      businessDay.changeBusinesDayIncrement(changeValue);

      // Then
      BusinessDayIncrement businessDayIncrement = businessDay.getIncrements().get(0);
      TimeSnippet lastTimeSnippet = businessDayIncrement.getCurrentTimeSnippet();
      assertThat(businessDayIncrement.getTotalDuration(), is(Float.parseFloat(newTotalBDDuration)));
      assertThat(lastTimeSnippet.getDuration(), is(2.0f));
   }

   @Test
   public void testChangeDBIncDescription() {

      // Given
      String newDescription = "newDescription";
      long firstTimeStampStart = System.currentTimeMillis();
      int firstTimeBetweenStartAndStop = 3600 * 1000;
      TimeSnippet firstSnippet = createTimeSnippet(firstTimeStampStart, firstTimeStampStart + firstTimeBetweenStartAndStop);

      BusinessDay businessDay = new BusinessDay();
      businessDay.addBusinessIncrement(createUpdate(firstSnippet, 113, getTicket4Nr()));

      ChangedValue changeValue = ChangedValue.of(0, newDescription, ValueTypes.DESCRIPTION);

      // When
      businessDay.changeBusinesDayIncrement(changeValue);

      // Then
      BusinessDayIncrement businessDayIncrement = businessDay.getIncrements().get(0);
      assertThat(businessDayIncrement.getDescription(), is(newDescription));
   }

   @Test
   public void testChangeDBIncBegin() {

      // Given
      String newBegin = "10:30";
      float expectedNewDuration = 0.5f;
      int firstTimeBetweenStartAndStop = 3600 * 1000;
      TimeSnippet firstSnippet = createTimeSnippet(firstTimeBetweenStartAndStop, 10);

      BusinessDay businessDay = new BusinessDay();
      businessDay.addBusinessIncrement(createUpdate(firstSnippet, 113, getTicket4Nr()));
      TestTimeSnippedChangedCallbackHandler callbackHandler = new TestTimeSnippedChangedCallbackHandler();
      businessDay.getIncrements().get(0).getCurrentTimeSnippet().setCallbackHandler(callbackHandler);

      ChangedValue changeValue = ChangedValue.of(0, newBegin, ValueTypes.BEGIN);

      // When
      businessDay.changeBusinesDayIncrement(changeValue);

      // Then
      BusinessDayIncrement businessDayIncrement = businessDay.getIncrements().get(0);
      assertThat(businessDayIncrement.getTotalDuration(), is(expectedNewDuration));
      assertThat(callbackHandler.changeValue.getValueTypes(), is(ValueTypes.BEGIN));
      assertThat(callbackHandler.changeValue.getNewValue(), is(newBegin));
   }

   @Test
   public void testChangeDBIncEnd() {

      // Given
      String newBegin = "11:30";
      float expectedNewDuration = 1.5f;
      int firstTimeBetweenStartAndStop = 3600 * 1000;
      TimeSnippet firstSnippet = createTimeSnippet(firstTimeBetweenStartAndStop, 10);

      BusinessDay businessDay = new BusinessDay();
      businessDay.addBusinessIncrement(createUpdate(firstSnippet, 113, getTicket4Nr()));

      ChangedValue changeValue = ChangedValue.of(0, newBegin, ValueTypes.END);

      // When
      businessDay.changeBusinesDayIncrement(changeValue);

      // Then
      BusinessDayIncrement businessDayIncrement = businessDay.getIncrements().get(0);
      assertThat(businessDayIncrement.getTotalDuration(), is(expectedNewDuration));
   }

   @Test
   public void testChangeDBIncDurationSnippets_DecreaseDuration() {

      // Given
      String newTotalBDDuration = "1.4";
      long firstTimeStampStart = System.currentTimeMillis();
      int firstTimeBetweenStartAndStop = 3600 * 1000;
      TimeSnippet firstSnippet = createTimeSnippet(firstTimeStampStart, firstTimeStampStart + firstTimeBetweenStartAndStop);
      BusinessDayIncrementAdd firstInc = createUpdate(firstSnippet, 113, getTicket4Nr());

      BusinessDay businessDay = new BusinessDay();
      businessDay.addBusinessIncrement(firstInc);

      ChangedValue changeValue = ChangedValue.of(0, newTotalBDDuration, ValueTypes.AMOUNT_OF_TIME);

      // When
      businessDay.changeBusinesDayIncrement(changeValue);

      // Then
      BusinessDayIncrement businessDayIncrement = businessDay.getIncrements().get(0);
      TimeSnippet lastTimeSnippet = businessDayIncrement.getCurrentTimeSnippet();
      assertThat(businessDayIncrement.getTotalDuration(), is(Float.parseFloat(newTotalBDDuration)));
      assertThat(lastTimeSnippet.getDuration(), is(1.4f));
   }

   @Test
   public void testChangeDBIncDurationWith2TimeSnippets_DecreaseNotPossibleSinceItsToMuch() {

      // Given
      // This is not possible, since we would have to drop BusinessDayIncrements and time-stamps in order to achive this duration
      String newTotalBDDuration = "-1.0";
      int firstTimeBetweenStartAndStop = 3600 * 1000 * 2;
      long firstTimeStampStart = System.currentTimeMillis() + firstTimeBetweenStartAndStop;
      TimeSnippet secondSnippet = createTimeSnippet(firstTimeStampStart, firstTimeStampStart + firstTimeBetweenStartAndStop);
      BusinessDayIncrementAdd secondInc = createUpdate(secondSnippet, 113, getTicket4Nr());

      BusinessDay businessDay = new BusinessDay();
      businessDay.addBusinessIncrement(secondInc);

      ChangedValue changeValue = ChangedValue.of(0, newTotalBDDuration, ValueTypes.AMOUNT_OF_TIME);

      // When
      businessDay.changeBusinesDayIncrement(changeValue);

      // Then - everything remains as it is
      BusinessDayIncrement businessDayIncrement = businessDay.getIncrements().get(0);
      TimeSnippet lastTimeSnippet = businessDayIncrement.getCurrentTimeSnippet();
      assertThat(businessDayIncrement.getTotalDuration(), is(2.0f));
      assertThat(lastTimeSnippet.getDuration(), is(2.0f));
   }

   @Test
   public void testHasNotChargedElements() {

      // Given
      boolean expectedHasNotChargedElements = true;
      BusinessDay businessDay = new BusinessDay();

      TimeSnippet timeSnippet = createTimeSnippet(0);
      BusinessDayIncrementAdd updateWithTimeSnippet = createUpdate(timeSnippet, 113, getTicket4Nr());

      TimeSnippet timeSnippetYesterday = createTimeSnippet(0);
      BusinessDayIncrementAdd updateWithTimeSnippetTomorrow = createUpdate(timeSnippetYesterday, 114, getTicket4Nr());

      businessDay.addBusinessIncrement(updateWithTimeSnippet);
      businessDay.flagBusinessDayAsCharged();
      businessDay.addBusinessIncrement(updateWithTimeSnippetTomorrow);

      // When
      boolean actualHasNotChargedElements = businessDay.hasNotChargedElements();

      // Then
      assertThat(actualHasNotChargedElements, is(expectedHasNotChargedElements));
   }

   @Test
   public void testHasNoNotChargedElements() {

      // Given
      boolean expectedHasNotChargedElements = false;
      BusinessDay businessDay = new BusinessDay();

      TimeSnippet timeSnippet = createTimeSnippet(0);
      BusinessDayIncrementAdd updateWithTimeSnippet = createUpdate(timeSnippet, 113, getTicket4Nr());

      TimeSnippet timeSnippetYesterday = createTimeSnippet(0);
      BusinessDayIncrementAdd updateWithTimeSnippetTomorrow = createUpdate(timeSnippetYesterday, 114, getTicket4Nr());

      businessDay.addBusinessIncrement(updateWithTimeSnippet);
      businessDay.addBusinessIncrement(updateWithTimeSnippetTomorrow);
      businessDay.flagBusinessDayAsCharged();

      // When
      boolean actualHasNotChargedElements = businessDay.hasNotChargedElements();

      // Then
      assertThat(actualHasNotChargedElements, is(expectedHasNotChargedElements));
   }

   @Test
   public void testHasElementsFromPrecedentDays_PrecedentElements() {

      // Given
      boolean expectedHasElementsFromPrecedentDays = true;
      BusinessDay businessDay = new BusinessDay();

      TimeSnippet timeSnippet = createTimeSnippet(0);
      BusinessDayIncrementAdd updateWithTimeSnippet = createUpdate(timeSnippet, 113, getTicket4Nr());

      TimeSnippet timeSnippetYesterday = createTimeSnippet(-24 * 60 * 3600 * 1000);// One day in Miliseconds
      BusinessDayIncrementAdd updateWithTimeSnippetTomorrow = createUpdate(timeSnippetYesterday, 114, getTicket4Nr());

      businessDay.addBusinessIncrement(updateWithTimeSnippet);
      businessDay.addBusinessIncrement(updateWithTimeSnippetTomorrow);

      // When
      boolean actualHasElementsFromPrecedentDays = businessDay.hasElementsFromPrecedentDays();

      // Then
      assertThat(actualHasElementsFromPrecedentDays, is(expectedHasElementsFromPrecedentDays));
   }

   private Ticket getTicket4Nr() {
      Ticket ticket = mock(Ticket.class);
      when(ticket.getNr()).thenReturn("SYRIUS-1324");
      return ticket;
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

   private BusinessDayIncrementAdd createUpdate(TimeSnippet timeSnippet, int kindOfService, Ticket ticket) {
      return new BusinessDayIncrementAddBuilder()
            .withTimeSnippet(timeSnippet)
            .withDescription("Default Description")
            .withTicket(ticket)
            .withKindOfService(kindOfService)
            .build();
   }

   private TimeSnippet createTimeSnippet(int timeBetweenBeginAndEnd) {
      return createTimeSnippet(timeBetweenBeginAndEnd, 0);
   }

   private TimeSnippet createTimeSnippet(int timeBetweenBeginAndEnd, int hour) {
      GregorianCalendar startDate = new GregorianCalendar(2020, 1, 1, hour, 0, 0);// year, month, day, hours, min, second
      Time beginTimeStamp = new Time(startDate.getTimeInMillis());
      TimeSnippet timeSnippet = new TimeSnippet(new Date(beginTimeStamp.getTime()));
      timeSnippet.setBeginTimeStamp(beginTimeStamp);
      timeSnippet.setEndTimeStamp(new Time(startDate.getTimeInMillis() + timeBetweenBeginAndEnd));
      return timeSnippet;
   }

   private TimeSnippet createTimeSnippet(long startTime, long stopTime) {
      Time beginTimeStamp = new Time(startTime);
      Time endTimeStamp = new Time(stopTime);
      TimeSnippet timeSnippet = new TimeSnippet(new Date(beginTimeStamp.getTime()));
      timeSnippet.setBeginTimeStamp(beginTimeStamp);
      timeSnippet.setEndTimeStamp(endTimeStamp);
      return timeSnippet;
   }

   private static class TestTimeSnippedChangedCallbackHandler implements TimeSnippedChangedCallbackHandler {
      private ChangedValue changeValue;

      @Override
      public void handleTimeSnippedChanged(ChangedValue changeValue) {
         this.changeValue = changeValue;
      }
   }
}
