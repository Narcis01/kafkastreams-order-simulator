package com.naical.orderkafka.config;

import com.naical.orderkafka.analytics.AnalyticsTopology;
import com.naical.orderkafka.notification.NotificationTopology;
import com.naical.orderkafka.order.Order;
import com.naical.orderkafka.order.OrderTopology;
import com.naical.orderkafka.serdes.AnalyticsSerde;
import com.naical.orderkafka.serdes.NotificationSerde;
import com.naical.orderkafka.serdes.SerdesFactory;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.LongSerializer;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

@Configuration
public class KafkaConfig {
    @Value("${spring.kafka.streams.bootstrap-servers}")
    private String bootstrapServers;
    @Bean
    public KafkaStreams kafkaStreamsForConsumerAnalytics() {
        Properties properties = new Properties();
        properties.put(StreamsConfig.APPLICATION_ID_CONFIG, "analytics-consumer");
        properties.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        //properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        properties.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.Long().getClass());
        properties.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, AnalyticsSerde.class.getName());


        return new KafkaStreams(AnalyticsTopology.buildTopology(), properties);
    }

    @Bean
    public KafkaStreams kafkaStreamsForConsumerOrder() {
        Properties properties = new Properties();
        properties.put(StreamsConfig.APPLICATION_ID_CONFIG, "order-consumer");
        properties.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        properties.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, LongSerializer.class.getName());
        properties.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, SerdesFactory.orderSerdes().getClass().getName());


        return new KafkaStreams(OrderTopology.buildTopology(), properties);
    }

    @Bean
    public KafkaStreams kafkaStreamsForConsumerNotification() {
        Properties properties = new Properties();
        properties.put(StreamsConfig.APPLICATION_ID_CONFIG, "notification-consumer");
        properties.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        //properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        properties.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.Long().getClass());
        properties.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, NotificationSerde.class.getName());


        return new KafkaStreams(NotificationTopology.buildTopology(), properties);
    }

    @Bean
    public KafkaProducer<Long, Order> kafkaStreamsForProducer() {
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, LongSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, SerdesFactory.orderSerdes().serializer().getClass());
        props.put(StreamsConfig.STATE_DIR_CONFIG, "tmp/state-store");


        return new KafkaProducer<>(props);
    }




}
