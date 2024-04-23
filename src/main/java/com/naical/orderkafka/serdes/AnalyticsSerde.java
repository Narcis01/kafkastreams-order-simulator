package com.naical.orderkafka.serdes;

import com.naical.orderkafka.analytics.Analytics;
import com.naical.orderkafka.notification.Notification;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.serialization.Serializer;

public class AnalyticsSerde extends Serdes.WrapperSerde<Analytics> {
    public AnalyticsSerde(Serializer<Analytics> serializer, Deserializer<Analytics> deserializer) {
        super(serializer, deserializer);
    }

    public AnalyticsSerde(){
        super(new JsonSerializer<>(), new JsonDeserializer<>(Analytics.class));
    }
}
