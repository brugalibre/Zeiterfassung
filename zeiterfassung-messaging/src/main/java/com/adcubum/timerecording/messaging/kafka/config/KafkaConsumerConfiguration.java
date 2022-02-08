package com.adcubum.timerecording.messaging.kafka.config;

import com.adcubum.timerecording.messaging.model.BookBusinessDayMessage;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.Map;

@Configuration
public class KafkaConsumerConfiguration {

   /** ID of this {@link com.adcubum.timerecording.messaging.kafka.services.BookBusinessDayKafkaConsumer}*/
   public static final String ID = "bookBusinessDayConsumer";

   @Value("${timerecording.messaging.groupId}")
   private String groupId;

   @Value("${timerecording.messaging.consumer-enabled}")
   private String isConsumerEnabledProperty;

   @Value("${timerecording.messaging.brokers}")
   private String brokers;

   @Value("${timerecording.auth.password}")
   private String password;

   @Value("${timerecording.auth.username}")
   private String username;

   @Bean
   public ConsumerFactory<String, BookBusinessDayMessage> consumerFactory() {
      return new DefaultKafkaConsumerFactory<>(
              consumerConfigurations(),
              new StringDeserializer(),
              new JsonDeserializer<>(BookBusinessDayMessage.class));
   }

   @Bean
   public Map<String, Object> consumerConfigurations() {
      return new KafkaConfigurationHelper().fillUpDefaultConfigurations(brokers, groupId, username, password);
   }

   @Bean
   ConcurrentKafkaListenerContainerFactory<String, BookBusinessDayMessage> kafkaListenerContainerFactory() {
      ConcurrentKafkaListenerContainerFactory<String, BookBusinessDayMessage> factory = new ConcurrentKafkaListenerContainerFactory<>();
      factory.setConsumerFactory(consumerFactory());
      return factory;
   }

   /**
    * @return <code>true</code> if the KafkaConsumer is enabled or <code>false</code> if not
    */
   public boolean isConsumerEnabled(){
      return Boolean.valueOf(isConsumerEnabledProperty);
   }
}