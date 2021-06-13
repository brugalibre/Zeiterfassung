package com.adcubum.timerecording.core.work.businessday;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.text.ParseException;
import java.util.Date;
import java.util.GregorianCalendar;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import com.adcubum.librarys.text.res.TextLabel;
import com.adcubum.timerecording.core.work.businessday.comeandgo.ComeAndGoes;
import com.adcubum.timerecording.core.work.businessday.comeandgo.impl.ComeAndGoesImpl;
import com.adcubum.timerecording.core.work.businessday.update.callback.TimeSnippedChangedCallbackHandler;
import com.adcubum.timerecording.core.work.businessday.update.callback.impl.BusinessDayIncrementAdd;
import com.adcubum.timerecording.core.work.businessday.update.callback.impl.BusinessDayIncrementAdd.BusinessDayIncrementAddBuilder;
import com.adcubum.timerecording.core.work.businessday.update.callback.impl.ChangedValue;
import com.adcubum.timerecording.jira.data.Ticket;
import com.adcubum.timerecording.test.BaseTestWithSettings;
import com.adcubum.timerecording.work.businessday.TimeSnippetBuilder;
import com.adcubum.timerecording.work.date.Time;
import com.adcubum.timerecording.work.date.TimeFactory;
import com.adcubum.util.parser.DateParser;

class BusinessDayTest extends BaseTestWithSettings {

   @Test
   void testGetComeAndGoMsgWithoutComeAndGoes() {
      // Given
      BusinessDay businessDay = new BusinessDay();

      // When
      Executable exe = () -> businessDay.getComeAndGoMsg();

      // Then
      assertThrows(IllegalStateException.class, exe);
   }

   @Test
   void testGetCapturingSinceMsgWithUnfinishedIncrement() {
      // Given
      BusinessDay businessDay = new BusinessDay();
      businessDay.startNewIncremental();
      BusinessDayIncrement currentBussinessDayIncremental = businessDay.getCurrentBussinessDayIncremental();
      TimeSnippet currentTimeSnippet = currentBussinessDayIncremental.getCurrentTimeSnippet();

      // When
      String actualCapturingInactiveSinceMsg = businessDay.getCapturingActiveSinceMsg();

      // Then
      assertThat(actualCapturingInactiveSinceMsg, is(TextLabel.CAPTURING_ACTIVE_SINCE + " " + currentTimeSnippet.getBeginTimeStamp()));
   }

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
      Ticket currentTicket = mockTicket(true);
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
      Ticket currentTicket = mockTicket(false);
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
   public void testAddNewDBInc() {

      // Given
      int firstTimeBetweenStartAndStop = 3600 * 1000;
      TimeSnippet firstSnippet = TimeSnippetBuilder.of()
            .withYear(2021)
            .withMonth(1)
            .withDay(1)
            .withStartHourAndDuration(2, firstTimeBetweenStartAndStop)
            .build();
      TimeSnippet secondSnippet = TimeSnippetBuilder.of()
            .withYear(2021)
            .withMonth(2)
            .withDay(1)
            .withStartHourAndDuration(2, firstTimeBetweenStartAndStop)
            .build();

      BusinessDay businessDay = new BusinessDay();
      businessDay.addBusinessIncrement(createUpdate(firstSnippet, 113, getTicket4Nr()));

      // When
      Executable ex = () -> businessDay.addBusinessIncrement(createUpdate(secondSnippet, 113, getTicket4Nr()));

      // Then
      assertThrows(IllegalStateException.class, ex);
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

   @Test
   void testHasComeAndGoesFromPrecedentDays_PrecedentComeAndGoes() throws ParseException {

      // Given
      boolean expectedHasComeAndGoesFromPrecedentDays = true;
      String dateAsString = "01-02-2020";
      String comeHourAndMinAsString = "10:00";
      String goHourAndMinAsString = "12:00";
      Time come = createTime(dateAsString, comeHourAndMinAsString);
      Time go = createTime(dateAsString, goHourAndMinAsString);
      ComeAndGoes comeAndGoes = ComeAndGoesImpl.of()
            .comeOrGo(come)
            .comeOrGo(go);
      BusinessDay businessDay = new BusinessDay(new Date(), comeAndGoes);


      // When
      boolean actualHasComeAndGoesFromPrecedentDays = businessDay.hasComeAndGoesFromPrecedentDays();

      // Then
      assertThat(actualHasComeAndGoesFromPrecedentDays, is(expectedHasComeAndGoesFromPrecedentDays));
   }

   @Test
   void testHasComeAndGoesFromPrecedentDays_PrecedentNoComeAndGoes() throws ParseException {

      // Given
      boolean expectedHasComeAndGoesFromPrecedentDays = false;
      Time come =TimeFactory.createNew();
      Time go =TimeFactory.createNew();
      ComeAndGoes comeAndGoes = ComeAndGoesImpl.of()
            .comeOrGo(come)
            .comeOrGo(go);
      BusinessDay businessDay = new BusinessDay(new Date(), comeAndGoes);


      // When
      boolean actualHasComeAndGoesFromPrecedentDays = businessDay.hasComeAndGoesFromPrecedentDays();

      // Then
      assertThat(actualHasComeAndGoesFromPrecedentDays, is(expectedHasComeAndGoesFromPrecedentDays));
   }

   private Time createTime(String dateAsString, String comeHourAndMinAsString) throws ParseException {
      Date parsedDate = DateParser.parse2Date(dateAsString + " " + comeHourAndMinAsString, DateParser.DATE_PATTERN);
      return TimeFactory.createNew(parsedDate.getTime());
   }

   private Ticket getTicket4Nr() {
      return Ticket.dummy("SYRIUS-1324");
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
      Time beginTimeStamp = TimeFactory.createNew(startDate.getTimeInMillis());
      TimeSnippet timeSnippet = new TimeSnippet(new Date(beginTimeStamp.getTime()));
      timeSnippet.setBeginTimeStamp(beginTimeStamp);
      timeSnippet.setEndTimeStamp(TimeFactory.createNew(startDate.getTimeInMillis() + timeBetweenBeginAndEnd));
      return timeSnippet;
   }

   private TimeSnippet createTimeSnippet(long startTime, long stopTime) {
      Time beginTimeStamp = TimeFactory.createNew(startTime);
      Time endTimeStamp = TimeFactory.createNew(stopTime);
      TimeSnippet timeSnippet = new TimeSnippet(new Date(beginTimeStamp.getTime()));
      timeSnippet.setBeginTimeStamp(beginTimeStamp);
      timeSnippet.setEndTimeStamp(endTimeStamp);
      return timeSnippet;
   }

   private Ticket mockTicket(boolean isDummy) {
      Ticket currentTicket = mock(Ticket.class);
      when(currentTicket.isDummyTicket()).thenReturn(isDummy);
      when(currentTicket.getNr()).thenReturn("1234");
      return currentTicket;
   }

   private static class TestTimeSnippedChangedCallbackHandler implements TimeSnippedChangedCallbackHandler {
      private ChangedValue changeValue;

      @Override
      public void handleTimeSnippedChanged(ChangedValue changeValue) {
         this.changeValue = changeValue;
      }
   }
}
