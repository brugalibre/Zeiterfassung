package com.adcubum.timerecording.app;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import com.adcubum.librarys.text.res.TextLabel;
import com.adcubum.timerecording.core.book.adapter.BookerAdapter;
import com.adcubum.timerecording.core.book.adapter.ServiceCodeAdapter;
import com.adcubum.timerecording.core.book.result.BookResultType;
import com.adcubum.timerecording.core.book.result.BookerResult;
import com.adcubum.timerecording.core.callbackhandler.UiCallbackHandler;
import com.adcubum.timerecording.core.work.businessday.BusinessDay;
import com.adcubum.timerecording.core.work.businessday.TimeSnippet;
import com.adcubum.timerecording.core.work.businessday.ValueTypes;
import com.adcubum.timerecording.core.work.businessday.update.callback.impl.BusinessDayIncrementAdd;
import com.adcubum.timerecording.core.work.businessday.update.callback.impl.BusinessDayIncrementAdd.BusinessDayIncrementAddBuilder;
import com.adcubum.timerecording.core.work.businessday.update.callback.impl.ChangedValue;
import com.adcubum.timerecording.core.work.businessday.vo.BusinessDayIncrementVO;
import com.adcubum.timerecording.core.work.businessday.vo.BusinessDayVO;
import com.adcubum.timerecording.jira.data.Ticket;
import com.adcubum.timerecording.message.Message;
import com.adcubum.timerecording.message.MessageType;
import com.adcubum.timerecording.test.BaseTestWithSettings;
import com.adcubum.timerecording.work.date.Time;

class TimeRecorderTest extends BaseTestWithSettings {

   @AfterEach
   @Override
   public void cleanUp() throws IOException {
      super.cleanUp();
      TimeRecorder.INSTANCE.clear();
      TimeRecorder.INSTANCE.init();
   }

   @Test
   void testRefreshDummyTickets() {

      // Given
      BusinessDay businessDay = mock(BusinessDay.class);
      TimeRecorder timeRecorder = new TimeRecorder(mock(BookerAdapter.class), businessDay);

      // When
      timeRecorder.onSuccessfullyLogin();

      // Then
      verify(businessDay).refreshDummyTickets();
   }

   @Test
   void testRemoveBusinessIncrementAtIndexOutOfBounds() {
      // Given
      String ticketNr = "SYRIUS-4321";
      String description = "Test";
      int kindOfService = 113;
      int timeSnippedDuration = 3600 * 1000;
      new TestCaseBuilder(TimeRecorder.INSTANCE)
            .withBusinessDayIncrement(ticketNr, description, kindOfService, timeSnippedDuration)
            .build();

      // When
      TimeRecorder.INSTANCE.removeIncrementAtIndex(1);

      // Then
      BusinessDayVO bussinessDayVO = TimeRecorder.INSTANCE.getBussinessDayVO();
      assertThat(bussinessDayVO.getBusinessDayIncrements().size(), is(1));
   }

   @Test
   void testRemoveBusinessIncrementAtIndex() {
      // Given
      new TestCaseBuilder(TimeRecorder.INSTANCE)
            .withBusinessDayIncrement("SYRIUS-45645", "Test3", 113, 3600 * 1000)
            .build();

      // When
      BusinessDayVO bussinessDayVO = TimeRecorder.INSTANCE.getBussinessDayVO();
      assertThat(bussinessDayVO.getBusinessDayIncrements().size(), is(1));
      TimeRecorder.INSTANCE.removeIncrementAtIndex(0);

      // Then
      bussinessDayVO = TimeRecorder.INSTANCE.getBussinessDayVO();
      assertThat(bussinessDayVO.getBusinessDayIncrements().isEmpty(), is(true));
   }

   @Test
   void testClearAllBusinessIncrements() {
      // Given
      new TestCaseBuilder(TimeRecorder.INSTANCE)
            .withBusinessDayIncrement("SYRIUS-42353", "Test88", 113, 3600)
            .build();

      // When
      BusinessDayVO bussinessDayVO = TimeRecorder.INSTANCE.getBussinessDayVO();
      assertThat(bussinessDayVO.getBusinessDayIncrements().size(), is(1));
      TimeRecorder.INSTANCE.clear();

      // Then
      bussinessDayVO = TimeRecorder.INSTANCE.getBussinessDayVO();
      assertThat(bussinessDayVO.getBusinessDayIncrements().isEmpty(), is(true));
   }

   @Test
   void testStartWithElementsFromPrecedentDay_DoNotStart() {
      // Given, add an existing increment from the 01.02.2020
      TestUiCallbackHandler testUiCallbackHandler = spy(new TestUiCallbackHandler());
      new TestCaseBuilder(TimeRecorder.INSTANCE)
            .withBusinessDayIncrement("SYRIUS-534534", "Test", 113, 600 * 1000)
            .setCallbackHandler(testUiCallbackHandler)
            .build();

      // When
      TimeRecorder.INSTANCE.handleUserInteraction();

      // Then
      verify(testUiCallbackHandler, never()).onStart();
      assertThat(testUiCallbackHandler.receivedMessageType, is(MessageType.ERROR));
   }

   @Test
   void testStartAndStopNewBusinessIncrement() throws InterruptedException {
      // Given
      UiCallbackHandler testUiCallbackHandler = spy(new TestUiCallbackHandler());
      TimeRecorder.INSTANCE.setCallbackHandler(testUiCallbackHandler);

      // When
      TimeRecorder.INSTANCE.handleUserInteraction();
      Thread.sleep(500);
      TimeRecorder.INSTANCE.handleUserInteraction();

      // Then
      BusinessDayVO bussinessDayVO = TimeRecorder.INSTANCE.getBussinessDayVO();
      boolean actualHasContent = TimeRecorder.INSTANCE.hasContent();
      assertThat(bussinessDayVO.getBusinessDayIncrements().isEmpty(), is(true));
      assertThat(actualHasContent, is(false));
      BusinessDayIncrementVO currentBussinessDayIncrement = TimeRecorder.INSTANCE.getCurrentBussinessDayIncrement();
      assertThat(currentBussinessDayIncrement.getCurrentTimeSnippet().getBeginTimeStamp(), is(notNullValue()));
      assertThat(currentBussinessDayIncrement.getCurrentTimeSnippet().getEndTimeStamp(), is(notNullValue()));
      verify(testUiCallbackHandler).onStart();
      verify(testUiCallbackHandler).onStop();
   }

   @Test
   void testStartNewBusinessIncrement() {
      // Given
      UiCallbackHandler testUiCallbackHandler = spy(new TestUiCallbackHandler());
      TimeRecorder.INSTANCE.setCallbackHandler(testUiCallbackHandler);

      // When
      TimeRecorder.INSTANCE.handleUserInteraction();
      boolean isRecordingAfterFirstHandle = TimeRecorder.INSTANCE.isRecordindg();
      boolean actualIsBooking = TimeRecorder.INSTANCE.isBooking();

      // Then
      BusinessDayIncrementVO currentBussinessDayIncrement = TimeRecorder.INSTANCE.getCurrentBussinessDayIncrement();
      assertThat(currentBussinessDayIncrement.getCurrentTimeSnippet().getBeginTimeStamp(), is(notNullValue()));
      assertThat(currentBussinessDayIncrement.getCurrentTimeSnippet().getEndTimeStamp(), is(nullValue()));
      assertThat(isRecordingAfterFirstHandle, is(true));
      assertThat(actualIsBooking, is(false));
      verify(testUiCallbackHandler).onStart();
      verify(testUiCallbackHandler, never()).onStop();
   }

   @Test
   void testStartStopAndResumeNewBusinessIncrement() throws InterruptedException {
      // Given
      UiCallbackHandler testUiCallbackHandler = spy(new TestUiCallbackHandler());
      TimeRecorder.INSTANCE.setCallbackHandler(testUiCallbackHandler);

      // When
      TimeRecorder.INSTANCE.handleUserInteraction();
      boolean isRecordingAfterFirstHandle = TimeRecorder.INSTANCE.isRecordindg();
      Thread.sleep(500);

      TimeRecorder.INSTANCE.handleUserInteraction();
      boolean isRecordingAfterSecondHandle = TimeRecorder.INSTANCE.isRecordindg();
      BusinessDayIncrementVO currentBussinessDayIncrement = TimeRecorder.INSTANCE.getCurrentBussinessDayIncrement();
      assertThat(currentBussinessDayIncrement.getCurrentTimeSnippet().getBeginTimeStamp(), is(notNullValue()));
      assertThat(currentBussinessDayIncrement.getCurrentTimeSnippet().getEndTimeStamp(), is(notNullValue()));

      TimeRecorder.INSTANCE.resume();
      currentBussinessDayIncrement = TimeRecorder.INSTANCE.getCurrentBussinessDayIncrement();
      assertThat(currentBussinessDayIncrement.getCurrentTimeSnippet().getBeginTimeStamp(), is(notNullValue()));
      assertThat(currentBussinessDayIncrement.getCurrentTimeSnippet().getEndTimeStamp(), is(nullValue()));

      // Then
      BusinessDayVO bussinessDayVO = TimeRecorder.INSTANCE.getBussinessDayVO();
      assertThat(bussinessDayVO.getBusinessDayIncrements().isEmpty(), is(true));
      assertThat(isRecordingAfterFirstHandle, is(true));
      assertThat(isRecordingAfterSecondHandle, is(false));
      verify(testUiCallbackHandler).onStart();
      verify(testUiCallbackHandler).onStop();
      verify(testUiCallbackHandler).onResume();
   }

   @Test
   void testBook_StartDuringBooking() throws InterruptedException {

      // Given
      BookerAdapter bookAdapter = mockBookAdapter(true, BookResultType.FAILURE, 500);
      TimeRecorder timeRecorder = new TimeRecorder(bookAdapter);
      TestUiCallbackHandler uiCallbackHandler = new TestUiCallbackHandler();
      timeRecorder.setCallbackHandler(uiCallbackHandler);
      new TestCaseBuilder(timeRecorder)
            .withBusinessDayIncrement("SYRIUS-65468", "fest", 113, 3600 * 1000)
            .build();

      // When
      Thread startBookThread = new Thread(() -> timeRecorder.book());
      startBookThread.start();
      Thread.sleep(50);
      boolean actualIsBooking = timeRecorder.isBooking();
      boolean actualHandle = timeRecorder.handleUserInteraction();

      // Then
      verify(bookAdapter).book(any());
      assertThat(actualIsBooking, is(true));
      assertThat(actualHandle, is(false));
   }

   @Test
   void testChangeBusinessDay() {

      // Given
      int firstTimeBetweenStartAndStop = 3600 * 1000;
      String ticketNr = "SYRIUS-11111";
      String description = "Test";
      String newDescription = "Test2";
      int kindOfService = 113;
      new TestCaseBuilder(TimeRecorder.INSTANCE)
            .withBusinessDayIncrement(ticketNr, description, kindOfService, firstTimeBetweenStartAndStop)
            .build();

      ChangedValue changeValue = ChangedValue.of(0, newDescription, ValueTypes.DESCRIPTION);

      // When
      TimeRecorder.INSTANCE.changeBusinesDayIncrement(changeValue);

      // Then
      BusinessDayVO bussinessDayVO = TimeRecorder.INSTANCE.getBussinessDayVO();
      BusinessDayIncrementVO businessDayIncrement = TimeRecorder.INSTANCE.getBussinessDayVO().getBusinessDayIncrements().get(0);
      assertThat(businessDayIncrement.getDescription(), is(newDescription));
      assertThat(bussinessDayVO.hasIncrementWithDescription(), is(true));
   }

   @Test
   public void testChangeDBIncChargeType() {

      // Given
      int kindOfService = 113;
      int expectedNewChargeType = 111;
      new TestCaseBuilder(TimeRecorder.INSTANCE)
            .withBusinessDayIncrement("SYRIUS-11111", "Test", kindOfService, 3600 * 1000)
            .build();

      ChangedValue changeValue = ChangedValue.of(0, "111 - Meeting", ValueTypes.CHARGE_TYPE);

      // When
      TimeRecorder.INSTANCE.changeBusinesDayIncrement(changeValue);

      // Then
      BusinessDayIncrementVO businessDayIncrement = TimeRecorder.INSTANCE.getBussinessDayVO().getBusinessDayIncrements().get(0);
      assertThat(businessDayIncrement.getChargeType(), is(expectedNewChargeType));
   }

   @Test
   public void testChangeDBIncChargeType_Invalid() {

      // Given
      // Given
      int currentServiceCode = 113;
      new TestCaseBuilder(TimeRecorder.INSTANCE)
            .withBusinessDayIncrement("SYRIUS-11111", "Test", currentServiceCode, 3600 * 1000)
            .build();

      ChangedValue changeValue = ChangedValue.of(0, "Schubedibuuu", ValueTypes.CHARGE_TYPE);

      // When
      TimeRecorder.INSTANCE.changeBusinesDayIncrement(changeValue);

      // Then
      BusinessDayIncrementVO businessDayIncrement = TimeRecorder.INSTANCE.getBussinessDayVO().getBusinessDayIncrements().get(0);
      assertThat(businessDayIncrement.getChargeType(), is(currentServiceCode));
   }

   @Test
   void testHasContent() {

      // Given
      new TestCaseBuilder(TimeRecorder.INSTANCE)
            .withBusinessDayIncrement("SYRIUS-48642", "Test", 113, 3600 * 1000)
            .build();

      // When
      boolean actualHasContent = TimeRecorder.INSTANCE.hasContent();
      BusinessDayVO bussinessDayVO = TimeRecorder.INSTANCE.getBussinessDayVO();

      // Then
      assertThat(actualHasContent, is(true));
      assertThat(bussinessDayVO.hasNotChargedElements(), is(true));
   }

   @Test
   void testHasDescription() {

      // Given
      new TestCaseBuilder(TimeRecorder.INSTANCE)
            .withBusinessDayIncrement("SYRIUS-1234", "Test", 113, 3600 * 1000)
            .build();

      // When
      boolean actualHasBusinessDayDescription = TimeRecorder.INSTANCE.hasBusinessDayDescription();

      // Then
      assertThat(actualHasBusinessDayDescription, is(true));
   }

   @Test
   void testGetInfoStringForStateNotWorking() {

      // Given
      String expectedInfoStatePrefix = "Adcubum Zeiterfassung: Zeiterfassung inaktiv seit: ";
      new TestCaseBuilder(TimeRecorder.INSTANCE)
            .withBusinessDayIncrement("SYRIUS-1234", "Test", 113, 3600 * 1000)
            .build();

      // When
      String infoStringForStateNotWorking = TimeRecorder.INSTANCE.getInfoStringForState();

      // Then
      assertThat(infoStringForStateNotWorking.startsWith(expectedInfoStatePrefix), is(true));
   }

   @Test
   void testGetInfoStringForStateNotWorking_WithoutBusinessDayIncrement() {

      // Given
      String expectedInfoStatePrefix = TextLabel.APPLICATION_TITLE + ": " + TextLabel.CAPTURING_INACTIVE;
      // When
      String infoStringForStateNotWorking = TimeRecorder.INSTANCE.getInfoStringForState();

      // Then
      assertThat(infoStringForStateNotWorking.equals(expectedInfoStatePrefix), is(true));
   }

   @Test
   void testGetInfoStringForStateWorking() {

      // Given
      TimeRecorder.INSTANCE.setCallbackHandler(mock(TestUiCallbackHandler.class));
      String expectedInfoStatePrefix = "Zeiterfassung aktiv seit: ";

      // When
      TimeRecorder.INSTANCE.handleUserInteraction();
      String infoStringForStateNotWorking = TimeRecorder.INSTANCE.getInfoStringForState();

      // Then
      assertThat(infoStringForStateNotWorking.startsWith(expectedInfoStatePrefix), is(true));
   }

   @Test
   void testGetInfoStringForStateBooking() throws InterruptedException {

      // Given
      BookerAdapter bookAdapter = mockBookAdapter(true, BookResultType.FAILURE, 500);
      TimeRecorder timeRecorder = new TimeRecorder(bookAdapter);
      timeRecorder.setCallbackHandler(mock(UiCallbackHandler.class));
      new TestCaseBuilder(timeRecorder)
            .withBusinessDayIncrement("SYRIUS-99999", "fest", 113, 3600 * 1000)
            .build();

      // When
      Thread startBookThread = new Thread(() -> timeRecorder.book());
      startBookThread.start();
      Thread.sleep(50);
      String infoStringForStateBooking = timeRecorder.getInfoStringForState();

      // Then
      assertThat(infoStringForStateBooking.equals(TextLabel.BOOKING_RUNNING), is(true));
   }

   @Test
   void testBook_NothingToBook() {

      // Given
      BookerAdapter bookAdapter = mock(BookerAdapter.class);
      TimeRecorder timeRecorder = new TimeRecorder(bookAdapter);
      timeRecorder.setCallbackHandler(mock(TestUiCallbackHandler.class));

      // When
      timeRecorder.book();
      boolean actualHasNotChargedElements = timeRecorder.hasNotChargedElements();

      // Then
      verify(bookAdapter, never()).book(any());
      assertThat(actualHasNotChargedElements, is(false));
   }

   @Test
   void testBook_HasSomethingToBookButNotBookingWasNotSuccessfull() {

      // Given
      BookerAdapter bookAdapter = mockBookAdapter(false, BookResultType.FAILURE);
      TimeRecorder timeRecorder = new TimeRecorder(bookAdapter);
      TestUiCallbackHandler uiCallbackHandler = mock(TestUiCallbackHandler.class);
      timeRecorder.setCallbackHandler(uiCallbackHandler);
      new TestCaseBuilder(timeRecorder)
            .withBusinessDayIncrement("SYRIUS-34345", "fest", 113, 3600 * 1000)
            .build();

      // When
      boolean actualHasNotChargedElements = timeRecorder.hasNotChargedElements();
      timeRecorder.book();

      // Then
      verify(bookAdapter).book(any());
      verify(uiCallbackHandler, never()).displayMessage(any());
      assertThat(actualHasNotChargedElements, is(true));
   }

   @Test
   void testBook_BookSuccess() {

      // Given
      BookerAdapter bookAdapter = mockBookAdapter(true, BookResultType.SUCCESS);
      TimeRecorder timeRecorder = new TimeRecorder(bookAdapter);
      TestUiCallbackHandler uiCallbackHandler = new TestUiCallbackHandler();
      timeRecorder.setCallbackHandler(uiCallbackHandler);
      new TestCaseBuilder(timeRecorder)
            .withBusinessDayIncrement("SYRIUS-456456", "fest", 113, 3600 * 1000)
            .build();

      // When
      timeRecorder.book();

      // Then
      verify(bookAdapter).book(any());
      assertThat(uiCallbackHandler.receivedMessageType, is(MessageType.INFORMATION));
   }

   @Test
   void testBook_BookPartialSuccess() {

      // Given
      BookerAdapter bookAdapter = mockBookAdapter(true, BookResultType.PARTIAL_SUCCESS_WITH_ERROR);
      TimeRecorder timeRecorder = new TimeRecorder(bookAdapter);
      TestUiCallbackHandler uiCallbackHandler = new TestUiCallbackHandler();
      timeRecorder.setCallbackHandler(uiCallbackHandler);
      new TestCaseBuilder(timeRecorder)
            .withBusinessDayIncrement("SYRIUS-25345", "fest", 113, 3600 * 1000)
            .build();

      // When
      timeRecorder.book();

      // Then
      verify(bookAdapter).book(any());
      assertThat(uiCallbackHandler.receivedMessageType, is(MessageType.WARNING));
   }

   @Test
   void testBook_BookPartialSuccess2() {

      // Given
      BookerAdapter bookAdapter = mockBookAdapter(true, BookResultType.PARTIAL_SUCCESS_WITH_NON_BOOKABLE);
      TimeRecorder timeRecorder = new TimeRecorder(bookAdapter);
      TestUiCallbackHandler uiCallbackHandler = new TestUiCallbackHandler();
      timeRecorder.setCallbackHandler(uiCallbackHandler);
      new TestCaseBuilder(timeRecorder)
            .withBusinessDayIncrement("SYRIUS-5656", "fest", 113, 3600 * 1000)
            .build();

      // When
      timeRecorder.book();

      // Then
      verify(bookAdapter).book(any());
      assertThat(uiCallbackHandler.receivedMessageType, is(MessageType.WARNING));
   }

   @Test
   void testBook_BookPartialFailure() {

      // Given
      BookerAdapter bookAdapter = mockBookAdapter(true, BookResultType.FAILURE);
      TimeRecorder timeRecorder = new TimeRecorder(bookAdapter);
      TestUiCallbackHandler uiCallbackHandler = new TestUiCallbackHandler();
      timeRecorder.setCallbackHandler(uiCallbackHandler);
      new TestCaseBuilder(timeRecorder)
            .withBusinessDayIncrement("SYRIUS-65468", "fest", 113, 3600 * 1000)
            .build();

      // When
      timeRecorder.book();

      // Then
      verify(bookAdapter).book(any());
      assertThat(uiCallbackHandler.receivedMessageType, is(MessageType.ERROR));
   }

   private BookerAdapter mockBookAdapter(boolean hasBooked, BookResultType bookResultType, int delayDuringBooking) {
      return spy(new TestBookAdapter(hasBooked, bookResultType, delayDuringBooking));
   }

   private BookerAdapter mockBookAdapter(boolean hasBooked, BookResultType bookResultType) {
      return mockBookAdapter(hasBooked, bookResultType, 0);
   }

   private static class TestCaseBuilder {

      private List<BusinessDayIncrementAdd> businessDayIncrementAdds;
      private TimeRecorder timeRecorder;

      private TestCaseBuilder(TimeRecorder timeRecorder) {
         this.businessDayIncrementAdds = new ArrayList<>();
         this.timeRecorder = timeRecorder;
      }

      public TestCaseBuilder setCallbackHandler(UiCallbackHandler testUiCallbackHandler) {
         timeRecorder.setCallbackHandler(testUiCallbackHandler);
         return this;
      }

      private TestCaseBuilder withBusinessDayIncrement(String ticketNr, String description, int kindOfService, int timeSnippedDuration) {
         Ticket ticket = mockTicket(ticketNr);
         businessDayIncrementAdds.add(new BusinessDayIncrementAddBuilder()
               .withTimeSnippet(createTimeSnippet(timeSnippedDuration))
               .withDescription(description)
               .withTicket(ticket)
               .withKindOfService(kindOfService)
               .build());
         return this;
      }

      private Ticket mockTicket(String ticketNr) {
         Ticket ticket = mock(Ticket.class);
         when(ticket.getNr()).thenReturn(ticketNr);
         return ticket;
      }

      private TestCaseBuilder build() {
         addBusinessIncrements();
         return this;
      }

      private void addBusinessIncrements() {
         for (BusinessDayIncrementAdd businessDayIncrementAdd : businessDayIncrementAdds) {
            timeRecorder.addBusinessIncrement(businessDayIncrementAdd);
         }
      }

      private TimeSnippet createTimeSnippet(int timeBetweenBeginAndEnd) {
         GregorianCalendar startDate = new GregorianCalendar(2020, 1, 1);// year, month (starts at zero!), day, hours, min, second
         Time beginTimeStamp = new Time(startDate.getTimeInMillis());
         TimeSnippet timeSnippet = new TimeSnippet(new Date(beginTimeStamp.getTime()));
         timeSnippet.setBeginTimeStamp(beginTimeStamp);
         timeSnippet.setEndTimeStamp(new Time(startDate.getTimeInMillis() + timeBetweenBeginAndEnd));
         return timeSnippet;
      }
   }

   private static class TestBookAdapter implements BookerAdapter {

      private BookResultType bookResultType;
      private boolean hasBooked;
      private int delayDuringBooking;

      private TestBookAdapter(boolean hasBooked, BookResultType bookResultType, int delayDuringBooking) {
         this.hasBooked = hasBooked;
         this.bookResultType = bookResultType;
         this.delayDuringBooking = delayDuringBooking;
      }

      @Override
      public ServiceCodeAdapter getServiceCodeAdapter() {
         return mock(ServiceCodeAdapter.class);
      }

      @Override
      public BookerResult book(BusinessDay businessDay) {
         try {
            Thread.sleep(delayDuringBooking);
         } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException(e);
         }
         return new BookerResult() {

            @Override
            public boolean hasBooked() {
               return hasBooked;
            }

            @Override
            public String getMessage() {
               return "";
            }

            @Override
            public BookResultType getBookResultType() {
               return bookResultType;
            }
         };
      }
   }

   private static class TestUiCallbackHandler implements UiCallbackHandler {

      private MessageType receivedMessageType;

      @Override
      public void onStop() {
         // empty
      }

      @Override
      public void onStart() {
         // empty
      }

      @Override
      public void onResume() {
         // empty
      }

      @Override
      public void onException(Throwable throwable, Thread t) {
         // empty
      }

      @Override
      public void displayMessage(Message message) {
         this.receivedMessageType = message.getMessageType();
      }
   }
}
