package com.naical.orderkafka.order;

import com.naical.orderkafka.serdes.JsonMapper;
import com.naical.orderkafka.serdes.SerdesFactory;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.*;

public class OrderTopology {

    public static Topology buildTopology(){
        JsonMapper jsonMapper = new JsonMapper();
        StreamsBuilder streamsBuilder = new StreamsBuilder();

        streamsBuilder.stream("order",
                Consumed.with(Serdes.Long(), SerdesFactory.orderSerdes()))
                .toTable()
                .toStream()
                .print(Printed.<Long, Order>toSysOut().withLabel("order-values"));

        streamsBuilder.stream("order",
                Consumed.with(Serdes.Long(), SerdesFactory.orderSerdes()))
                .mapValues(jsonMapper::orderToAnalytics)
                .to("analytics", Produced.with(Serdes.Long(), SerdesFactory.analyticsSerdes()));

        streamsBuilder.stream("order",
                        Consumed.with(Serdes.Long(), SerdesFactory.orderSerdes()))
                .mapValues(jsonMapper::orderToNotication)
                .to("notification", Produced.with(Serdes.Long(), SerdesFactory.notificationSerdes()));

        return streamsBuilder.build();
    }
}
