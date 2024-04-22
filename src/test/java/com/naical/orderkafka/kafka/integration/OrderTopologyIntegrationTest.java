package com.naical.orderkafka.kafka.integration;


import com.naical.orderkafka.item.Item;
import com.naical.orderkafka.notification.NotificationService;
import com.naical.orderkafka.order.Order;
import com.naical.orderkafka.user.User;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.TestPropertySource;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.Matchers.equalTo;

@SpringBootTest
@EmbeddedKafka(topics = {"order", "notification", "analytics"})
@TestPropertySource(properties = {
        "spring.kafka.streams.bootstrap-servers=${spring.embedded.kafka.brokers}",
        "spring.kafka.producer.bootstrap-servers=${spring.embedded.kafka.brokers}",
        "spring.kafka.producer.key-serializer=org.apache.kafka.common.serialization.LongSerializer",
        "spring.kafka.producer.value-serializer=org.springframework.kafka.support.serializer.JsonSerializer"
})
public class OrderTopologyIntegrationTest {

    @Autowired
    KafkaProducer<Long, Order> kafkaProducer;

    @Autowired
    NotificationService notificationService;

    @BeforeEach
    void setUp() {

    }

    @Test
    void orderTest(){
        publishOrders();

        Awaitility.await().atMost(10, TimeUnit.SECONDS)
                .pollDelay(Duration.ofSeconds(1))
                .ignoreExceptions()
                .until(() -> notificationService.premiumCustomer().size(), equalTo(13) );
    }

    private void publishOrders(){
        User user1 = User.builder().id(101L).firstName("firstName").lastName("lastName").subscription("PREMIUM").build();
        Item item1 = Item.builder().id(101L).name("Item").price(200.0).build();
        Order order1 = Order.builder().id(101L).address("Address").city("City").value(200.0).build();
        order1.addItem(item1);
        user1.addOrder(order1);

        User user2 = User.builder().id(200L).firstName("firstName").lastName("lastName").subscription("PREMIUM").build();
        Item item2 = Item.builder().id(200L).name("Item").price(200.0).build();
        Order order2 = Order.builder().id((200L)).address("Address").city("City").value(200.0).build();
        order2.addItem(item2);
        user2.addOrder(order2);

        User user3 = User.builder().id(300L).firstName("firstName").lastName("lastName").subscription("PREMIUM").build();
        Item item3 = Item.builder().id(300L).name("Item").price(200.0).build();
        Order order3 = Order.builder().id(300L).address("Address").city("City").value(200.0).build();
        order3.addItem(item3);
        user3.addOrder(order3);

        List.of(order1, order2, order3).forEach(o -> kafkaProducer.send(new ProducerRecord<>("order", o.getId(), o)));
    }
}
