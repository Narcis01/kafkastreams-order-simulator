package com.naical.orderkafka;

import com.naical.orderkafka.serdes.SerdesFactory;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.LongSerializer;
import org.apache.kafka.streams.StreamsConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;

import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

@SpringBootApplication
@Slf4j
@EnableKafka
public class OrderKafkaApplication {

	public static void main(String[] args) {
		SpringApplication.run(OrderKafkaApplication.class, args);
		//createTopics(propsOrder(), List.of("order", "analytics", "notification"));

	}


	private static void createTopics(Properties config, List<String> topics) {

		AdminClient admin = AdminClient.create(config);
		var partitions = 1;
		short replication  = 1;

		var newTopics = topics
				.stream()
				.map(topic ->{
					return new NewTopic(topic, partitions, replication);
				})
				.collect(Collectors.toList());

		var createTopicResult = admin.createTopics(newTopics);
		try {
			createTopicResult
					.all().get();
			log.info("topics are created successfully");
		} catch (Exception e) {
			log.error("Exception creating topics : {} ",e.getMessage(), e);
		}
	}


	private static Properties propsOrder(){
		Properties properties = new Properties();
		properties.put(StreamsConfig.APPLICATION_ID_CONFIG, "order-consumer");
		properties.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
		properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
		properties.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, LongSerializer.class.getName());
		properties.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, SerdesFactory.orderSerdes().getClass().getName());

		return properties;

	}




}
