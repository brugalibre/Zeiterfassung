package com.adcubum.timerecording.messaging.kafka.config;

import com.adcubum.timerecording.messaging.model.BookBusinessDayMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.util.Map;

@Configuration
public class KafkaProducerConfiguration {

   @Value("${timerecording.messaging.groupId}")
   private String groupId;

   @Value("${timerecording.messaging.brokers}")
   private String brokers;

   @Value("${timerecording.auth.password}")
   private String password;

   @Value("${timerecording.auth.username}")
   private String username;

   @Bean
   public ProducerFactory<String, BookBusinessDayMessage> producerFactory() {
      return new DefaultKafkaProducerFactory<>(producerConfigurations());
   }

   @Bean
   public Map<String, Object> producerConfigurations() {
      return new KafkaConfigurationHelper().fillUpDefaultConfigurations(brokers, groupId, username, password);
   }

   @Bean
   public KafkaTemplate<String, BookBusinessDayMessage> kafkaTemplate() {
      return new KafkaTemplate<>(producerFactory());
   }

}