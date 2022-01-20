package com.adcubum.timerecording.messaging.receive;

import com.adcubum.timerecording.app.TimeRecorder;
import com.adcubum.timerecording.app.book.TimeRecorderBookResult;
import com.adcubum.timerecording.core.work.businessday.TimeSnippetImpl.TimeSnippetBuilder;
import com.adcubum.timerecording.core.work.businessday.update.callback.impl.BusinessDayIncrementAdd.BusinessDayIncrementAddBuilder;
import com.adcubum.timerecording.messaging.api.model.BookSenderReceiverId;
import com.adcubum.timerecording.messaging.api.model.BusinessDayDto;
import com.adcubum.timerecording.messaging.api.model.BusinessDayIncrementDto;
import com.adcubum.timerecording.messaging.api.receive.BookBusinessDayMessageReceiver;
import com.adcubum.timerecording.messaging.api.receive.MessageReceiver;
import com.adcubum.timerecording.messaging.receive.mapping.TicketMapper;
import com.adcubum.timerecording.messaging.receive.mapping.impl.TicketMapping;
import com.adcubum.timerecording.messaging.model.BookBusinessDayMessage;
import com.adcubum.timerecording.settings.Settings;
import com.adcubum.timerecording.settings.key.ValueKey;
import com.adcubum.timerecording.settings.key.ValueKeyFactory;
import com.adcubum.timerecording.ticketbacklog.TicketBacklog;
import com.adcubum.timerecording.ticketbacklog.TicketBacklogSPI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@MessageReceiver
public class BookBusinessDayMessageReceiverImpl implements BookBusinessDayMessageReceiver {

   private static final Logger LOG = LoggerFactory.getLogger(BookBusinessDayMessageReceiverImpl.class);
   static final ValueKey<BookSenderReceiverId> BOOK_RECEIVER_ID_KEY = ValueKeyFactory.createNew("BookBDaySenderReceiverId", BookSenderReceiverId.class);
   private final TimeRecorder timeRecorder;
   private final TicketBacklog ticketBacklog;
   private final Settings settings;

   public BookBusinessDayMessageReceiverImpl() {
      this(TimeRecorder.INSTANCE, TicketBacklogSPI.getTicketBacklog(), Settings.INSTANCE);
   }

   /**
    * Constructor for testing purpose
    *
    * @param timeRecorder  the {@link TimeRecorder}
    * @param ticketBacklog the {@link TicketBacklog}
    * @param settings      the {@link Settings}
    */
   BookBusinessDayMessageReceiverImpl(TimeRecorder timeRecorder, TicketBacklog ticketBacklog, Settings settings) {
      this.timeRecorder = timeRecorder;
      this.ticketBacklog = ticketBacklog;
      this.settings = settings;
   }

   @Override
   public void onBookBusinessDayMessageReceive(BookBusinessDayMessage bookBusinessDayMessage) {
      BusinessDayDto businessDayDto = bookBusinessDayMessage.getBusinessDayDto();
      BookSenderReceiverId bookSenderId = bookBusinessDayMessage.getBookSenderId();
      TicketMapper ticketMapper = getTicketMapper(bookSenderId);
      for (BusinessDayIncrementDto businessDayIncrementDto : businessDayDto.getBusinessDayIncrementDtos()) {
         addBusinessIncrement(businessDayIncrementDto, ticketMapper);
      }
      LOG.info("Received in total '{}' BusinessDayIncrements.", bookBusinessDayMessage.getBusinessDayDto().getBusinessDayIncrementDtos().size());
      if (hasToBook(businessDayDto)) {
         if (isNotSameReceiver(bookSenderId)) {
            handleBooking();
         } else {
            LOG.info("Flag the current business-day as recorded");
            timeRecorder.flagBusinessDayAsBooked();
         }
      }
   }

   private boolean isNotSameReceiver(BookSenderReceiverId bookSenderId) {
      return bookSenderId != settings.getSettingsValue(BOOK_RECEIVER_ID_KEY);
   }

   private void handleBooking() {
      LOG.info("Call timeRecorder book...");
      TimeRecorderBookResult timeRecorderBookResult = timeRecorder.book();
      if (timeRecorderBookResult.hasAllBooked()) {
         LOG.info("Booking was a complete success - delete all records");
         timeRecorder.clear();
      }
   }

   private void addBusinessIncrement(BusinessDayIncrementDto businessDayIncrementDto, TicketMapper ticketMapper) {
      String ticketNr = ticketMapper.mapTicketNr(businessDayIncrementDto.getTicketNr());
      String serviceCode = ticketMapper.mapTicketActivityCode(businessDayIncrementDto.getTicketActivityCode());
      timeRecorder.addBusinessIncrement(new BusinessDayIncrementAddBuilder()
              .withDescription(businessDayIncrementDto.getDescription())
              .withTicket(ticketBacklog.getTicket4Nr(ticketNr))
              .withTicketActivity(ticketBacklog.getTicketActivity4ServiceCode(Integer.parseInt(serviceCode)))
              .withTimeSnippet(TimeSnippetBuilder.of()
                      .withBeginTimeStamp(businessDayIncrementDto.getTimeSnippetDto()
                              .getBeginTimeStamp())
                      .withEndTimeStamp(businessDayIncrementDto.getTimeSnippetDto()
                              .getEndTimeStamp())
                      .build())
              .build());
   }

   /**
    * We do book an incoming Business-Day if
    * -  We are not currently recording (since this means that there is a user interaction, so the user should do the
    * booking as soon as he is done recording
    * -  We have received an empty payload with no increments. So avoid a no-op and don't call book()
    * -  If the sender has the same as the receiver (e.g. both nag) then we must not book again. Since the sender
    * has booked them already and we only want to add them and flag them as booked. In order to complete the history
    *
    * @param businessDayDto the received BusinessDayDto
    * @return <ocde>true</ocde> if the {@link TimeRecorder#book()} is called or <code>false</code> if not
    */
   private boolean hasToBook(BusinessDayDto businessDayDto) {
      return !timeRecorder.isRecording()
              && businessDayDto.hasBusinessDayIncrementDtos();
   }

   private TicketMapper getTicketMapper(BookSenderReceiverId bookSenderId) {
      BookSenderReceiverId bookReceiverId = settings.getSettingsValue(BOOK_RECEIVER_ID_KEY);
      return TicketMapping.getMapper(bookSenderId, bookReceiverId);
   }
}
