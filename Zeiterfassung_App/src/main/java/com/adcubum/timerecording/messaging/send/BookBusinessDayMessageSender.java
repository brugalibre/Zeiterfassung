package com.adcubum.timerecording.messaging.send;

import com.adcubum.timerecording.app.TimeRecorder;
import com.adcubum.timerecording.core.work.businessday.BusinessDay;
import com.adcubum.timerecording.core.work.businessday.BusinessDayIncrement;
import com.adcubum.timerecording.messaging.api.BookBusinessDayMessageApiService;
import com.adcubum.timerecording.messaging.api.BookBusinessDayMessageApiServiceHolder;
import com.adcubum.timerecording.messaging.api.model.BookSenderReceiverId;
import com.adcubum.timerecording.messaging.api.model.BusinessDayDto;
import com.adcubum.timerecording.messaging.send.mapping.BusinessDayIncrementsToBusinessDayDtoMapper;
import com.adcubum.timerecording.settings.Settings;
import com.adcubum.timerecording.settings.key.ValueKey;
import com.adcubum.timerecording.settings.key.ValueKeyFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static java.util.Objects.nonNull;

/**
 * The {@link BookBusinessDayMessageSender} decides which {@link BusinessDayIncrement} has to be
 * sent and if there has to be sent anything at all.
 * After the {@link BusinessDayIncrement}s to sent are determined, the {@link BookBusinessDayMessageApiService} is
 * called in order to actually send them
 *
 * @author dstalder
 */
public class BookBusinessDayMessageSender {

   private static final Logger LOG = LoggerFactory.getLogger(BookBusinessDayMessageSender.class);
   static final ValueKey<Boolean> IS_MASTER_KEY = ValueKeyFactory.createNew("IsMaster", Boolean.class, false);
   static final ValueKey<BookSenderReceiverId> BOOK_REQUEST_SENDER_ID_KEY = ValueKeyFactory.createNew("BookBDaySenderReceiverId", BookSenderReceiverId.class);

   private final Supplier<BookBusinessDayMessageApiService> bookBusinessDayMessageApiServiceSupplier;
   private final Settings settings;
   private final TimeRecorder timeRecorder;

   /**
    * Creates a new default BookBusinessDayMessageSender
    */
   public BookBusinessDayMessageSender() {
      this(TimeRecorder.INSTANCE, Settings.INSTANCE, BookBusinessDayMessageApiServiceHolder::getBookBusinessDayMessageApiService);
   }

   public BookBusinessDayMessageSender(TimeRecorder timeRecorder, Settings settings, Supplier<BookBusinessDayMessageApiService> bookBusinessDayMessageApiServiceSupplier) {
      this.settings = settings;
      this.timeRecorder = timeRecorder;
      this.bookBusinessDayMessageApiServiceSupplier = bookBusinessDayMessageApiServiceSupplier;
   }

   /**
    * Sends the {@link BusinessDayIncrement}s, which are booked and not sent yet, via the
    * {@link BookBusinessDayMessageApiService} as a message to any receiver listening.
    *
    * @param businessDay the booked {@link BusinessDay}
    * @return a copy of the given {@link BusinessDay} which may contains sent {@link BusinessDayIncrement}s
    */
   public BusinessDay sendBookedIncrements(BusinessDay businessDay) {
      BusinessDay sentBusinessDay = businessDay;
      BookSenderReceiverId bookRequestSenderId = settings.getSettingsValue(BOOK_REQUEST_SENDER_ID_KEY);
      List<BusinessDayIncrement> businessDayIncrements = evalBookedBusinessDayIncrements(businessDay);
      if (isSendBookRequestNecessary(bookRequestSenderId, businessDayIncrements)) {
         LOG.info("Map {} booked BusinessDayIncrements and send via broker", businessDayIncrements.size());
         List<BusinessDayIncrement> sentBusinessDayIncrements = createAndSendBookBusinessDayRequest(businessDayIncrements, bookRequestSenderId);
         sentBusinessDay = flagBDayIncrementsAsSent(businessDay, sentBusinessDayIncrements);
      }
      return sentBusinessDay;
   }

   /**
    * Sends the given {@link BusinessDayIncrement}s via the {@link BookBusinessDayMessageApiService} as a message
    * to any receiver listening.
    *
    * @param bookedBusinessDayIncrements the {@link BusinessDayIncrement}
    * @param bookSenderReceiverId        the {@link BookSenderReceiverId} in order to identify the request owner
    * @return a {@link List} with {@link BusinessDayIncrement} which are flag as 'sent' if the sending was successful or not
    */
   public List<BusinessDayIncrement> createAndSendBookBusinessDayRequest(List<BusinessDayIncrement> bookedBusinessDayIncrements, BookSenderReceiverId bookSenderReceiverId) {
      BusinessDayDto businessDayDto = BusinessDayIncrementsToBusinessDayDtoMapper.mapToBusinessDayDto(bookedBusinessDayIncrements);
      boolean wasSent = bookBusinessDayMessageApiServiceSupplier.get().createAndSendBookBusinessDayMessage(businessDayDto, bookSenderReceiverId);
      if (wasSent) {
         return bookedBusinessDayIncrements.stream()
                 .map(BusinessDayIncrement::flagAsSent)
                 .collect(Collectors.toList());
      }
      return Collections.emptyList();
   }

   private static BusinessDay flagBDayIncrementsAsSent(BusinessDay bookedBusinessDay, List<BusinessDayIncrement> sentBusinessDayIncrements) {
      for (BusinessDayIncrement sentBusinessDayIncrement : sentBusinessDayIncrements) {
         bookedBusinessDay = bookedBusinessDay.flagBusinessDayIncrementAsSent(sentBusinessDayIncrement.getId());
      }
      return bookedBusinessDay;
   }

   private static List<BusinessDayIncrement> evalBookedBusinessDayIncrements(BusinessDay bookedBusinessDay) {
      return bookedBusinessDay.getIncrements()
              .stream()
              .filter(BusinessDayIncrement::isBooked)
              .filter(businessDayIncrement -> !businessDayIncrement.isSent())
              .collect(Collectors.toList());
   }

   private boolean isSendBookRequestNecessary(BookSenderReceiverId bookRequestSenderId, List<BusinessDayIncrement> businessDayIncrements) {
      return nonNull(bookRequestSenderId)
              && hasUnsentAndBookedBusinessDayIncrements(businessDayIncrements)
              && isNotMaster();
   }

   private boolean isNotMaster() {
      return !settings.getSettingsValue(IS_MASTER_KEY);
   }

   /**
    * Evaluates if the configuration is properly done and if there are any {@link BusinessDayIncrement} to send
    *
    * @return <code>true</code> if there are any unsent messages which can be sent to a master {@link TimeRecorder}
    * or <code>false</code> if not
    */
   public boolean isSendBookedBusinessDayIncrementsEnabled() {
      BookSenderReceiverId bookRequestSenderId = settings.getSettingsValue(BOOK_REQUEST_SENDER_ID_KEY);
      return nonNull(bookRequestSenderId)
              && hasUnsentAndBookedBusinessDayIncrements()
              && isNotMaster();
   }

   private boolean hasUnsentAndBookedBusinessDayIncrements() {
      return hasUnsentAndBookedBusinessDayIncrements(timeRecorder.getBusinessDay()
              .getIncrements());
   }

   private static boolean hasUnsentAndBookedBusinessDayIncrements(List<BusinessDayIncrement> businessDayIncrements) {
      return businessDayIncrements.stream()
              .filter(BusinessDayIncrement::isBooked)
              .anyMatch(businessDayIncrement -> !businessDayIncrement.isSent());
   }
}
