package com.adcubum.timerecording.messaging.api;

import com.adcubum.timerecording.messaging.api.model.BookSenderReceiverId;
import com.adcubum.timerecording.messaging.api.model.BusinessDayDto;
import com.adcubum.timerecording.messaging.api.receive.BookBusinessDayMessageReceiver;
import com.adcubum.timerecording.messaging.api.receive.MessageReceiver;
import com.adcubum.timerecording.messaging.kafka.services.BookBusinessDayKafkaConsumer;
import com.adcubum.timerecording.messaging.kafka.services.BookBusinessDayKafkaProducer;
import com.adcubum.timerecording.messaging.model.BookBusinessDayMessage;
import com.adcubum.timerecording.messaging.util.ReflectionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.KafkaException;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("bookBusinessDayMessageApiService")
public class BookBusinessDayMessageApiService implements BookBusinessDayMessageReceiver {

   private static final Logger LOG = LoggerFactory.getLogger(BookBusinessDayMessageApiService.class);
   @Autowired
   private BookBusinessDayKafkaConsumer bookBusinessDayKafkaConsumer;
   @Autowired
   private BookBusinessDayKafkaProducer bookBusinessDayKafkaProducer;

   public BookBusinessDayMessageApiService(BookBusinessDayKafkaProducer bookBusinessDayKafkaProducer, BookBusinessDayKafkaConsumer bookBusinessDayKafkaConsumer) {
      this.bookBusinessDayKafkaProducer = bookBusinessDayKafkaProducer;
      this.bookBusinessDayKafkaConsumer = bookBusinessDayKafkaConsumer;
      this.bookBusinessDayKafkaConsumer.setBookBusinessDayMessageReceiver(this);
   }

   /**
    * Creates a new {@link BookBusinessDayMessage} for the given {@link BusinessDayDto} and {@link BookSenderReceiverId}
    *
    * @param businessDayDto       the {@link BusinessDayDto} to send
    * @param bookSenderReceiverId the owner of the created {@link BookBusinessDayMessage}
    * @return <code>true</code> if the {@link Message} was sent or <code>false</code> if a {@link KafkaException} occurred
    */
   public boolean createAndSendBookBusinessDayMessage(BusinessDayDto businessDayDto, BookSenderReceiverId bookSenderReceiverId) {
      return bookBusinessDayKafkaProducer.createAndSendBookBusinessDayMessage(businessDayDto, bookSenderReceiverId);
   }

   @Override
   public void onBookBusinessDayMessageReceive(BookBusinessDayMessage bookBusinessDayMessage) {
      for (BookBusinessDayMessageReceiver bookBusinessDayMessageReceiver : createBookBusinessDayMessageReceivers()) {
         LOG.info("Notify '{}' about incoming BookBusinessDayMessage", bookBusinessDayMessageReceiver);
         bookBusinessDayMessageReceiver.onBookBusinessDayMessageReceive(bookBusinessDayMessage);
      }
   }

   private static List<BookBusinessDayMessageReceiver> createBookBusinessDayMessageReceivers() {
      return ReflectionUtil.lookupClasses4AnnotationAndCreateInstances(MessageReceiver.class.getName());
   }

   /**
    * Starts the listener for the {@link BookBusinessDayMessageApiService}
    */
   public void startListener() {
      this.bookBusinessDayKafkaConsumer.start();
   }
}