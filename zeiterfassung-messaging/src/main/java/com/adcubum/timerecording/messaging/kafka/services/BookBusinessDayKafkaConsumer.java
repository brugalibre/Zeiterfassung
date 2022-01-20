package com.adcubum.timerecording.messaging.kafka.services;

import com.adcubum.timerecording.messaging.api.receive.BookBusinessDayMessageReceiver;
import com.adcubum.timerecording.messaging.model.BookBusinessDayMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import static java.util.Objects.requireNonNull;

@Component()
public class BookBusinessDayKafkaConsumer {

   private static final Logger LOG = LoggerFactory.getLogger(BookBusinessDayKafkaConsumer.class);
   private BookBusinessDayMessageReceiver bookBusinessDayMessageReceiver;

   @KafkaListener(topics = "${timerecording.messaging.topic}", autoStartup = "${timerecording.messaging.consumer-enabled:false}"
           , groupId = "${timerecording.messaging.groupId}")
   public void listen(BookBusinessDayMessage bookBusinessDayMessage) {
      requireNonNull(bookBusinessDayMessageReceiver, "No 'bookBusinessDayMessageReceiver' available. Call setBookBusinessDayMessageReceiver first!");
      LOG.info("Received message '{}' from sender '{}'", bookBusinessDayMessage.getBusinessDayDto(), bookBusinessDayMessage.getBookSenderId());
      bookBusinessDayMessageReceiver.onBookBusinessDayMessageReceive(bookBusinessDayMessage);
   }

   public void setBookBusinessDayMessageReceiver(BookBusinessDayMessageReceiver bookBusinessDayMessageReceiver) {
      this.bookBusinessDayMessageReceiver = requireNonNull(bookBusinessDayMessageReceiver);
   }
}