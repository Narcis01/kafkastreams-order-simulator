package com.naical.orderkafka.kafka.topology;

import com.naical.orderkafka.analytics.Analytics;
import com.naical.orderkafka.item.Item;
import com.naical.orderkafka.notification.Notification;
import com.naical.orderkafka.order.Order;
import com.naical.orderkafka.order.OrderTopology;
import com.naical.orderkafka.serdes.SerdesFactory;
import com.naical.orderkafka.user.User;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.TestInputTopic;
import org.apache.kafka.streams.TestOutputTopic;
import org.apache.kafka.streams.TopologyTestDriver;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class OrderTests {

    TopologyTestDriver topologyTestDriver = null;

    TestInputTopic<Long, Order> inputTopic = null;

    TestOutputTopic<Long, Analytics> analyticsOutputTopic = null;

    TestOutputTopic<Long, Notification> notificationOutputTopic = null;

    static String INPUT_TOPIC = "order";

    static String ANALYTICS_TOPIC = "analytics";

    static String NOTIFICATION_TOPIC = "notification";

    User user = new User();
    Item item = new Item();
    Order order = new Order();

    @BeforeEach
    void setUp() {
        topologyTestDriver = new TopologyTestDriver(OrderTopology.buildTopology());

        inputTopic = topologyTestDriver.createInputTopic(INPUT_TOPIC,
                Serdes.Long().serializer(), SerdesFactory.orderSerdes().serializer());

        analyticsOutputTopic = topologyTestDriver.createOutputTopic(ANALYTICS_TOPIC,
                Serdes.Long().deserializer(), SerdesFactory.analyticsSerdes().deserializer());

        notificationOutputTopic = topologyTestDriver.createOutputTopic(NOTIFICATION_TOPIC,
                Serdes.Long().deserializer(), SerdesFactory.notificationSerdes().deserializer());

        user = User.builder().id(1L).firstName("firstName").lastName("lastName").subscription("PREMIUM").build();
        item = Item.builder().id(1L).name("Item").price(200.0).build();
        order = Order.builder().id(1L).address("Address").city("City").value(200.0).build();
        order.addItem(item);
        user.addOrder(order);

    }

    @AfterEach
    void tearDown() {
        topologyTestDriver.close();
    }

    @Test
    void buildTopology_singleInput(){
        inputTopic.pipeInput(order.getId(), order);

        Long notificationSize = notificationOutputTopic.getQueueSize();
        Long analyticsSize = analyticsOutputTopic.getQueueSize();

        assertEquals(1,notificationSize);
        assertEquals(1, analyticsSize);
    }

    @Test
    void buildTopology_multipleInput(){
        inputTopic.pipeInput(order.getId(), order);
        inputTopic.pipeInput(order.getId(), order);

        Long notificationSize = notificationOutputTopic.getQueueSize();
        Long analyticsSize = analyticsOutputTopic.getQueueSize();

        assertEquals(2,notificationSize);
        assertEquals(2, analyticsSize);
    }
}
