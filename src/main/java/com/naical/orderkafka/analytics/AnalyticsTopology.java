package com.naical.orderkafka.analytics;

import com.naical.orderkafka.serdes.SerdesFactory;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.*;

public class AnalyticsTopology {

    public static Topology buildTopology(){

        StreamsBuilder streamsBuilder = new StreamsBuilder();

        streamsBuilder.stream("analytics",
                        Consumed.with(Serdes.Long(), SerdesFactory.analyticsSerdes()))
                .groupBy((k,v) -> v.getCity(), Grouped.with(Serdes.String(), SerdesFactory.analyticsSerdes()))
                .count(Materialized.as("analytics-city"))
                .toStream()
                .print(Printed.<String, Long>toSysOut().withLabel("city-count"));

        streamsBuilder.stream("analytics",
                        Consumed.with(Serdes.Long(), SerdesFactory.analyticsSerdes()))
                .groupBy((k,v) -> v.getSubscription(), Grouped.with(Serdes.String(), SerdesFactory.analyticsSerdes()))
                .count(Materialized.as("analytics-subscribe"))
                .toStream()
                .print(Printed.<String, Long>toSysOut().withLabel("subscribe-count"));


        return streamsBuilder.build();
    }
}
