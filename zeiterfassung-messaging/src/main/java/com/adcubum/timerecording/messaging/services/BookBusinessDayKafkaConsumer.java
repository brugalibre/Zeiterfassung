package com.adcubum.timerecording.messaging.services;

import com.adcubum.timerecording.messaging.api.receive.BookBusinessDayMessageReceiver;
import com.adcubum.timerecording.messaging.model.BookBusinessDayMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component()
public class BookBusinessDayKafkaConsumer {

   private static final Logger LOG = LoggerFactory.getLogger(BookBusinessDayKafkaConsumer.class);
   private Optional<BookBusinessDayMessageReceiver> optionalBookBusinessDayMessageReceiver;

   @KafkaListener(topics = "${timerecording.messaging.topic}", autoStartup = "${timerecording.messaging.consumer-enabled:false}"
           , groupId = "${timerecording.messaging.groupId}")
   public void listen(BookBusinessDayMessage bookBusinessDayMessage) {
      LOG.info("Received message '{}' from sender '{}'", bookBusinessDayMessage.getBusinessDayDto(), bookBusinessDayMessage.getBookSenderId());
      optionalBookBusinessDayMessageReceiver.ifPresent(bookBusinessDayMessageReceiver -> bookBusinessDayMessageReceiver.onBookBusinessDayMessageReceive(bookBusinessDayMessage));
   }

   public void setBookBusinessDayMessageReceiver(BookBusinessDayMessageReceiver bookBusinessDayMessageReceiver) {
      this.optionalBookBusinessDayMessageReceiver = Optional.ofNullable(bookBusinessDayMessageReceiver);
   }
}