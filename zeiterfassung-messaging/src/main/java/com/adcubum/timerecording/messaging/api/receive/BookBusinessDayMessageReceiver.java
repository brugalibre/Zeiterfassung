package com.adcubum.timerecording.messaging.api.receive;

import com.adcubum.timerecording.messaging.model.BookBusinessDayMessage;
import com.adcubum.timerecording.messaging.kafka.services.BookBusinessDayKafkaConsumer;

/**
 * For external consumers of this api, the {@link BookBusinessDayMessageReceiver}
 * is used as a hook when ever the {@link BookBusinessDayKafkaConsumer} receives
 * a new {@link BookBusinessDayMessage}
 */
public interface BookBusinessDayMessageReceiver {

   /**
    * Is called as soon as a new {@link BookBusinessDayMessage} is received
    *
    * @param bookBusinessDayMessage the received {@link BookBusinessDayMessage}
    */
   void onBookBusinessDayMessageReceive(BookBusinessDayMessage bookBusinessDayMessage);
}
