package com.naical.orderkafka.analytics;

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
public class AnalyticsService {

    private final KafkaStreams kafkaStreams;
    private static final String STORE_CITY = "analytics-city";
    private static final String STORE_SUBSCRIBE = "analytics-subscribe";

    public AnalyticsService(@Qualifier("kafkaStreamsForConsumerAnalytics") KafkaStreams kafkaStreams) {
        this.kafkaStreams = kafkaStreams;
        this.kafkaStreams.start();
    }


    public Map<String, Long> cityCount(){
        ReadOnlyKeyValueStore<String, Long> city = kafkaStreams
                .store(StoreQueryParameters.fromNameAndType(STORE_CITY, QueryableStoreTypes.keyValueStore()));
        Map<String, Long> cityList = new HashMap<>();
        city.all().forEachRemaining(v -> cityList.put(v.key,v.value));
        log.info(cityList.toString());
        return cityList;
    }

    public Map<String, Long> subscribeCount(){
        ReadOnlyKeyValueStore<String, Long> subscribe =  kafkaStreams.store(StoreQueryParameters.fromNameAndType(STORE_SUBSCRIBE, QueryableStoreTypes.keyValueStore()));

        Map<String, Long> subscribeList = new HashMap<>();
        subscribe.all().forEachRemaining(v -> subscribeList.put(v.key, v.value));
        log.info(subscribeList.toString());
        return subscribeList;
    }

}
