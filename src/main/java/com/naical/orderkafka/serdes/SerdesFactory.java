package com.naical.orderkafka.serdes;

import com.naical.orderkafka.analytics.Analytics;
import com.naical.orderkafka.notification.Notification;
import com.naical.orderkafka.order.Order;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;

public class SerdesFactory {

    public static Serde<Order> orderSerdes(){

        JsonSerializer<Order> jsonSerializer = new JsonSerializer<>();

        JsonDeserializer<Order> jsonDeSerializer = new JsonDeserializer<>(Order.class);
        return  Serdes.serdeFrom(jsonSerializer, jsonDeSerializer);
    }

    public static Serde<Analytics> analyticsSerdes(){

        JsonSerializer<Analytics> jsonSerializer = new JsonSerializer<>();

        JsonDeserializer<Analytics> jsonDeSerializer = new JsonDeserializer<>(Analytics.class);
        return  Serdes.serdeFrom(jsonSerializer, jsonDeSerializer);
    }

    public static Serde<Notification> notificationSerdes(){

        JsonSerializer<Notification> jsonSerializer = new JsonSerializer<>();

        JsonDeserializer<Notification> jsonDeSerializer = new JsonDeserializer<>(Notification.class);
        return  Serdes.serdeFrom(jsonSerializer, jsonDeSerializer);
    }
}
