package com.naical.orderkafka.serdes;

import com.naical.orderkafka.notification.Notification;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.serialization.Serializer;

public class NotificationSerde extends Serdes.WrapperSerde<Notification> {
    public NotificationSerde(Serializer<Notification> serializer, Deserializer<Notification> deserializer) {
        super(serializer, deserializer);
    }

    public NotificationSerde(){
        super(new JsonSerializer<>(), new JsonDeserializer<>(Notification.class));
    }
}
