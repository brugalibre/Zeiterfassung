package com.adcubum.timerecording.messaging.kafka.services;

import com.adcubum.timerecording.messaging.api.model.BookSenderReceiverId;
import com.adcubum.timerecording.messaging.api.model.BusinessDayDto;
import com.adcubum.timerecording.messaging.model.BookBusinessDayMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.KafkaException;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

@Component()
public class BookBusinessDayKafkaProducer {

   private static final Logger LOG = LoggerFactory.getLogger(BookBusinessDayKafkaProducer.class);
   private final KafkaTemplate<String, BookBusinessDayMessage> template;

   @Value("${timerecording.messaging.topic}")
   private String messagingTopic;

   public BookBusinessDayKafkaProducer(KafkaTemplate<String, BookBusinessDayMessage> template) {
      this.template = template;
   }

   /**
    * Creates and sends a {@link org.apache.kafka.common.protocol.Message} with a {@link BookBusinessDayMessage} with
    * the given {@link BusinessDayDto} as payload.
    * This {@link BookBusinessDayMessage} consists of the given {@link BusinessDayDto} and {@link BookSenderReceiverId}
    *
    * @param businessDayDto the {@link BusinessDayDto}
    * @param bookSenderId   the {@link BookSenderReceiverId}
    * @return <code>true</code> if the {@link Message} was sent or <code>false</code> if a {@link KafkaException} occurred
    */
   public boolean createAndSendBookBusinessDayMessage(BusinessDayDto businessDayDto, BookSenderReceiverId bookSenderId) {
      LOG.info("Sending message with payload '{}' from sender '{}'", businessDayDto, bookSenderId);
      Message<BookBusinessDayMessage> message = MessageBuilder
              .withPayload(BookBusinessDayMessage.of(businessDayDto, bookSenderId))
              .setHeader(KafkaHeaders.TOPIC, messagingTopic)
              .build();
      return send(message);
   }

   private boolean send(Message<BookBusinessDayMessage> message) {
      try {
         template.send(message);
         LOG.info("Message successfully send");
         return true;
      } catch (KafkaException e) {
         LOG.error("Error while sending message!", e);
         return false;
      }
   }
}
