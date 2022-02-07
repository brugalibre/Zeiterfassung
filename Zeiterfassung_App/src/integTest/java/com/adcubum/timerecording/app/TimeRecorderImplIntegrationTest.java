package com.adcubum.timerecording.app;

import com.adcubum.librarys.text.res.TextLabel;
import com.adcubum.timerecording.app.startstopresult.StartNotPossibleInfo;
import com.adcubum.timerecording.app.startstopresult.UserInteractionResult;
import com.adcubum.timerecording.core.book.adapter.BookerAdapter;
import com.adcubum.timerecording.core.book.adapter.ServiceCodeAdapter;
import com.adcubum.timerecording.core.book.result.BookResultType;
import com.adcubum.timerecording.core.book.result.BookerResult;
import com.adcubum.timerecording.core.callbackhandler.UiCallbackHandler;
import com.adcubum.timerecording.core.work.businessday.*;
import com.adcubum.timerecording.core.work.businessday.TimeSnippetImpl.TimeSnippetBuilder;
import com.adcubum.timerecording.core.work.businessday.comeandgo.ComeAndGo;
import com.adcubum.timerecording.core.work.businessday.comeandgo.ComeAndGoes;
import com.adcubum.timerecording.core.work.businessday.comeandgo.impl.ComeAndGoesImpl;
import com.adcubum.timerecording.core.work.businessday.repository.BusinessDayRepository;
import com.adcubum.timerecording.core.work.businessday.update.callback.impl.BusinessDayIncrementAdd;
import com.adcubum.timerecording.core.work.businessday.update.callback.impl.BusinessDayIncrementAdd.BusinessDayIncrementAddBuilder;
import com.adcubum.timerecording.core.work.businessday.update.callback.impl.ChangedValue;
import com.adcubum.timerecording.data.ticket.ticketactivity.factor.TicketActivityFactory;
import com.adcubum.timerecording.integtest.BaseTestWithSettings;
import com.adcubum.timerecording.jira.data.ticket.Ticket;
import com.adcubum.timerecording.jira.data.ticket.TicketActivity;
import com.adcubum.timerecording.message.Message;
import com.adcubum.timerecording.message.MessageType;
import com.adcubum.timerecording.messaging.send.BookBusinessDayMessageSender;
import com.adcubum.timerecording.settings.Settings;
import com.adcubum.timerecording.work.date.DateTime;
import com.adcubum.timerecording.work.date.DateTimeFactory;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.concurrent.TimeUnit;

import static com.adcubum.timerecording.core.work.businessday.repository.BusinessDayRepositoryIntegMockUtil.mockBusinessDayRepository;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class TimeRecorderImplIntegrationTest extends BaseTestWithSettings {

   @Test
   void testGetSettingsValue() {
      // Given
      String key = "key";
      Settings settings = mock(Settings.class);
      TimeRecorder timeRecorderImpl = TestCaseBuilder.mockTimeRecorderImpl(settings);

      // When
      timeRecorderImpl.getSettingsValue(key);

      // Then
      verify(settings).getSettingsValue(any());
   }

   @Test
   void testSaveSettingValue() {
      // Given
      String key = "key";
      Settings settings = mock(Settings.class);
      TimeRecorder timeRecorderImpl = TestCaseBuilder.mockTimeRecorderImpl(settings);
      String value = "value";

      // When
      timeRecorderImpl.saveSettingValue(value, key);

      // Then
      verify(settings).saveValueToProperties(any(), eq(value));
   }

   @Test
   void testCreateAndDeleteComeAndGo() {

      // Given
      TestUiCallbackHandler testUiCallbackHandler = spy(new TestUiCallbackHandler());
      TestCaseBuilder tcb = new TestCaseBuilder()
              .withUiCallbackHandler(testUiCallbackHandler)
              .build();

      // When
      tcb.timeRecorder.handleUserInteraction(true);// come
      tcb.timeRecorder.handleUserInteraction(true);// go
      BusinessDay businessDay = tcb.timeRecorder.getBusinessDay();
      int amountOfComeAndGoesBeforeDeletion = businessDay.getComeAndGoes().getComeAndGoEntries().size();
      tcb.timeRecorder.clearComeAndGoes();
      businessDay = tcb.timeRecorder.getBusinessDay();
      int amountOfComeAndGoesAfterDeletion = businessDay.getComeAndGoes().getComeAndGoEntries().size();

      // Then
      assertThat(amountOfComeAndGoesBeforeDeletion, is(1));
      assertThat(amountOfComeAndGoesAfterDeletion, is(0));
   }

   @Test
   void testCreateAndDeleteComeAndGoAndFlagAsRecorded() {

      // Given
      TestUiCallbackHandler testUiCallbackHandler = spy(new TestUiCallbackHandler());
      TestCaseBuilder tcb = new TestCaseBuilder()
              .withUiCallbackHandler(testUiCallbackHandler)
              .build();

      // When
      tcb.timeRecorder.handleUserInteraction(true);// come
      tcb.timeRecorder.handleUserInteraction(true);// go
      tcb.timeRecorder.flagBusinessDayComeAndGoesAsRecorded();

      BusinessDay businessDay = tcb.timeRecorder.getBusinessDay();

      // Then
      assertThat(businessDay.getComeAndGoes().getComeAndGoEntries().size(), is(1));
      ComeAndGo comeAndGo = businessDay.getComeAndGoes().getComeAndGoEntries().get(0);
      assertThat(comeAndGo.isNotRecorded(), is(false));
   }

   @Test
   void testRefreshDummyTickets() {

      // Given
      BusinessDayImpl businessDay = mockBusinessDayImpl();
      TimeRecorder timeRecorder = TestCaseBuilder.mockTimeRecorderImpl(businessDay);

      // When
      timeRecorder.onTicketBacklogInitialized();

      // Then
      verify(businessDay).refreshDummyTickets();
   }

   @Test
   void testRemoveBusinessIncrementWithUnknownId() {
      // Given
      String ticketNr = "SYRIUS-4321";
      String description = "Test";
      int serviceCode = 113;
      int timeSnippedDuration = 3600 * 1000;
      TestCaseBuilder tcb = new TestCaseBuilder()
              .withBusinessDayIncrement(ticketNr, description, serviceCode, timeSnippedDuration)
              .build();

      // When
      tcb.timeRecorder.removeIncrement4Id(UUID.randomUUID());

      // Then
      BusinessDay businessDay = tcb.timeRecorder.getBusinessDay();
      assertThat(businessDay.getIncrements().size(), is(1));
   }

   @Test
   void testRemoveBusinessIncrementAtIndex() {
      // Given
      TestCaseBuilder tcb = new TestCaseBuilder()
              .withBusinessDayIncrement("SYRIUS-45645", "Test3", 113, 3600 * 1000)
              .build();

      // When
      BusinessDay businessDay = tcb.timeRecorder.getBusinessDay();
      BusinessDayIncrement BusinessDayIncrement = tcb.timeRecorder.getBusinessDay().getIncrements().get(0);
      assertThat(businessDay.getIncrements().size(), is(1));
      tcb.timeRecorder.removeIncrement4Id(BusinessDayIncrement.getId());

      // Then
      businessDay = tcb.timeRecorder.getBusinessDay();
      assertThat(businessDay.getIncrements().isEmpty(), is(true));
   }

   @Test
   void testClearAllBusinessIncrements() {
      // Given
      TestCaseBuilder tcb = new TestCaseBuilder()
              .withBusinessDayIncrement("SYRIUS-42353", "Test88", 113, 3600)
              .build();

      // When
      BusinessDay businessDay = tcb.timeRecorder.getBusinessDay();
      assertThat(businessDay.getIncrements().size(), is(1));
      tcb.timeRecorder.clear();

      // Then
      businessDay = tcb.timeRecorder.getBusinessDay();
      assertThat(businessDay.getIncrements().isEmpty(), is(true));
   }

   @Test
   void testStartWithElementsFromPrecedentDay_DoNotStart() {
      // Given, add an existing increment from the 01.02.2020
      TestUiCallbackHandler testUiCallbackHandler = spy(new TestUiCallbackHandler());
      TestCaseBuilder tcb = new TestCaseBuilder()
              .withBusinessDayIncrement("SYRIUS-534534", "Test", 113, 600 * 1000)
              .withUiCallbackHandler(testUiCallbackHandler)
              .build();
      // When
      UserInteractionResult userInteractionResult = tcb.timeRecorder.handleUserInteraction(false);

      // Then
      verify(testUiCallbackHandler, never()).onStart();
      assertThat(userInteractionResult.getOptionalStartNotPossibleInfo().isPresent(), is(true));
      StartNotPossibleInfo startNotPossibleInfo = userInteractionResult.getOptionalStartNotPossibleInfo().get();
      Message message = startNotPossibleInfo.getMessage();
      assertThat(message.getMessageTitle(), is(TextLabel.START_NOT_POSSIBLE_PRECEDENT_ELEMENTS_TITLE));
      assertThat(message.getMessage(), is(TextLabel.START_NOT_POSSIBLE_PRECEDENT_ELEMENTS));
      assertThat(testUiCallbackHandler.receivedMessageType, is(MessageType.ERROR));
   }

   @Test
   void testComeWithComeAndGoesFromPrecedentDay_DoNotStart() {
      // Given, add an existing increment from the 01.02.2020
      TestUiCallbackHandler testUiCallbackHandler = spy(new TestUiCallbackHandler());
      BusinessDayImpl businessDay = mockBusinessDayImpl();
      when(businessDay.hasComeAndGoesFromPrecedentDays()).thenReturn(true);
      TimeRecorder timeRecorder = TestCaseBuilder.mockTimeRecorderImpl(businessDay);
      timeRecorder.setCallbackHandler(testUiCallbackHandler);

      // When
      timeRecorder.handleUserInteraction(true);

      // Then
      verify(testUiCallbackHandler, never()).onCome();
      assertThat(testUiCallbackHandler.receivedMessageType, is(MessageType.ERROR));
   }

   @Test
   void testComeWithBDIncrementsFromPrecedentDay_DoNotStart() {
      // Given, add an existing increment from the 01.02.2020
      TestUiCallbackHandler testUiCallbackHandler = spy(new TestUiCallbackHandler());
      TestCaseBuilder tcb = new TestCaseBuilder()
              .withBusinessDayIncrement("SYRIUS-534534", "Test", 113, 600 * 1000)
              .withUiCallbackHandler(testUiCallbackHandler)
              .build();

      // When
      tcb.timeRecorder.handleUserInteraction(true);

      // Then
      verify(testUiCallbackHandler, never()).onCome();
      assertThat(testUiCallbackHandler.receivedMessageType, is(MessageType.ERROR));
   }

   @Test
   void testStartAndStopNewBusinessIncrement() throws InterruptedException {
      // Given
      TimeRecorderImpl timeRecorderImpl = TestCaseBuilder.mockTimeRecorderImpl();
      UiCallbackHandler testUiCallbackHandler = spy(new TestUiCallbackHandler());
      timeRecorderImpl.setCallbackHandler(testUiCallbackHandler);

      // When
      timeRecorderImpl.handleUserInteraction(false);
      TimeUnit.MILLISECONDS.sleep(500);
      timeRecorderImpl.handleUserInteraction(false);

      // Then
      BusinessDay businessDay = timeRecorderImpl.getBusinessDay();
      boolean actualHasContent = timeRecorderImpl.hasContent();
      assertThat(businessDay.getIncrements().isEmpty(), is(true));
      assertThat(actualHasContent, is(false));
      TimeSnippet currentTimeSnippet = timeRecorderImpl.getBusinessDay().getCurrentTimeSnippet();
      assertThat(currentTimeSnippet.getBeginTimeStamp(), is(notNullValue()));
      assertThat(currentTimeSnippet.getEndTimeStamp(), is(notNullValue()));
      verify(testUiCallbackHandler).onStart();
      verify(testUiCallbackHandler).onStop();
   }

   @Test
   void testStartNewBusinessIncrement() {
      // Given
      TimeRecorderImpl timeRecorderImpl = TestCaseBuilder.mockTimeRecorderImpl();
      UiCallbackHandler testUiCallbackHandler = spy(new TestUiCallbackHandler());
      timeRecorderImpl.setCallbackHandler(testUiCallbackHandler);

      // When
      timeRecorderImpl.handleUserInteraction(false);
      boolean isRecordingAfterFirstHandle = timeRecorderImpl.isRecording();
      boolean actualIsBooking = timeRecorderImpl.isBooking();

      // Then
      TimeSnippet currentTimeSnippet = timeRecorderImpl.getBusinessDay().getCurrentTimeSnippet();
      assertThat(currentTimeSnippet.getBeginTimeStamp(), is(notNullValue()));
      assertThat(currentTimeSnippet.getEndTimeStamp(), is(nullValue()));
      assertThat(isRecordingAfterFirstHandle, is(true));
      assertThat(actualIsBooking, is(false));
      verify(testUiCallbackHandler).onStart();
      verify(testUiCallbackHandler, never()).onStop();
   }

   @Test
   void testStartStopAndResumeNewBusinessIncrement() throws InterruptedException {
      // Given
      UiCallbackHandler testUiCallbackHandler = spy(new TestUiCallbackHandler());
      TimeRecorderImpl timeRecorderImpl = TestCaseBuilder.mockTimeRecorderImpl();
      timeRecorderImpl.setCallbackHandler(testUiCallbackHandler);

      // When
      timeRecorderImpl.handleUserInteraction(false);
      boolean isRecordingAfterFirstHandle = timeRecorderImpl.isRecording();
      TimeUnit.MILLISECONDS.sleep(500);

      timeRecorderImpl.handleUserInteraction(false);
      boolean isRecordingAfterSecondHandle = timeRecorderImpl.isRecording();
      TimeSnippet currentTimeSnippet = timeRecorderImpl.getBusinessDay().getCurrentTimeSnippet();
      assertThat(currentTimeSnippet.getBeginTimeStamp(), is(notNullValue()));
      assertThat(currentTimeSnippet.getEndTimeStamp(), is(notNullValue()));

      timeRecorderImpl.resume();
      currentTimeSnippet = timeRecorderImpl.getBusinessDay().getCurrentTimeSnippet();
      assertThat(currentTimeSnippet.getBeginTimeStamp(), is(notNullValue()));
      assertThat(currentTimeSnippet.getEndTimeStamp(), is(nullValue()));

      // Then
      BusinessDay businessDay = timeRecorderImpl.getBusinessDay();
      assertThat(businessDay.getIncrements().isEmpty(), is(true));
      assertThat(isRecordingAfterFirstHandle, is(true));
      assertThat(isRecordingAfterSecondHandle, is(false));
      verify(testUiCallbackHandler).onStart();
      verify(testUiCallbackHandler).onStop();
      verify(testUiCallbackHandler).onResume();
   }

   @Test
   void testBook_StartDuringBooking() throws InterruptedException {

      // Given
      TestCaseBuilder tcb = new TestCaseBuilder()
              .withBookerAdapter(mockBookAdapter(true, BookResultType.FAILURE, 500))
              .withBusinessDayRepository(mockBusinessDayRepository(new BusinessDayImpl()))
              .withBookBusinessDayMessageSender(mock(BookBusinessDayMessageSender.class))
              .withUiCallbackHandler(new TestUiCallbackHandler())
              .withBusinessDayIncrement("SYRIUS-65468", "fest", 113, 3600 * 1000)
              .build();

      // When
      Thread startBookThread = new Thread(() -> tcb.timeRecorder.book());
      startBookThread.start();
      TimeUnit.MILLISECONDS.sleep(50);
      boolean actualIsBooking = tcb.timeRecorder.isBooking();
      UserInteractionResult actualHandleResult = tcb.timeRecorder.handleUserInteraction(false);

      // Then
      verify(tcb.bookerAdapter).book(any());
      verify(tcb.bookBusinessDayMessageSender, never()).sendBookedIncrements(any());
      assertThat(actualIsBooking, is(true));
      assertThat(actualHandleResult.isUserInteractionRequired(), is(false));
   }

   @Test
   void testBook_StartWhileComeAndGoIsEnabled() {

      // Given

      TestCaseBuilder tcb = new TestCaseBuilder()
              .withUiCallbackHandler(new TestUiCallbackHandler())
              .build();

      // When
      // start come and go
      tcb.timeRecorder.handleUserInteraction(true);
      UserInteractionResult actualUserInteractionResult = tcb.timeRecorder.handleUserInteraction(false);

      // Then
      ComeAndGoes comeAndGoes = tcb.timeRecorder.getBusinessDay().getComeAndGoes();
      assertThat(actualUserInteractionResult.isUserInteractionRequired(), is(false));
      assertThat(comeAndGoes.getComeAndGoEntries().size(), is(1));
   }

   @Test
   void testChangeBusinessDay() {

      // Given
      int firstTimeBetweenStartAndStop = 3600 * 1000;
      String ticketNr = "SYRIUS-11111";
      String description = "Test";
      String newDescription = "Test2";
      int serviceCode = 113;
      TestCaseBuilder tcb = new TestCaseBuilder()
              .withBusinessDayIncrement(ticketNr, description, serviceCode, firstTimeBetweenStartAndStop)
              .build();
      BusinessDayIncrement BusinessDayIncrement = tcb.timeRecorder.getBusinessDay().getIncrements().get(0);
      ChangedValue changeValue = ChangedValue.of(BusinessDayIncrement.getId(), newDescription, ValueTypes.DESCRIPTION);

      // When
      tcb.timeRecorder.changeBusinesDayIncrement(changeValue);

      // Then
      BusinessDay businessDay = tcb.timeRecorder.getBusinessDay();
      BusinessDayIncrement businessDayIncrement = tcb.timeRecorder.getBusinessDay().getIncrements().get(0);
      assertThat(businessDayIncrement.getDescription(), is(newDescription));
      assertThat(businessDay.hasDescription(), is(true));
   }

   @Test
   void testChangeDBIncTicketActivity() {

      // Given
      int serviceCode = 113;
      int expectedNewServiceCode = 111;
      TestCaseBuilder tcb = new TestCaseBuilder()
              .withBusinessDayIncrement("SYRIUS-11111", "Test", serviceCode, 3600 * 1000)
              .build();
      BusinessDayIncrement BusinessDayIncrement = tcb.timeRecorder.getBusinessDay().getIncrements().get(0);

      TicketActivity newTicketActivity = TicketActivityFactory.INSTANCE.createNew("Meeting", 111);
      ChangedValue changeValue = ChangedValue.of(BusinessDayIncrement.getId(), newTicketActivity, ValueTypes.TICKET_ACTIVITY);

      // When
      tcb.timeRecorder.changeBusinesDayIncrement(changeValue);

      // Then
      BusinessDayIncrement businessDayIncrement = tcb.timeRecorder.getBusinessDay().getIncrements().get(0);
      assertThat(businessDayIncrement.getTicketActivity().getActivityCode(), is(expectedNewServiceCode));
   }

   @Test
   void testChangeDBIncServiceCode_Invalid() {

      // Given
      // Given
      int currentServiceCode = 113;
      TestCaseBuilder tcb = new TestCaseBuilder()
              .withBusinessDayIncrement("SYRIUS-11111", "Test", currentServiceCode, 3600 * 1000)
              .build();
      BusinessDayIncrement BusinessDayIncrement = tcb.timeRecorder.getBusinessDay().getIncrements().get(0);

      TicketActivity newTicketActivity = TicketActivityFactory.INSTANCE.dummy("Schubedibuuu", -1);
      ChangedValue changeValue = ChangedValue.of(BusinessDayIncrement.getId(), newTicketActivity, ValueTypes.TICKET_ACTIVITY);

      // When
      tcb.timeRecorder.changeBusinesDayIncrement(changeValue);

      // Then
      BusinessDayIncrement businessDayIncrement = tcb.timeRecorder.getBusinessDay().getIncrements().get(0);
      assertThat(businessDayIncrement.getTicketActivity().getActivityCode(), is(currentServiceCode));
   }

   @Test
   void testHasContent() {

      // Given
      TestCaseBuilder tcb = new TestCaseBuilder()
              .withBusinessDayIncrement("SYRIUS-48642", "Test", 113, 3600 * 1000)
              .build();

      // When
      boolean actualHasContent = tcb.timeRecorder.hasContent();
      BusinessDay businessDay = tcb.timeRecorder.getBusinessDay();

      // Then
      assertThat(actualHasContent, is(true));
      assertThat(businessDay.hasNotChargedElements(), is(true));
   }

   @Test
   void testGetInfoStringForStateNotWorking() {

      // Given
      String expectedInfoStatePrefix = "Zeiterfassung inaktiv seit: ";
      TestCaseBuilder tcb = new TestCaseBuilder()
              .withBusinessDayIncrement("SYRIUS-1234", "Test", 113, 3600 * 1000)
              .build();

      // When
      String infoStringForStateNotWorking = tcb.timeRecorder.getInfoStringForState();

      // Then
      assertThat(infoStringForStateNotWorking.startsWith(expectedInfoStatePrefix), is(true));
   }

   @Test
   void testGetInfoStringForStateNotWorking_WithoutBusinessDayIncrement() {

      // Given
      TimeRecorderImpl timeRecorderImpl = TestCaseBuilder.mockTimeRecorderImpl();
      String expectedInfoStatePrefix = TextLabel.CAPTURING_INACTIVE;
      // When
      String infoStringForStateNotWorking = timeRecorderImpl.getInfoStringForState();

      // Then
      assertThat(infoStringForStateNotWorking.equals(expectedInfoStatePrefix), is(true));
   }

   @Test
   void testGetInfoStringForStateWorking() {

      // Given
      TimeRecorderImpl timeRecorderImpl = TestCaseBuilder.mockTimeRecorderImpl();
      timeRecorderImpl.setCallbackHandler(mock(TestUiCallbackHandler.class));
      String expectedInfoStatePrefix = "Zeiterfassung aktiv seit: ";

      // When
      timeRecorderImpl.handleUserInteraction(false);
      String infoStringForStateNotWorking = timeRecorderImpl.getInfoStringForState();

      // Then
      assertThat(infoStringForStateNotWorking.startsWith(expectedInfoStatePrefix), is(true));
   }

   @Test
   void testGetInfoStringForStateComeAndGo() {

      // Given
      TimeRecorderImpl timeRecorderImpl = TestCaseBuilder.mockTimeRecorderImpl();
      timeRecorderImpl.setCallbackHandler(mock(TestUiCallbackHandler.class));
      String expectedInfoStatePrefix = TextLabel.CAPTURING_INACTIVE + ". " + TextLabel.COME_OR_GO;

      // When
      timeRecorderImpl.handleUserInteraction(true);
      String infoStringForStateNotWorking = timeRecorderImpl.getInfoStringForState();

      // Then
      assertThat(infoStringForStateNotWorking.startsWith(expectedInfoStatePrefix), is(true));
   }

   @Test
   void testGetInfoStringForStateBooking() throws InterruptedException {

      // Given
      TestCaseBuilder tcb = new TestCaseBuilder()
              .withBookerAdapter(mockBookAdapter(true, BookResultType.FAILURE, 500))
              .withUiCallbackHandler(mock(UiCallbackHandler.class))
              .withBusinessDayIncrement("SYRIUS-99999", "fest", 113, 3600 * 1000)
              .build();

      // When
      Thread startBookThread = new Thread(() -> tcb.timeRecorder.book());
      startBookThread.start();
      TimeUnit.MILLISECONDS.sleep(50);
      String infoStringForStateBooking = tcb.timeRecorder.getInfoStringForState();

      // Then
      assertThat(infoStringForStateBooking.equals(TextLabel.BOOKING_RUNNING), is(true));
   }

   @Test
   void testBook_NothingToBook() {

      // Given
      BookerAdapter bookAdapter = mock(BookerAdapter.class);
      TimeRecorder timeRecorder = TestCaseBuilder.mockTimeRecorderImpl(bookAdapter);
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
      BookerAdapter bookerAdapter = mockBookAdapter(false, BookResultType.FAILURE);
      TestUiCallbackHandler uiCallbackHandler = mock(TestUiCallbackHandler.class);
      TestCaseBuilder tcb = new TestCaseBuilder()
              .withBookerAdapter(bookerAdapter)
              .withBusinessDayIncrement("SYRIUS-34345", "fest", 113, 3600 * 1000)
              .build();

      // When
      boolean actualHasNotChargedElements = tcb.timeRecorder.hasNotChargedElements();
      tcb.timeRecorder.book();

      // Then
      verify(bookerAdapter).book(any());
      verify(uiCallbackHandler, never()).displayMessage(any());
      assertThat(uiCallbackHandler.onBusinessDayChangedCount, is(0));
      assertThat(actualHasNotChargedElements, is(true));
   }

   @Test
   void testBook_BookSuccess() {

      // Given
      BookerAdapter bookAdapter = mockBookAdapter(true, BookResultType.SUCCESS);
      TestUiCallbackHandler uiCallbackHandler = spy(new TestUiCallbackHandler());
      TestCaseBuilder tcb = new TestCaseBuilder()
              .withBookerAdapter(bookAdapter)
              .withUiCallbackHandler(uiCallbackHandler)
              .withBusinessDayIncrement("SYRIUS-456456", "fest", 113, 3600 * 1000)
              .build();

      // When
      tcb.timeRecorder.book();

      // Then
      verify(bookAdapter).book(any());
      assertThat(uiCallbackHandler.onBusinessDayChangedCount, is(1));
      assertThat(uiCallbackHandler.receivedMessageType, is(MessageType.INFORMATION));
   }

   @Test
   void testBook_BookPartialSuccess() {

      // Given
      BookBusinessDayMessageSender bookBusinessDayMessageSender = mock(BookBusinessDayMessageSender.class);
      TestCaseBuilder tcb = new TestCaseBuilder()
              .withBookerAdapter(mockBookAdapter(true, BookResultType.PARTIAL_SUCCESS_WITH_ERROR))
              .withBusinessDayRepository(mockBusinessDayRepository(new BusinessDayImpl()))
              .withBookBusinessDayMessageSender(bookBusinessDayMessageSender)
              .withUiCallbackHandler(spy(new TestUiCallbackHandler()))
              .withBusinessDayIncrement("SYRIUS-25345", "fest", 113, 3600 * 1000)
              .build();

      // When
      tcb.timeRecorder.book();

      // Then
      verify(tcb.bookerAdapter).book(any());
      verify(bookBusinessDayMessageSender).sendBookedIncrements(any());
      assertThat(((TestUiCallbackHandler) tcb.uiCallbackHandler).onBusinessDayChangedCount, is(1));
      assertThat(((TestUiCallbackHandler) tcb.uiCallbackHandler).receivedMessageType, is(MessageType.WARNING));
   }

   @Test
   void testBook_BookPartialSuccess2() {

      // Given
      BookBusinessDayMessageSender bookBusinessDayMessageSender = mock(BookBusinessDayMessageSender.class);
      TestCaseBuilder tcb = new TestCaseBuilder()
              .withBookerAdapter(mockBookAdapter(true, BookResultType.PARTIAL_SUCCESS_WITH_NON_BOOKABLE))
              .withUiCallbackHandler(spy(new TestUiCallbackHandler()))
              .withBusinessDayRepository(mockBusinessDayRepository(new BusinessDayImpl()))
              .withBookBusinessDayMessageSender(bookBusinessDayMessageSender)
              .withBusinessDayIncrement("SYRIUS-5656", "fest", 113, 3600 * 1000)
              .build();

      // When
      tcb.timeRecorder.book();

      // Then
      verify(tcb.bookerAdapter).book(any());
      verify(bookBusinessDayMessageSender).sendBookedIncrements(any());
      assertThat(((TestUiCallbackHandler) tcb.uiCallbackHandler).onBusinessDayChangedCount, is(1));
      assertThat(((TestUiCallbackHandler) tcb.uiCallbackHandler).receivedMessageType, is(MessageType.WARNING));
   }

   @Test
   void testBook_BookPartialFailure() {

      // Given
      BookBusinessDayMessageSender bookBusinessDayMessageSender = mock(BookBusinessDayMessageSender.class);
      TestCaseBuilder tcb = new TestCaseBuilder()
              .withBookerAdapter(mockBookAdapter(true, BookResultType.FAILURE))
              .withBusinessDayRepository(mockBusinessDayRepository(new BusinessDayImpl()))
              .withBookBusinessDayMessageSender(bookBusinessDayMessageSender)
              .withUiCallbackHandler(new TestUiCallbackHandler())
              .withBusinessDayIncrement("SYRIUS-65468", "fest", 113, 3600 * 1000)
              .build();

      // When
      tcb.timeRecorder.book();

      // Then
      verify(tcb.bookerAdapter).book(any());
      verify(bookBusinessDayMessageSender).sendBookedIncrements(any());
      assertThat(((TestUiCallbackHandler) tcb.uiCallbackHandler).onBusinessDayChangedCount, is(1));
      assertThat(((TestUiCallbackHandler) tcb.uiCallbackHandler).receivedMessageType, is(MessageType.ERROR));
   }

   private BookerAdapter mockBookAdapter(boolean hasBooked, BookResultType bookResultType, int delayDuringBooking) {
      return spy(new TestBookAdapter(hasBooked, bookResultType, delayDuringBooking));
   }

   private BookerAdapter mockBookAdapter(boolean hasBooked, BookResultType bookResultType) {
      return mockBookAdapter(hasBooked, bookResultType, 0);
   }

   private static class TestCaseBuilder {

      private TimeRecorder timeRecorder;
      private final List<BusinessDayIncrementAdd> businessDayIncrementAdds;

      private final Settings settings;
      private BookerAdapter bookerAdapter;
      private UiCallbackHandler uiCallbackHandler;
      private BusinessDayRepository businessDayRepository;
      private BookBusinessDayMessageSender bookBusinessDayMessageSender;

      private TestCaseBuilder() {
         this.bookBusinessDayMessageSender = mock(BookBusinessDayMessageSender.class);
         this.businessDayRepository = mockBusinessDayRepository(new BusinessDayImpl());
         this.bookerAdapter = mock(BookerAdapter.class);
         this.settings = mock(Settings.class);
         this.businessDayIncrementAdds = new ArrayList<>();
      }

      private TestCaseBuilder withBusinessDayIncrement(String ticketNr, String description, int serviceCode, int timeSnippedDuration) {
         Ticket ticket = mockTicket(ticketNr);
         businessDayIncrementAdds.add(new BusinessDayIncrementAddBuilder()
                 .withTimeSnippet(createTimeSnippet(timeSnippedDuration))
                 .withDescription(description)
                 .withTicket(ticket)
                 .withId(UUID.randomUUID())
                 .withTicketActivity(TicketActivityFactory.INSTANCE.createNew("test", serviceCode))
                 .build());
         return this;
      }

      private Ticket mockTicket(String ticketNr) {
         Ticket ticket = mock(Ticket.class);
         when(ticket.getNr()).thenReturn(ticketNr);
         return ticket;
      }

      private TestCaseBuilder withBookerAdapter(BookerAdapter bookerAdapter) {
         this.bookerAdapter = bookerAdapter;
         return this;
      }

      private TestCaseBuilder withUiCallbackHandler(UiCallbackHandler uiCallbackHandler) {
         this.uiCallbackHandler = uiCallbackHandler;
         return this;
      }

      private TestCaseBuilder build() {
         timeRecorder = new TimeRecorderImpl(bookerAdapter, settings, businessDayRepository, bookBusinessDayMessageSender);
         timeRecorder.init();
         addBusinessIncrements();
         // set callback handler after we added the business-day calling the timerecorder-api, otherwise the 'addBusinessIncrements' already triggers it
         timeRecorder.setCallbackHandler(uiCallbackHandler);
         when(bookBusinessDayMessageSender.sendBookedIncrements(any())).thenReturn(timeRecorder.getBusinessDay());
         return this;
      }

      private void addBusinessIncrements() {
         for (BusinessDayIncrementAdd businessDayIncrementAdd : businessDayIncrementAdds) {
            timeRecorder.addBusinessIncrement(businessDayIncrementAdd);
         }
      }

      private TimeSnippet createTimeSnippet(int timeBetweenBeginAndEnd) {
         GregorianCalendar startDate = new GregorianCalendar(2020, Calendar.FEBRUARY, 1);// year, month, day, hours, min, second
         DateTime beginTimeStamp = DateTimeFactory.createNew(startDate.getTimeInMillis());
         return TimeSnippetBuilder.of()
                 .withBeginTime(beginTimeStamp)
                 .withEndTime(DateTimeFactory.createNew(startDate.getTimeInMillis() + timeBetweenBeginAndEnd))
                 .build();
      }

      private static TimeRecorderImpl mockTimeRecorderImpl() {
         return mockTimeRecorderImpl(mock(BookerAdapter.class), mock(Settings.class), new BusinessDayImpl());
      }

      private static TimeRecorderImpl mockTimeRecorderImpl(Settings settings) {
         return mockTimeRecorderImpl(mock(BookerAdapter.class), settings, new BusinessDayImpl());
      }

      private static TimeRecorder mockTimeRecorderImpl(BusinessDayImpl businessDay) {
         return mockTimeRecorderImpl(mock(BookerAdapter.class), mock(Settings.class), businessDay);
      }

      private static TimeRecorder mockTimeRecorderImpl(BookerAdapter bookAdapter) {
         return mockTimeRecorderImpl(bookAdapter, mock(Settings.class));
      }

      private static TimeRecorderImpl mockTimeRecorderImpl(BookerAdapter bookerAdapter, Settings settings) {
         return mockTimeRecorderImpl(bookerAdapter, settings, new BusinessDayImpl());
      }

      private static TimeRecorderImpl mockTimeRecorderImpl(BookerAdapter bookerAdapter, Settings settings, BusinessDay businessDay) {
         BusinessDayRepository businessDayRepository = mockBusinessDayRepository(businessDay);
         BookBusinessDayMessageSender bookBusinessDayMessageSender = mock(BookBusinessDayMessageSender.class);
         when(bookBusinessDayMessageSender.sendBookedIncrements(any())).thenReturn(businessDay);
         TimeRecorderImpl timeRecorderImpl = new TimeRecorderImpl(bookerAdapter, settings, businessDayRepository, bookBusinessDayMessageSender);
         timeRecorderImpl.init();
         return timeRecorderImpl;
      }

      public TestCaseBuilder withBusinessDayRepository(BusinessDayRepository businessDayRepository) {
         this.businessDayRepository = businessDayRepository;
         return this;
      }

      public TestCaseBuilder withBookBusinessDayMessageSender(BookBusinessDayMessageSender bookBusinessDayMessageSender) {
         this.bookBusinessDayMessageSender = bookBusinessDayMessageSender;
         return this;
      }
   }

   private BusinessDayImpl mockBusinessDayImpl() {
      BusinessDayImpl businessDay = mock(BusinessDayImpl.class);
      when(businessDay.getComeAndGoes()).thenReturn(ComeAndGoesImpl.of());
      return businessDay;
   }

   private static class TestBookAdapter implements BookerAdapter {

      private final BookResultType bookResultType;
      private final boolean hasBooked;
      private final int delayDuringBooking;
      private BusinessDay bookedBusinessDay;

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
      public void init() {
         // well, theres nothing to do
      }

      @Override
      public BookerResult book(BusinessDay businessDay) {
         try {
            this.bookedBusinessDay = businessDay;
            TimeUnit.MILLISECONDS.sleep(delayDuringBooking);
         } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException(e);
         }
         return new BookerResult() {

            @Override
            public BusinessDay getBookedBusinessDay() {
               return bookedBusinessDay;
            }

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

      @Override
      public boolean isTicketBookable(Ticket ticket) {
         return true;
      }
   }

   private static class TestUiCallbackHandler implements UiCallbackHandler {

      private MessageType receivedMessageType;
      private int onBusinessDayChangedCount = 0;

      @Override
      public void onBusinessDayChanged() {
         this.onBusinessDayChangedCount++;
      }

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

      @Override
      public void onCome() {
         // empty
      }

      @Override
      public void onGo() {
         // empty
      }
   }
}
