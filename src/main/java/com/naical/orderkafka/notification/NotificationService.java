package com.naical.orderkafka.notification;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StoreQueryParameters;
import org.apache.kafka.streams.state.QueryableStoreTypes;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class NotificationService {

    private final KafkaStreams kafkaStreams;

    public NotificationService(@Qualifier("kafkaStreamsForConsumerNotification") KafkaStreams kafkaStreams) {
        this.kafkaStreams = kafkaStreams;
        this.kafkaStreams.start();
    }

    public Map<Long, Notification> premiumCustomer(String store){
        ReadOnlyKeyValueStore<Long, Notification> notification = kafkaStreams
                .store(StoreQueryParameters.fromNameAndType(store, QueryableStoreTypes.keyValueStore()));

        Map<Long, Notification> notificationMap = new HashMap<>();
        notification.all().forEachRemaining(v -> notificationMap.put(v.key, v.value));
        log.info(notificationMap.toString());
        return notificationMap;

    }
}
