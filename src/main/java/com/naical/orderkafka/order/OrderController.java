package com.naical.orderkafka.order;


import com.naical.orderkafka.item.Item;
import com.naical.orderkafka.user.User;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.streams.KafkaStreams;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/producer")
@Component
public class OrderController {
    private final KafkaProducer<Long, Order> kafkaProducer;
    private final KafkaStreams kafkaStreams;

    public OrderController(@Qualifier("kafkaStreamsForProducer") KafkaProducer<Long, Order> kafkaProducer,
                           @Qualifier("kafkaStreamsForConsumerOrder") KafkaStreams kafkaStreams) {
        this.kafkaProducer = kafkaProducer;
        this.kafkaStreams = kafkaStreams;

        this.kafkaStreams.start();
    }

    @PostMapping
    public ResponseEntity<?> producer(){
        List<Order> orderList = new ArrayList<>();
        for(int i = 0; i< 10; i++) {
            User user = User.builder().id((long) i+100).firstName("firstName").lastName("lastName").subscription("PREMIUM").contactNumber("07contactme").build();
            Item item = Item.builder().id((long) i+100).name("Item").price(1.0 + i).build();
            Order order = Order.builder().id((long) i+100).address("Address").city("City-" + i).value(1000.0 + i).build();
            order.addItem(item);
            user.addOrder(order);

            orderList.add(order);
            kafkaProducer.send(new ProducerRecord<>("order", order.getId(), order));
        }
        return ResponseEntity.ok(orderList);
    }
}
