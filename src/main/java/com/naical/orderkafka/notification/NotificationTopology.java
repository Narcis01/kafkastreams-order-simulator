package com.naical.orderkafka.notification;

import com.naical.orderkafka.serdes.SerdesFactory;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.Materialized;

import java.util.Objects;

public class NotificationTopology {


    public static Topology buildTopology(){
        StreamsBuilder streamsBuilder = new StreamsBuilder();

        streamsBuilder.table("notification",
                Consumed.with(Serdes.Long(), SerdesFactory.notificationSerdes()))
                .filter((k,v) -> Objects.equals(v.getSubscription(), "PREMIUM"),
                        Materialized.as("notification"));

        return streamsBuilder.build();
    }



}
