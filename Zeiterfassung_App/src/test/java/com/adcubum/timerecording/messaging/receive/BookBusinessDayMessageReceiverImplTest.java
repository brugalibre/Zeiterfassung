package com.adcubum.timerecording.messaging.receive;

import com.adcubum.timerecording.app.TimeRecorder;
import com.adcubum.timerecording.app.TimeRecorderBookResultImpl;
import com.adcubum.timerecording.messaging.receive.mapping.TicketMapper;
import com.adcubum.timerecording.messaging.receive.mapping.impl.TicketMapping;
import com.adcubum.timerecording.messaging.send.mapping.BusinessDayIncrementsToBusinessDayDtoMapper;
import com.adcubum.timerecording.core.work.businessday.BusinessDay;
import com.adcubum.timerecording.core.work.businessday.BusinessDayImpl;
import com.adcubum.timerecording.core.work.businessday.BusinessDayIncrement;
import com.adcubum.timerecording.core.work.businessday.TimeSnippetImpl;
import com.adcubum.timerecording.core.work.businessday.update.callback.impl.BusinessDayIncrementAdd;
import com.adcubum.timerecording.core.work.businessday.update.callback.impl.BusinessDayIncrementAdd.BusinessDayIncrementAddBuilder;
import com.adcubum.timerecording.jira.data.ticket.Ticket;
import com.adcubum.timerecording.jira.data.ticket.TicketActivity;
import com.adcubum.timerecording.messaging.api.model.BookSenderReceiverId;
import com.adcubum.timerecording.messaging.model.BookBusinessDayMessage;
import com.adcubum.timerecording.settings.Settings;
import com.adcubum.timerecording.settings.key.ValueKeyFactory;
import com.adcubum.timerecording.test.TestTicket;
import com.adcubum.timerecording.test.TestTicketActivity;
import com.adcubum.timerecording.ticketbacklog.TicketBacklog;
import org.junit.jupiter.api.Test;

import java.util.*;

import static com.adcubum.timerecording.messaging.receive.BookBusinessDayMessageReceiverImpl.BOOK_RECEIVER_ID_KEY;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class BookBusinessDayMessageReceiverImplTest {

   @Test
   void testReceiveBookBusinessDayMessage_HappyCaseFromNAG() {
      // Given
      BusinessDayIncrementAdd firstBusinessDayIncrementAdd = new BusinessDayIncrementAddBuilder()
              .withTicket(mockTicket("ABC-123"))
              .withDescription("Test1")
              .withTimeSnippet(TimeSnippetImpl.TimeSnippetBuilder.of()
                      .withBeginTimeStamp(1000)
                      .withEndTimeStamp(20000)
                      .build())
              .withTicketActivity(createTicketActivity("Test", 113))
              .build();
      BusinessDayIncrementAdd secondBusinessDayIncrementAdd = new BusinessDayIncrementAddBuilder()
              .withTicket(mockTicket("XYZ-321"))
              .withDescription("Test2")
              .withTimeSnippet(TimeSnippetImpl.TimeSnippetBuilder.of()
                      .withBeginTimeStamp(1000)
                      .withEndTimeStamp(20000)
                      .build())
              .withTicketActivity(createTicketActivity("Test2", 321))
              .build();
      TestCaseBuilder tcb = new TestCaseBuilder()
              .withBookSenderId(BookSenderReceiverId.NAG)
              .withBookReceiverId(BookSenderReceiverId.NAG)
              .addBusinessDayIncrement(firstBusinessDayIncrementAdd)
              .addBusinessDayIncrement(secondBusinessDayIncrementAdd)
              .build();

      // When
      tcb.bookBusinessDayMessageReceiver.onBookBusinessDayMessageReceive(tcb.bookBusinessDayMessage);

      // Then
      verify(tcb.timeRecorder).addBusinessIncrement(eq(firstBusinessDayIncrementAdd));
      verify(tcb.timeRecorder).addBusinessIncrement(eq(secondBusinessDayIncrementAdd));
      verify(tcb.timeRecorder, never()).book();
      verify(tcb.timeRecorder).flagBusinessDayAsBooked();
   }

   @Test
   void testReceiveBookBusinessDayMessage_HappyCaseFromPostFinance() {
      // Given
      String pfTicketNr = "PostFinance PK&S";
      String defaultPFActivityName = "Projektarbeit in Basel";
      BusinessDayIncrementAdd businessDayIncrementAdd1FromPf = new BusinessDayIncrementAddBuilder()
              .withTicket(mockTicket("PKS-123"))
              .withDescription("Test1")
              .withTimeSnippet(TimeSnippetImpl.TimeSnippetBuilder.of()
                      .withBeginTimeStamp(1000)
                      .withEndTimeStamp(20000)
                      .build())
              .withTicketActivity(createTicketActivity(defaultPFActivityName, 113))
              .build();

      // Verify that the unknown activity-name was mapped in a default post-finance one, which is known to proles
      // the 2nd activity code was already well known and is therefore mapped 1:1
      BusinessDayIncrementAdd businessDayIncrementAdd1MappedToNag = new BusinessDayIncrementAddBuilder()
              .withTicket(mockTicket(pfTicketNr))
              .withDescription("Test1")
              .withTimeSnippet(TimeSnippetImpl.TimeSnippetBuilder.of()
                      .withBeginTimeStamp(1000)
                      .withEndTimeStamp(20000)
                      .build())
              .withTicketActivity(createTicketActivity(defaultPFActivityName, 113))
              .build();
      TestCaseBuilder tcb = new TestCaseBuilder()
              .withHasAllBooked()
              .withBookSenderId(BookSenderReceiverId.POST_FINANCE)
              .withBookReceiverId(BookSenderReceiverId.NAG)
              .addBusinessDayIncrement(businessDayIncrementAdd1FromPf)
              .build();

      // When
      tcb.bookBusinessDayMessageReceiver.onBookBusinessDayMessageReceive(tcb.bookBusinessDayMessage);

      // Then
      verify(tcb.timeRecorder).addBusinessIncrement(eq(businessDayIncrementAdd1MappedToNag));
      verify(tcb.timeRecorder).book();
      verify(tcb.timeRecorder).clear();
   }

   @Test
   void testReceiveBookBusinessDayMessage_HappyCaseFromPostFinancePartialSuccess() {
      // Given
      String pfTicketNr = "PostFinance PK&S";
      String defaultPFActivityName = "Projektarbeit in Basel";
      BusinessDayIncrementAdd businessDayIncrementAdd1FromPf = new BusinessDayIncrementAddBuilder()
              .withTicket(mockTicket("PKS-123"))
              .withDescription("Test1")
              .withTimeSnippet(TimeSnippetImpl.TimeSnippetBuilder.of()
                      .withBeginTimeStamp(1000)
                      .withEndTimeStamp(20000)
                      .build())
              .withTicketActivity(createTicketActivity(defaultPFActivityName, 113))
              .build();

      // Verify that the unknown activity-name was mapped in a default post-finance one, which is known to proles
      // the 2nd activity code was already well known and is therefore mapped 1:1
      BusinessDayIncrementAdd businessDayIncrementAdd1MappedToNag = new BusinessDayIncrementAddBuilder()
              .withTicket(mockTicket(pfTicketNr))
              .withDescription("Test1")
              .withTimeSnippet(TimeSnippetImpl.TimeSnippetBuilder.of()
                      .withBeginTimeStamp(1000)
                      .withEndTimeStamp(20000)
                      .build())
              .withTicketActivity(createTicketActivity(defaultPFActivityName, 113))
              .build();
      TestCaseBuilder tcb = new TestCaseBuilder()
              .withBookSenderId(BookSenderReceiverId.POST_FINANCE)
              .withBookReceiverId(BookSenderReceiverId.NAG)
              .addBusinessDayIncrement(businessDayIncrementAdd1FromPf)
              .build();

      // When
      tcb.bookBusinessDayMessageReceiver.onBookBusinessDayMessageReceive(tcb.bookBusinessDayMessage);

      // Then
      verify(tcb.timeRecorder).addBusinessIncrement(eq(businessDayIncrementAdd1MappedToNag));
      verify(tcb.timeRecorder).book();
      verify(tcb.timeRecorder, never()).clear();
   }

   @Test
   void testReceiveBookBusinessDayMessage_NotBooking() {
      // Given
      BusinessDayIncrementAdd firstBusinessDayIncrementAdd = new BusinessDayIncrementAddBuilder()
              .withTicket(mockTicket("ABC-123"))
              .withDescription("Test1")
              .withTimeSnippet(TimeSnippetImpl.TimeSnippetBuilder.of()
                      .withBeginTimeStamp(1000)
                      .withEndTimeStamp(20000)
                      .build())
              .withTicketActivity(createTicketActivity("Test", 113))
              .build();
      BusinessDayIncrementAdd secondBusinessDayIncrementAdd = new BusinessDayIncrementAddBuilder()
              .withTicket(mockTicket("XYZ-321"))
              .withDescription("Test2")
              .withTimeSnippet(TimeSnippetImpl.TimeSnippetBuilder.of()
                      .withBeginTimeStamp(1000)
                      .withEndTimeStamp(20000)
                      .build())
              .withTicketActivity(createTicketActivity("Test2", 321))
              .build();
      TestCaseBuilder tcb = new TestCaseBuilder()
              .withBookSenderId(BookSenderReceiverId.NAG)
              .withBookReceiverId(BookSenderReceiverId.NAG)
              .addBusinessDayIncrement(firstBusinessDayIncrementAdd)
              .addBusinessDayIncrement(secondBusinessDayIncrementAdd)
              .withTimeRecordingIsRecording(true)
              .build();

      // When
      tcb.bookBusinessDayMessageReceiver.onBookBusinessDayMessageReceive(tcb.bookBusinessDayMessage);

      // Then
      verify(tcb.timeRecorder).addBusinessIncrement(eq(firstBusinessDayIncrementAdd));
      verify(tcb.timeRecorder).addBusinessIncrement(eq(secondBusinessDayIncrementAdd));
      verify(tcb.timeRecorder, never()).book();
   }

   @Test
   void testReceiveBookBusinessDayMessage_EmptyPayload() {
      // Given
      TestCaseBuilder tcb = new TestCaseBuilder()
              .withBookSenderId(BookSenderReceiverId.NAG)
              .withBookReceiverId(BookSenderReceiverId.NAG)
              .build();

      // When
      tcb.bookBusinessDayMessageReceiver.onBookBusinessDayMessageReceive(tcb.bookBusinessDayMessage);

      // Then
      verify(tcb.timeRecorder, never()).addBusinessIncrement(any());
      verify(tcb.timeRecorder, never()).book();
   }

   private static class TestCaseBuilder {

      private final TimeRecorder timeRecorder;
      private final TicketBacklog ticketBacklog;
      private final List<BusinessDayIncrementAdd> businessDayIncrementAdds;
      private BookBusinessDayMessageReceiverImpl bookBusinessDayMessageReceiver;
      private BookBusinessDayMessage bookBusinessDayMessage;
      private BookSenderReceiverId bookSenderId;
      private BookSenderReceiverId bookReceiverId;
      private boolean hasAllBooked;

      private TestCaseBuilder() {
         this.timeRecorder = mock(TimeRecorder.class);
         this.ticketBacklog = mock(TicketBacklog.class);
         this.businessDayIncrementAdds = new ArrayList<>();
         this.hasAllBooked = false;
      }

      public TestCaseBuilder withHasAllBooked() {
         this.hasAllBooked = true;
         return this;
      }

      private TestCaseBuilder addBusinessDayIncrement(BusinessDayIncrementAdd businessDayIncrementAdd) {
         this.businessDayIncrementAdds.add(businessDayIncrementAdd);
         return this;
      }

      private TestCaseBuilder withBookSenderId(BookSenderReceiverId bookSenderId) {
         this.bookSenderId = bookSenderId;
         return this;
      }

      private TestCaseBuilder withBookReceiverId(BookSenderReceiverId bookReceiverId) {
         this.bookReceiverId = bookReceiverId;
         return this;
      }

      private TestCaseBuilder build() {
         BusinessDay businessDay = createBusinessDay();
         this.bookBusinessDayMessage = createBookBusinessDayMessage(businessDay);
         Settings settings = mock(Settings.class);
         when(settings.getSettingsValue(eq(BOOK_RECEIVER_ID_KEY))).thenReturn(bookReceiverId);
         this.bookBusinessDayMessageReceiver = new BookBusinessDayMessageReceiverImpl(timeRecorder, ticketBacklog, settings);
         mockTicketBacklog(businessDay);
         if (hasAllBooked){
            when(timeRecorder.book()).thenReturn(TimeRecorderBookResultImpl.success());
         }else{
            when(timeRecorder.book()).thenReturn(TimeRecorderBookResultImpl.nonBooked());
         }
         return this;
      }

      private BookBusinessDayMessage createBookBusinessDayMessage(BusinessDay businessDay) {
         BookBusinessDayMessage bookBusinessDayMessage = new BookBusinessDayMessage();
         bookBusinessDayMessage.setBusinessDayDto(BusinessDayIncrementsToBusinessDayDtoMapper.mapToBusinessDayDto(businessDay.getIncrements()));
         bookBusinessDayMessage.setBookSenderId(bookSenderId);
         return bookBusinessDayMessage;
      }

      private void mockTicketBacklog(BusinessDay businessDay) {
         TicketMapper ticketMapper = TicketMapping.getMapper(bookSenderId, bookReceiverId);
         for (BusinessDayIncrement increment : businessDay.getIncrements()) {
            when(ticketBacklog.getTicket4Nr(eq(increment.getTicket().getNr()))).thenReturn(increment.getTicket());
            when(ticketBacklog.getTicket4Nr(eq(ticketMapper.mapTicketNr(increment.getTicket().getNr())))).thenReturn(mockTicket(ticketMapper.mapTicketNr(increment.getTicket().getNr())));
            when(ticketBacklog.getTicketActivity4ServiceCode(eq(increment.getTicketActivity().getActivityCode()))).thenReturn(increment.getTicketActivity());
         }
      }

      private BusinessDay createBusinessDay() {
         BusinessDay businessDay = new BusinessDayImpl();
         for (BusinessDayIncrementAdd businessDayIncrementAdd : this.businessDayIncrementAdds) {
            businessDay = businessDay.addBusinessIncrement(businessDayIncrementAdd);
         }
         return businessDay.flagBusinessDayAsBooked();
      }

      public TestCaseBuilder withTimeRecordingIsRecording(boolean isRecording) {
         when(timeRecorder.isRecording()).thenReturn(isRecording);
         return this;
      }

   }

   private TicketActivity createTicketActivity(String activityName, int activityCode) {
      return new TestTicketActivity(activityName, activityCode);
   }

   private static Ticket mockTicket(String ticketNr) {
      return new TestTicket(ticketNr);
   }

}