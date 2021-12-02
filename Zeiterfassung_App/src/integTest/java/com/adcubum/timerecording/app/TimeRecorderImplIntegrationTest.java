package com.adcubum.timerecording.app;

import static com.adcubum.timerecording.core.work.businessday.repository.BusinessDayRepositoryIntegMockUtil.mockBusinessDayRepository;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Test;

import com.adcubum.librarys.text.res.TextLabel;
import com.adcubum.timerecording.app.startstopresult.StartNotPossibleInfo;
import com.adcubum.timerecording.app.startstopresult.UserInteractionResult;
import com.adcubum.timerecording.core.book.adapter.BookerAdapter;
import com.adcubum.timerecording.core.book.adapter.ServiceCodeAdapter;
import com.adcubum.timerecording.core.book.result.BookResultType;
import com.adcubum.timerecording.core.book.result.BookerResult;
import com.adcubum.timerecording.core.callbackhandler.UiCallbackHandler;
import com.adcubum.timerecording.core.work.businessday.BusinessDay;
import com.adcubum.timerecording.core.work.businessday.BusinessDayImpl;
import com.adcubum.timerecording.core.work.businessday.BusinessDayIncrement;
import com.adcubum.timerecording.core.work.businessday.TimeSnippet;
import com.adcubum.timerecording.core.work.businessday.TimeSnippetImpl.TimeSnippetBuilder;
import com.adcubum.timerecording.core.work.businessday.ValueTypes;
import com.adcubum.timerecording.core.work.businessday.comeandgo.ComeAndGo;
import com.adcubum.timerecording.core.work.businessday.comeandgo.ComeAndGoes;
import com.adcubum.timerecording.core.work.businessday.comeandgo.impl.ComeAndGoesImpl;
import com.adcubum.timerecording.core.work.businessday.repository.BusinessDayRepository;
import com.adcubum.timerecording.core.work.businessday.update.callback.impl.BusinessDayIncrementAdd;
import com.adcubum.timerecording.core.work.businessday.update.callback.impl.BusinessDayIncrementAdd.BusinessDayIncrementAddBuilder;
import com.adcubum.timerecording.core.work.businessday.update.callback.impl.ChangedValue;
import com.adcubum.timerecording.integtest.BaseTestWithSettings;
import com.adcubum.timerecording.jira.data.ticket.Ticket;
import com.adcubum.timerecording.message.Message;
import com.adcubum.timerecording.message.MessageType;
import com.adcubum.timerecording.settings.Settings;
import com.adcubum.timerecording.work.date.DateTime;
import com.adcubum.timerecording.work.date.DateTimeFactory;

class TimeRecorderImplIntegrationTest extends BaseTestWithSettings {

   @Test
   void testGetSettingsValue() {
      // Given
      String key = "key";
      Settings settings = mock(Settings.class);
      TimeRecorder timeRecorderImpl = mockTimeRecorderImpl(settings);

      // When
      timeRecorderImpl.getSettingsValue(key);

      // Then
      verify(settings).getSettingsValue(any());
   }

   private TimeRecorderImpl mockTimeRecorderImpl() {
      return mockTimeRecorderImpl(mock(BookerAdapter.class), mock(Settings.class), new BusinessDayImpl());
   }

   private TimeRecorderImpl mockTimeRecorderImpl(Settings settings) {
      return mockTimeRecorderImpl(mock(BookerAdapter.class), settings, new BusinessDayImpl());
   }


   private TimeRecorder mockTimeRecorderImpl(BusinessDayImpl businessDay) {
      return mockTimeRecorderImpl(mock(BookerAdapter.class), mock(Settings.class), businessDay);
   }

   private TimeRecorder mockTimeRecorderImpl(BookerAdapter bookAdapter) {
      return mockTimeRecorderImpl(bookAdapter, mock(Settings.class));
   }

   private TimeRecorderImpl mockTimeRecorderImpl(BookerAdapter bookerAdapter, Settings settings) {
      return mockTimeRecorderImpl(bookerAdapter, settings, new BusinessDayImpl());
   }

   private TimeRecorderImpl mockTimeRecorderImpl(BookerAdapter bookerAdapter, Settings settings, BusinessDay businessDay) {
      BusinessDayRepository businessDayRepository = mockBusinessDayRepository(businessDay);
      TimeRecorderImpl timeRecorderImpl = new TimeRecorderImpl(bookerAdapter, settings, businessDayRepository);
      timeRecorderImpl.init();
      return timeRecorderImpl;
   }

   @Test
   void testSaveSettingValue() {
      // Given
      String key = "key";
      Settings settings = mock(Settings.class);
      TimeRecorder timeRecorderImpl = mockTimeRecorderImpl(settings);
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
      TestCaseBuilder tcb = new TestCaseBuilder(mockTimeRecorderImpl())
            .setCallbackHandler(testUiCallbackHandler)
            .build();

      // When
      tcb.timeRecorder.handleUserInteraction(true);// come
      tcb.timeRecorder.handleUserInteraction(true);// go
      BusinessDay bussinessDay = tcb.timeRecorder.getBussinessDay();
      int amountOfComeAndGoesBeforeDeletion = bussinessDay.getComeAndGoes().getComeAndGoEntries().size();
      tcb.timeRecorder.clearComeAndGoes();
      bussinessDay = tcb.timeRecorder.getBussinessDay();
      int amountOfComeAndGoesAfterDeletion = bussinessDay.getComeAndGoes().getComeAndGoEntries().size();

      // Then
      assertThat(amountOfComeAndGoesBeforeDeletion, is(1));
      assertThat(amountOfComeAndGoesAfterDeletion, is(0));
   }

   @Test
   void testCreateAndDeleteComeAndGoAndFlagAsRecorded() {

      // Given
      TestUiCallbackHandler testUiCallbackHandler = spy(new TestUiCallbackHandler());
      TestCaseBuilder tcb = new TestCaseBuilder(mockTimeRecorderImpl())
            .setCallbackHandler(testUiCallbackHandler)
            .build();

      // When
      tcb.timeRecorder.handleUserInteraction(true);// come
      tcb.timeRecorder.handleUserInteraction(true);// go
      tcb.timeRecorder.flagBusinessDayComeAndGoesAsRecorded();

      BusinessDay bussinessDay = tcb.timeRecorder.getBussinessDay();

      // Then
      assertThat(bussinessDay.getComeAndGoes().getComeAndGoEntries().size(), is(1));
      ComeAndGo comeAndGo = bussinessDay.getComeAndGoes().getComeAndGoEntries().get(0);
      assertThat(comeAndGo.isNotRecorded(), is(false));
   }

   @Test
   void testRefreshDummyTickets() {

      // Given
      BusinessDayImpl businessDay = mockBusinessDayImpl();
      TimeRecorder timeRecorder = mockTimeRecorderImpl(businessDay);

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
      int kindOfService = 113;
      int timeSnippedDuration = 3600 * 1000;
      TestCaseBuilder tcb = new TestCaseBuilder(mockTimeRecorderImpl())
            .withBusinessDayIncrement(ticketNr, description, kindOfService, timeSnippedDuration)
            .build();

      // When
      tcb.timeRecorder.removeIncrement4Id(UUID.randomUUID());

      // Then
      BusinessDay bussinessDay = tcb.timeRecorder.getBussinessDay();
      assertThat(bussinessDay.getIncrements().size(), is(1));
   }

   @Test
   void testRemoveBusinessIncrementAtIndex() {
      // Given
      TestCaseBuilder tcb = new TestCaseBuilder(mockTimeRecorderImpl())
            .withBusinessDayIncrement("SYRIUS-45645", "Test3", 113, 3600 * 1000)
            .build();

      // When
      BusinessDay bussinessDay = tcb.timeRecorder.getBussinessDay();
      BusinessDayIncrement BusinessDayIncrement = tcb.timeRecorder.getBussinessDay().getIncrements().get(0);
      assertThat(bussinessDay.getIncrements().size(), is(1));
      tcb.timeRecorder.removeIncrement4Id(BusinessDayIncrement.getId());

      // Then
      bussinessDay = tcb.timeRecorder.getBussinessDay();
      assertThat(bussinessDay.getIncrements().isEmpty(), is(true));
   }

   @Test
   void testClearAllBusinessIncrements() {
      // Given
      TestCaseBuilder tcb = new TestCaseBuilder(mockTimeRecorderImpl())
            .withBusinessDayIncrement("SYRIUS-42353", "Test88", 113, 3600)
            .build();

      // When
      BusinessDay bussinessDay = tcb.timeRecorder.getBussinessDay();
      assertThat(bussinessDay.getIncrements().size(), is(1));
      tcb.timeRecorder.clear();

      // Then
      bussinessDay = tcb.timeRecorder.getBussinessDay();
      assertThat(bussinessDay.getIncrements().isEmpty(), is(true));
   }

   @Test
   void testStartWithElementsFromPrecedentDay_DoNotStart() {
      // Given, add an existing increment from the 01.02.2020
      TestUiCallbackHandler testUiCallbackHandler = spy(new TestUiCallbackHandler());
      TestCaseBuilder tcb = new TestCaseBuilder(mockTimeRecorderImpl())
            .withBusinessDayIncrement("SYRIUS-534534", "Test", 113, 600 * 1000)
            .setCallbackHandler(testUiCallbackHandler)
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
      TimeRecorder timeRecorder = mockTimeRecorderImpl(businessDay);
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
      TestCaseBuilder tcb = new TestCaseBuilder(mockTimeRecorderImpl())
            .withBusinessDayIncrement("SYRIUS-534534", "Test", 113, 600 * 1000)
            .setCallbackHandler(testUiCallbackHandler)
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
      TimeRecorderImpl timeRecorderImpl = mockTimeRecorderImpl();
      UiCallbackHandler testUiCallbackHandler = spy(new TestUiCallbackHandler());
      timeRecorderImpl.setCallbackHandler(testUiCallbackHandler);

      // When
      timeRecorderImpl.handleUserInteraction(false);
      TimeUnit.MILLISECONDS.sleep(500);
      timeRecorderImpl.handleUserInteraction(false);

      // Then
      BusinessDay bussinessDay = timeRecorderImpl.getBussinessDay();
      boolean actualHasContent = timeRecorderImpl.hasContent();
      assertThat(bussinessDay.getIncrements().isEmpty(), is(true));
      assertThat(actualHasContent, is(false));
      TimeSnippet currentTimeSnippet = timeRecorderImpl.getCurrentBussinessDayIncrement();
      assertThat(currentTimeSnippet.getBeginTimeStamp(), is(notNullValue()));
      assertThat(currentTimeSnippet.getEndTimeStamp(), is(notNullValue()));
      verify(testUiCallbackHandler).onStart();
      verify(testUiCallbackHandler).onStop();
   }

   @Test
   void testStartNewBusinessIncrement() {
      // Given
      TimeRecorderImpl timeRecorderImpl = mockTimeRecorderImpl();
      UiCallbackHandler testUiCallbackHandler = spy(new TestUiCallbackHandler());
      timeRecorderImpl.setCallbackHandler(testUiCallbackHandler);

      // When
      timeRecorderImpl.handleUserInteraction(false);
      boolean isRecordingAfterFirstHandle = timeRecorderImpl.isRecording();
      boolean actualIsBooking = timeRecorderImpl.isBooking();

      // Then
      TimeSnippet currentTimeSnippet = timeRecorderImpl.getCurrentBussinessDayIncrement();
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
      TimeRecorderImpl timeRecorderImpl = mockTimeRecorderImpl();
      timeRecorderImpl.setCallbackHandler(testUiCallbackHandler);

      // When
      timeRecorderImpl.handleUserInteraction(false);
      boolean isRecordingAfterFirstHandle = timeRecorderImpl.isRecording();
      TimeUnit.MILLISECONDS.sleep(500);

      timeRecorderImpl.handleUserInteraction(false);
      boolean isRecordingAfterSecondHandle = timeRecorderImpl.isRecording();
      TimeSnippet currentTimeSnippet = timeRecorderImpl.getCurrentBussinessDayIncrement();
      assertThat(currentTimeSnippet.getBeginTimeStamp(), is(notNullValue()));
      assertThat(currentTimeSnippet.getEndTimeStamp(), is(notNullValue()));

      timeRecorderImpl.resume();
      currentTimeSnippet = timeRecorderImpl.getCurrentBussinessDayIncrement();
      assertThat(currentTimeSnippet.getBeginTimeStamp(), is(notNullValue()));
      assertThat(currentTimeSnippet.getEndTimeStamp(), is(nullValue()));

      // Then
      BusinessDay bussinessDay = timeRecorderImpl.getBussinessDay();
      assertThat(bussinessDay.getIncrements().isEmpty(), is(true));
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
      BusinessDayRepository businessDayRepository = mockBusinessDayRepository(new BusinessDayImpl());
      TimeRecorder timeRecorder = new TimeRecorderImpl(bookAdapter, businessDayRepository);
      TestUiCallbackHandler uiCallbackHandler = new TestUiCallbackHandler();
      timeRecorder.setCallbackHandler(uiCallbackHandler);
      new TestCaseBuilder(timeRecorder)
            .withBusinessDayIncrement("SYRIUS-65468", "fest", 113, 3600 * 1000)
            .build();

      // When
      Thread startBookThread = new Thread(() -> timeRecorder.book());
      startBookThread.start();
      TimeUnit.MILLISECONDS.sleep(50);
      boolean actualIsBooking = timeRecorder.isBooking();
      UserInteractionResult actualHandleResult = timeRecorder.handleUserInteraction(false);

      // Then
      verify(bookAdapter).book(any());
      assertThat(actualIsBooking, is(true));
      assertThat(actualHandleResult.isUserInteractionRequired(), is(false));
   }

   @Test
   void testBook_StartWhileComeAndGoIsEnabled() throws InterruptedException {

      // Given

      TestCaseBuilder tcb = new TestCaseBuilder(mockTimeRecorderImpl())
            .setCallbackHandler(new TestUiCallbackHandler())
            .build();

      // When
      // start come and go
      tcb.timeRecorder.handleUserInteraction(true);
      UserInteractionResult actualUserInteractionResult = tcb.timeRecorder.handleUserInteraction(false);

      // Then
      ComeAndGoes comeAndGoes = tcb.timeRecorder.getBussinessDay().getComeAndGoes();
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
      int kindOfService = 113;
      TestCaseBuilder tcb = new TestCaseBuilder(mockTimeRecorderImpl())
            .withBusinessDayIncrement(ticketNr, description, kindOfService, firstTimeBetweenStartAndStop)
            .build();
      BusinessDayIncrement BusinessDayIncrement = tcb.timeRecorder.getBussinessDay().getIncrements().get(0);
      ChangedValue changeValue = ChangedValue.of(BusinessDayIncrement.getId(), newDescription, ValueTypes.DESCRIPTION);

      // When
      tcb.timeRecorder.changeBusinesDayIncrement(changeValue);

      // Then
      BusinessDay bussinessDay = tcb.timeRecorder.getBussinessDay();
      BusinessDayIncrement businessDayIncrement = tcb.timeRecorder.getBussinessDay().getIncrements().get(0);
      assertThat(businessDayIncrement.getDescription(), is(newDescription));
      assertThat(bussinessDay.hasDescription(), is(true));
   }

   @Test
   void testChangeDBIncChargeType() {

      // Given
      int kindOfService = 113;
      int expectedNewChargeType = 111;
      TestCaseBuilder tcb = new TestCaseBuilder(mockTimeRecorderImpl())
            .withBusinessDayIncrement("SYRIUS-11111", "Test", kindOfService, 3600 * 1000)
            .build();
      BusinessDayIncrement BusinessDayIncrement = tcb.timeRecorder.getBussinessDay().getIncrements().get(0);

      ChangedValue changeValue = ChangedValue.of(BusinessDayIncrement.getId(), "111 - Meeting", ValueTypes.SERVICE_CODE_DESCRIPTION);

      // When
      tcb.timeRecorder.changeBusinesDayIncrement(changeValue);

      // Then
      BusinessDayIncrement businessDayIncrement = tcb.timeRecorder.getBussinessDay().getIncrements().get(0);
      assertThat(businessDayIncrement.getChargeType(), is(expectedNewChargeType));
   }

   @Test
   void testChangeDBIncChargeType_Invalid() {

      // Given
      // Given
      int currentServiceCode = 113;
      TestCaseBuilder tcb = new TestCaseBuilder(mockTimeRecorderImpl())
            .withBusinessDayIncrement("SYRIUS-11111", "Test", currentServiceCode, 3600 * 1000)
            .build();
      BusinessDayIncrement BusinessDayIncrement = tcb.timeRecorder.getBussinessDay().getIncrements().get(0);

      ChangedValue changeValue = ChangedValue.of(BusinessDayIncrement.getId(), "Schubedibuuu", ValueTypes.SERVICE_CODE_DESCRIPTION);

      // When
      tcb.timeRecorder.changeBusinesDayIncrement(changeValue);

      // Then
      BusinessDayIncrement businessDayIncrement = tcb.timeRecorder.getBussinessDay().getIncrements().get(0);
      assertThat(businessDayIncrement.getChargeType(), is(currentServiceCode));
   }

   @Test
   void testHasContent() {

      // Given
      TestCaseBuilder tcb = new TestCaseBuilder(mockTimeRecorderImpl())
            .withBusinessDayIncrement("SYRIUS-48642", "Test", 113, 3600 * 1000)
            .build();

      // When
      boolean actualHasContent = tcb.timeRecorder.hasContent();
      BusinessDay bussinessDay = tcb.timeRecorder.getBussinessDay();

      // Then
      assertThat(actualHasContent, is(true));
      assertThat(bussinessDay.hasNotChargedElements(), is(true));
   }

   @Test
   void testGetInfoStringForStateNotWorking() {

      // Given
      String expectedInfoStatePrefix = "Zeiterfassung inaktiv seit: ";
      TestCaseBuilder tcb = new TestCaseBuilder(mockTimeRecorderImpl())
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
      TimeRecorderImpl timeRecorderImpl = mockTimeRecorderImpl();
      String expectedInfoStatePrefix = TextLabel.CAPTURING_INACTIVE;
      // When
      String infoStringForStateNotWorking = timeRecorderImpl.getInfoStringForState();

      // Then
      assertThat(infoStringForStateNotWorking.equals(expectedInfoStatePrefix), is(true));
   }

   @Test
   void testGetInfoStringForStateWorking() {

      // Given
      TimeRecorderImpl timeRecorderImpl = mockTimeRecorderImpl();
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
      TimeRecorderImpl timeRecorderImpl = mockTimeRecorderImpl();
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
      BookerAdapter bookAdapter = mockBookAdapter(true, BookResultType.FAILURE, 500);
      TimeRecorder timeRecorder = mockTimeRecorderImpl(bookAdapter);
      timeRecorder.setCallbackHandler(mock(UiCallbackHandler.class));
      new TestCaseBuilder(timeRecorder)
            .withBusinessDayIncrement("SYRIUS-99999", "fest", 113, 3600 * 1000)
            .build();

      // When
      Thread startBookThread = new Thread(() -> timeRecorder.book());
      startBookThread.start();
      TimeUnit.MILLISECONDS.sleep(50);
      String infoStringForStateBooking = timeRecorder.getInfoStringForState();

      // Then
      assertThat(infoStringForStateBooking.equals(TextLabel.BOOKING_RUNNING), is(true));
   }

   @Test
   void testBook_NothingToBook() {

      // Given
      BookerAdapter bookAdapter = mock(BookerAdapter.class);
      TimeRecorder timeRecorder = mockTimeRecorderImpl(bookAdapter);
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
      TimeRecorder timeRecorder = mockTimeRecorderImpl(bookAdapter);
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
      TimeRecorder timeRecorder = mockTimeRecorderImpl(bookAdapter);
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
      BusinessDayRepository businessDayRepository = mockBusinessDayRepository(new BusinessDayImpl());
      TimeRecorder timeRecorder = new TimeRecorderImpl(bookAdapter, businessDayRepository);
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
      BusinessDayRepository businessDayRepository = mockBusinessDayRepository(new BusinessDayImpl());
      TimeRecorder timeRecorder = new TimeRecorderImpl(bookAdapter, businessDayRepository);
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
      BusinessDayRepository businessDayRepository = mockBusinessDayRepository(new BusinessDayImpl());
      TimeRecorder timeRecorder = new TimeRecorderImpl(bookAdapter, businessDayRepository);
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
               .withTimeSnippet(createTimeSnippet(timeSnippedDuration, false))
               .withDescription(description)
               .withTicket(ticket)
               .withId(UUID.randomUUID())
               .withServiceCode(kindOfService)
               .build());
         return this;
      }

      private Ticket mockTicket(String ticketNr) {
         Ticket ticket = mock(Ticket.class);
         when(ticket.getNr()).thenReturn(ticketNr);
         return ticket;
      }

      private TestCaseBuilder build() {
         timeRecorder.init();
         addBusinessIncrements();
         return this;
      }

      private void addBusinessIncrements() {
         for (BusinessDayIncrementAdd businessDayIncrementAdd : businessDayIncrementAdds) {
            timeRecorder.addBusinessIncrement(businessDayIncrementAdd);
         }
      }

      private TimeSnippet createTimeSnippet(int timeBetweenBeginAndEnd, boolean isToday) {
         GregorianCalendar startDate = new GregorianCalendar(2020, 1, 1);// year, month (starts at zero!), day, hours, min, second
         if (isToday) {
            startDate = new GregorianCalendar();
         } else {
            startDate = new GregorianCalendar(2020, 1, 1);// year, month (starts at zero!), day, hours, min, second
         }
         DateTime beginTimeStamp = DateTimeFactory.createNew(startDate.getTimeInMillis());
         return TimeSnippetBuilder.of()
               .withBeginTime(beginTimeStamp)
               .withEndTime(DateTimeFactory.createNew(startDate.getTimeInMillis() + timeBetweenBeginAndEnd))
               .build();
      }
   }

   private BusinessDayImpl mockBusinessDayImpl() {
      BusinessDayImpl businessDay = mock(BusinessDayImpl.class);
      when(businessDay.getComeAndGoes()).thenReturn(ComeAndGoesImpl.of());
      return businessDay;
   }

   private static class TestBookAdapter implements BookerAdapter {

      private BookResultType bookResultType;
      private boolean hasBooked;
      private int delayDuringBooking;
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
