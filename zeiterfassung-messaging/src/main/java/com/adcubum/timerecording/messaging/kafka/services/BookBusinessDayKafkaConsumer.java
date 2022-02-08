package com.adcubum.timerecording.messaging.kafka.services;

import com.adcubum.timerecording.messaging.api.receive.BookBusinessDayMessageReceiver;
import com.adcubum.timerecording.messaging.kafka.config.KafkaConsumerConfiguration;
import com.adcubum.timerecording.messaging.model.BookBusinessDayMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.stereotype.Component;

import static com.adcubum.timerecording.messaging.kafka.config.KafkaConsumerConfiguration.ID;
import static java.util.Objects.requireNonNull;

@Component()
public class BookBusinessDayKafkaConsumer {



   private static final Logger LOG = LoggerFactory.getLogger(BookBusinessDayKafkaConsumer.class);
   @Autowired
   private final KafkaListenerEndpointRegistry kafkaListenerEndpointRegistry;
   private final KafkaConsumerConfiguration kafkaConsumerConfiguration;
   private BookBusinessDayMessageReceiver bookBusinessDayMessageReceiver;

   @Autowired
   public BookBusinessDayKafkaConsumer(KafkaListenerEndpointRegistry kafkaListenerEndpointRegistry, KafkaConsumerConfiguration kafkaConsumerConfiguration) {
      this.kafkaListenerEndpointRegistry = kafkaListenerEndpointRegistry;
      this.kafkaConsumerConfiguration = kafkaConsumerConfiguration;
   }

   @KafkaListener(id = ID, topics = "${timerecording.messaging.topic}", autoStartup = "false"
           , groupId = "${timerecording.messaging.groupId}")
   public void listen(BookBusinessDayMessage bookBusinessDayMessage) {
      requireNonNull(bookBusinessDayMessageReceiver, "No 'bookBusinessDayMessageReceiver' available. Call setBookBusinessDayMessageReceiver first!");
      LOG.info("Received message '{}' from sender '{}'", bookBusinessDayMessage.getBusinessDayDto(), bookBusinessDayMessage.getBookSenderId());
      bookBusinessDayMessageReceiver.onBookBusinessDayMessageReceive(bookBusinessDayMessage);
   }

   public void setBookBusinessDayMessageReceiver(BookBusinessDayMessageReceiver bookBusinessDayMessageReceiver) {
      this.bookBusinessDayMessageReceiver = requireNonNull(bookBusinessDayMessageReceiver);
   }

   /**
    * Starts the actual KafkaListener. So after this call, kafka messages can be received
    */
   public void start() {
      if (kafkaConsumerConfiguration.isConsumerEnabled()) {
         kafkaListenerEndpointRegistry.getListenerContainer(ID).start();
      }
   }
}