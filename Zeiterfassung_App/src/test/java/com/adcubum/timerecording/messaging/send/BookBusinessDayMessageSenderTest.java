package com.adcubum.timerecording.messaging.send;

import com.adcubum.timerecording.app.TimeRecorder;
import com.adcubum.timerecording.core.work.businessday.*;
import com.adcubum.timerecording.core.work.businessday.update.callback.impl.BusinessDayIncrementAdd;
import com.adcubum.timerecording.jira.data.ticket.IssueType;
import com.adcubum.timerecording.jira.data.ticket.Ticket;
import com.adcubum.timerecording.jira.data.ticket.TicketActivity;
import com.adcubum.timerecording.jira.data.ticket.TicketAttrs;
import com.adcubum.timerecording.messaging.api.BookBusinessDayMessageApiService;
import com.adcubum.timerecording.messaging.api.model.BookSenderReceiverId;
import com.adcubum.timerecording.messaging.api.model.BusinessDayDto;
import com.adcubum.timerecording.messaging.send.mapping.BusinessDayIncrementsToBusinessDayDtoMapper;
import com.adcubum.timerecording.settings.Settings;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.function.Supplier;

import static com.adcubum.timerecording.core.work.businessday.update.callback.impl.BusinessDayIncrementAdd.BusinessDayIncrementAddBuilder;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class BookBusinessDayMessageSenderTest {

   @Test
   void testCanSend_HappyCase() {

      // Given
      TestCaseBuilder tcb = new TestCaseBuilder()
              .withBusinessDayIncrementAdd(new BusinessDayIncrementAddBuilder()
                      .withTicket(mockTicket(true, "ABC-123"))
                      .withDescription("Im booked")
                      .withTimeSnippet(createTimeSnippet(50))
                      .withTicketActivity(createTicketActivity("Im booked", 113))
                      .withId(UUID.randomUUID())
                      .build())
              .withBusinessDayIncrementAdd(new BusinessDayIncrementAddBuilder()
                      .withTicket(mockTicket(true, "ABC-114"))
                      .withDescription("Im booked")
                      .withTimeSnippet(createTimeSnippet(50))
                      .withTicketActivity(createTicketActivity("Im also booked", 111))
                      .withId(UUID.randomUUID())
                      .build())
              .withBookedBusinessDayIncrements("ABC-123", "ABC-114")
              .build();

      // When
      boolean isActualEnabled = tcb.bookBusinessDayMessageSender.isSendBookedBusinessDayIncrementsEnabled();

      // Then
      assertThat(isActualEnabled, is(true));
   }
   @Test
   void testCanSendNotBooked_NotEnabled() {

      // Given
      TestCaseBuilder tcb = new TestCaseBuilder()
              .withBusinessDayIncrementAdd(new BusinessDayIncrementAddBuilder()
                      .withTicket(mockTicket(true, "ABC-123"))
                      .withDescription("Im booked")
                      .withTimeSnippet(createTimeSnippet(50))
                      .withTicketActivity(createTicketActivity("Im booked", 113))
                      .withId(UUID.randomUUID())
                      .build())
              .build();

      // When
      boolean isActualEnabled = tcb.bookBusinessDayMessageSender.isSendBookedBusinessDayIncrementsEnabled();

      // Then
      assertThat(isActualEnabled, is(false));
   }

   @Test
   void testCanSend_Master_NotEnabled() {

      // Given
      TestCaseBuilder tcb = new TestCaseBuilder()
              .withBusinessDayIncrementAdd(new BusinessDayIncrementAddBuilder()
                      .withTicket(mockTicket(true, "ABC-123"))
                      .withDescription("Im booked")
                      .withTimeSnippet(createTimeSnippet(50))
                      .withTicketActivity(createTicketActivity("Im booked", 113))
                      .withId(UUID.randomUUID())
                      .build())
              .withBusinessDayIncrementAdd(new BusinessDayIncrementAddBuilder()
                      .withTicket(mockTicket(true, "ABC-114"))
                      .withDescription("Im booked")
                      .withTimeSnippet(createTimeSnippet(50))
                      .withTicketActivity(createTicketActivity("Im also booked", 111))
                      .withId(UUID.randomUUID())
                      .build())
              .withBookedBusinessDayIncrements("ABC-123", "ABC-114")
              .isMaster(true)
              .build();

      // When
      boolean isActualEnabled = tcb.bookBusinessDayMessageSender.isSendBookedBusinessDayIncrementsEnabled();

      // Then
      assertThat(isActualEnabled, is(false));
   }

   @Test
   void testCanSend_NoSenderId_NotEnabled() {

      // Given
      TestCaseBuilder tcb = new TestCaseBuilder()
              .withBusinessDayIncrementAdd(new BusinessDayIncrementAddBuilder()
                      .withTicket(mockTicket(true, "ABC-123"))
                      .withDescription("Im booked")
                      .withTimeSnippet(createTimeSnippet(50))
                      .withTicketActivity(createTicketActivity("Im booked", 113))
                      .withId(UUID.randomUUID())
                      .build())
              .withBusinessDayIncrementAdd(new BusinessDayIncrementAddBuilder()
                      .withTicket(mockTicket(true, "ABC-114"))
                      .withDescription("Im booked")
                      .withTimeSnippet(createTimeSnippet(50))
                      .withTicketActivity(createTicketActivity("Im also booked", 111))
                      .withId(UUID.randomUUID())
                      .build())
              .withBookedBusinessDayIncrements("ABC-123", "ABC-114")
              .isMaster(false)
              .withSenderId(null)
              .build();

      // When
      boolean isActualEnabled = tcb.bookBusinessDayMessageSender.isSendBookedBusinessDayIncrementsEnabled();

      // Then
      assertThat(isActualEnabled, is(false));
   }

   @Test
   void testHappyCase_TwoIncrements_BothBookedAndNotSent() {

      // Given
      TestCaseBuilder tcb = new TestCaseBuilder()
              .withBusinessDayIncrementAdd(new BusinessDayIncrementAddBuilder()
                      .withTicket(mockTicket(true, "ABC-123"))
                      .withDescription("Im booked")
                      .withTimeSnippet(createTimeSnippet(50))
                      .withTicketActivity(createTicketActivity("Im booked", 113))
                      .withId(UUID.randomUUID())
                      .build())
              .withBusinessDayIncrementAdd(new BusinessDayIncrementAddBuilder()
                      .withTicket(mockTicket(true, "ABC-114"))
                      .withDescription("Im booked")
                      .withTimeSnippet(createTimeSnippet(50))
                      .withTicketActivity(createTicketActivity("Im also booked", 111))
                      .withId(UUID.randomUUID())
                      .build())
              .withBookedBusinessDayIncrements("ABC-123", "ABC-114")
              .build();

      // When
      BusinessDay sentBusinessDay = tcb.bookBusinessDayMessageSender.sendBookedIncrements(tcb.businessDay);

      // Then
      for (BusinessDayIncrement sentBusinessDayIncrement : sentBusinessDay.getIncrements()) {
         assertThat(sentBusinessDayIncrement.isSent(), is(true));
      }
      BusinessDayDto expectedBusinessDayDto = BusinessDayIncrementsToBusinessDayDtoMapper.mapToBusinessDayDto(tcb.businessDay.getIncrements());
      verify(tcb.bookBusinessDayMessageApiService).createAndSendBookBusinessDayMessage(eq(expectedBusinessDayDto), eq(BookSenderReceiverId.NAG));
   }

   @Test
   void testTwoIncrements_BothBookedAndNotSent_SendFailure() {

      // Given
      TestCaseBuilder tcb = new TestCaseBuilder()
              .withSendFailure()
              .withBusinessDayIncrementAdd(new BusinessDayIncrementAddBuilder()
                      .withTicket(mockTicket(true, "ABC-123"))
                      .withDescription("Im booked")
                      .withTimeSnippet(createTimeSnippet(50))
                      .withTicketActivity(createTicketActivity("Im booked", 113))
                      .withId(UUID.randomUUID())
                      .build())
              .withBusinessDayIncrementAdd(new BusinessDayIncrementAddBuilder()
                      .withTicket(mockTicket(true, "ABC-114"))
                      .withDescription("Im booked")
                      .withTimeSnippet(createTimeSnippet(50))
                      .withTicketActivity(createTicketActivity("Im also booked", 111))
                      .withId(UUID.randomUUID())
                      .build())
              .withBookedBusinessDayIncrements("ABC-123", "ABC-114")
              .build();

      // When
      BusinessDay sentBusinessDay = tcb.bookBusinessDayMessageSender.sendBookedIncrements(tcb.businessDay);

      // Then
      for (BusinessDayIncrement sentBusinessDayIncrement : sentBusinessDay.getIncrements()) {
         assertThat(sentBusinessDayIncrement.isSent(), is(false));
      }
      BusinessDayDto expectedBusinessDayDto = BusinessDayIncrementsToBusinessDayDtoMapper.mapToBusinessDayDto(sentBusinessDay.getIncrements());
      verify(tcb.bookBusinessDayMessageApiService).createAndSendBookBusinessDayMessage(eq(expectedBusinessDayDto), eq(BookSenderReceiverId.NAG));
   }

   @Test
   void testPartialSuccess_TwoIncrementsOnlyOneBooked() {

      // Given
      UUID bookedIncrementId = UUID.randomUUID();
      TestCaseBuilder tcb = new TestCaseBuilder()
              .withBusinessDayIncrementAdd(new BusinessDayIncrementAddBuilder()
                      .withTicket(mockTicket(true, "ABC-123"))
                      .withDescription("Im booked")
                      .withTimeSnippet(createTimeSnippet(50))
                      .withTicketActivity(createTicketActivity("Im booked", 113))
                      .withId(bookedIncrementId)
                      .build())
              .withBusinessDayIncrementAdd(new BusinessDayIncrementAddBuilder()
                      .withTicket(mockTicket(false, "ABC-114"))
                      .withDescription("Im not booked")
                      .withTimeSnippet(createTimeSnippet(50))
                      .withTicketActivity(createTicketActivity("Im not booked", 111))
                      .withId(UUID.randomUUID())
                      .build())
              .withBookedBusinessDayIncrements("ABC-123")
              .build();

      // When
      BusinessDay sentBusinessDay = tcb.bookBusinessDayMessageSender.sendBookedIncrements(tcb.businessDay);

      // Then
      BusinessDayIncrement sentBusinessDayIncrementById = sentBusinessDay.getBusinessDayIncrementById(bookedIncrementId);
      assertThat(sentBusinessDayIncrementById.isSent(), is(true));
      BusinessDayDto expectedBusinessDayDto = BusinessDayIncrementsToBusinessDayDtoMapper.mapToBusinessDayDto(tcb.businessDay.getIncrements());
      verify(tcb.bookBusinessDayMessageApiService).createAndSendBookBusinessDayMessage(eq(expectedBusinessDayDto), eq(BookSenderReceiverId.NAG));
   }

   @Test
   void testFailure_TwoIncrementsNonBooked() {

      // Given
      TestCaseBuilder tcb = new TestCaseBuilder()
              .withBusinessDayIncrementAdd(new BusinessDayIncrementAddBuilder()
                      .withTicket(mockTicket(true, "ABC-113"))
                      .withDescription("Im booked")
                      .withTimeSnippet(createTimeSnippet(50))
                      .withTicketActivity(createTicketActivity("Im booked", 113))
                      .withId(UUID.randomUUID())
                      .build())
              .withBusinessDayIncrementAdd(new BusinessDayIncrementAddBuilder()
                      .withTicket(mockTicket(true, "ABC-114"))
                      .withDescription("Im not booked")
                      .withTimeSnippet(createTimeSnippet(50))
                      .withTicketActivity(createTicketActivity("Im not booked", 111))
                      .withId(UUID.randomUUID())
                      .build())
              .build();

      // When
      tcb.bookBusinessDayMessageSender.sendBookedIncrements(tcb.businessDay);

      // Then
      verify(tcb.bookBusinessDayMessageSender, never()).createAndSendBookBusinessDayRequest(any(), any());
      verify(tcb.bookBusinessDayMessageApiService, never()).createAndSendBookBusinessDayMessage(any(), eq(BookSenderReceiverId.NAG));
   }

   private TicketActivity createTicketActivity(String activityName, int activityCode) {
      return new TicketActivity() {
         @Override
         public String getActivityName() {
            return activityName;
         }

         @Override
         public int getActivityCode() {
            return activityCode;
         }

         @Override
         public boolean isDummy() {
            return false;
         }
      };
   }

   private static Ticket mockTicket(boolean isBookable, String ticketNr) {
      Ticket ticket = mock(Ticket.class);
      TicketAttrs attrs = mock(TicketAttrs.class);
      when(attrs.getIssueType()).thenReturn(IssueType.BUG);
      when(attrs.getProjectNr()).thenReturn((long) 1234);
      when(attrs.getNr()).thenReturn(ticketNr);
      when(ticket.getTicketAttrs()).thenReturn(attrs);
      when(ticket.getNr()).thenReturn(ticketNr);
      when(ticket.isBookable()).thenReturn(isBookable);
      return ticket;
   }

   private TimeSnippet createTimeSnippet(int timeBetweenBeginAndEnd) {
      return TimeSnippetImpl.TimeSnippetBuilder
              .of()
              .withBeginTimeStamp(timeBetweenBeginAndEnd)
              .withEndTimeStamp(System.currentTimeMillis() + timeBetweenBeginAndEnd + 10)
              .build();
   }

   private static class TestCaseBuilder {

      private BusinessDay businessDay;
      private List<BusinessDayIncrementAdd> businessDayIncrementAdds;
      private List<String> bookedBusinessDayIncrements;
      private List<String> sentBusinessDayIncrements;
      private BookBusinessDayMessageSender bookBusinessDayMessageSender;
      private BookBusinessDayMessageApiService bookBusinessDayMessageApiService;
      private boolean sendSuccess;
      private boolean isMaster;
      private BookSenderReceiverId senderId;

      private TestCaseBuilder() {
         this.businessDayIncrementAdds = new ArrayList<>();
         this.businessDay = spy(new BusinessDayImpl());
         this.bookedBusinessDayIncrements = new ArrayList<>();
         this.sentBusinessDayIncrements = new ArrayList<>();
         this.sendSuccess = true;
         this.isMaster = false;
         this.senderId = BookSenderReceiverId.NAG;
      }

      public TestCaseBuilder withBusinessDayIncrementAdd(BusinessDayIncrementAdd businessDayIncrementAdd) {
         this.businessDayIncrementAdds.add(businessDayIncrementAdd);
         return this;
      }

      private TestCaseBuilder build() {
         addBusinessIncrements();
         Supplier<BookBusinessDayMessageApiService> bookBusinessDayMessageApiServiceSupplier = () -> bookBusinessDayMessageApiService;
         this.bookBusinessDayMessageApiService = mock(BookBusinessDayMessageApiService.class);
         when(bookBusinessDayMessageApiService.createAndSendBookBusinessDayMessage(any(), any())).thenReturn(sendSuccess);
         TimeRecorder timeRecorder = mock(TimeRecorder.class);
         when(timeRecorder.getBusinessDay()).thenReturn(businessDay);
         Settings settings = mock(Settings.class);
         when(settings.getSettingsValue(eq(BookBusinessDayMessageSender.IS_MASTER_KEY))).thenReturn(isMaster);
         when(settings.getSettingsValue(eq(BookBusinessDayMessageSender.BOOK_REQUEST_SENDER_ID_KEY))).thenReturn(senderId);
         this.bookBusinessDayMessageSender = spy(new BookBusinessDayMessageSender(timeRecorder, settings, bookBusinessDayMessageApiServiceSupplier));
         return this;
      }

      private void addBusinessIncrements() {
         for (BusinessDayIncrementAdd businessDayIncrementAdd : businessDayIncrementAdds) {
            this.businessDay = this.businessDay.addBusinessIncrement(businessDayIncrementAdd);
         }
         for (BusinessDayIncrement increment : businessDay.getIncrements()) {
            if (bookedBusinessDayIncrements.contains(increment.getTicket().getNr())) {
               businessDay = businessDay.flagBusinessDayIncrementAsBooked(increment.getId());
            }
         }
         for (BusinessDayIncrement increment : businessDay.getIncrements()) {
            if (sentBusinessDayIncrements.contains(increment.getTicket().getNr())) {
               businessDay = businessDay.flagBusinessDayIncrementAsSent(increment.getId());
            }
         }
      }

      public TestCaseBuilder withBookedBusinessDayIncrements(String... bookedTicketNrs) {
         this.bookedBusinessDayIncrements = Arrays.asList(bookedTicketNrs);
         return this;
      }

      public TestCaseBuilder withSentBusinessDayIncrements(String... sentTicketNr) {
         this.sentBusinessDayIncrements = Arrays.asList(sentTicketNr);
         return this;
      }

      public TestCaseBuilder withSendFailure() {
         this.sendSuccess = false;
         return this;
      }

      public TestCaseBuilder isMaster(boolean isMaster) {
         this.isMaster = isMaster;
         return this;
      }

      public TestCaseBuilder withSenderId(BookSenderReceiverId senderId

      ) {
         this.senderId = senderId;
         return this;
      }
   }
}