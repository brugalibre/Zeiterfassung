package com.adcubum.timerecording.messaging.config;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

public class KafkaConfigurationHelper {

   /**
    * Fills up some basics to the consumer/producer configuration
    *
    * @param brokers  the brokers
    * @param groupId  the group-Id
    * @param username the username
    * @param password the password
    * @return the default configuration
    */
   public Map<String, Object> fillupDefaultConfigurations(String brokers, String groupId,
                                                          String username, String password) {
      Map<String, Object> configurations = new HashMap<>();
      String jaasTemplate = "org.apache.kafka.common.security.scram.ScramLoginModule required username=\"%s\" password=\"%s\";";
      String jaasCfg = String.format(jaasTemplate, username, password);
      configurations.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, brokers);
      configurations.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
      configurations.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
      configurations.put("group.id", groupId);
      configurations.put("enable.auto.commit", "true");
      configurations.put("auto.commit.interval.ms", "1000");
      configurations.put("auto.offset.reset", "earliest");
      configurations.put("session.timeout.ms", "30000");
      configurations.put("security.protocol", "SASL_SSL");
      configurations.put("sasl.mechanism", "SCRAM-SHA-256");
      configurations.put("sasl.jaas.config", jaasCfg);
      return configurations;
   }
}
