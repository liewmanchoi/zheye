package com.liewmanchoi.zheye.config;

import com.liewmanchoi.zheye.event.Event;
import java.util.HashMap;
import java.util.Map;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

/**
 * @author wangsheng
 * @date 2019/8/13
 */
@Configuration
public class KafkaConfig {
  public static final String TOPIC = "zheye";

  @Value("${spring.kafka.producer.bootstrap-servers}")
  private String serverAddresses;

  @Bean
  public KafkaAdmin kafkaAdmin() {
    Map<String, Object> configs = new HashMap<>(1);
    configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, serverAddresses);
    return new KafkaAdmin(configs);
  }

  @Bean
  public NewTopic topic() {
    return new NewTopic(TOPIC, 1, (short) 1);
  }

  private Map<String, Object> producerConfig() {
    Map<String, Object> config = new HashMap<>(3);
    config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, serverAddresses);
    config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
    config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);

    return config;
  }

  @Bean
  public ProducerFactory<String, Event> producerFactory() {
    return new DefaultKafkaProducerFactory<>(producerConfig());
  }

  @Bean
  public KafkaTemplate<String, Event> kafkaTemplate() {
    return new KafkaTemplate<>(producerFactory());
  }

  private Map<String, Object> consumerConfig() {
    Map<String, Object> config = new HashMap<>(3);
    config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, serverAddresses);
    config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
    config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
    config.put(ConsumerConfig.GROUP_ID_CONFIG, "PigeonConsumer");
    config.put(JsonDeserializer.TRUSTED_PACKAGES, "com.liewmanchoi.domain.message");

    return config;
  }

  @Bean
  public ConsumerFactory<String, Event> consumerFactory() {
    return new DefaultKafkaConsumerFactory<>(consumerConfig());
  }

  @Bean
  public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, Event>>
  kafkaListenerContainerFactory() {
    ConcurrentKafkaListenerContainerFactory<String, Event> factory =
        new ConcurrentKafkaListenerContainerFactory<>();
    factory.setConsumerFactory(consumerFactory());
    // 允许批量消费消息
    factory.setBatchListener(true);
    return factory;
  }
}
