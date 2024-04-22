package com.naical.orderkafka.serdes;


import com.naical.orderkafka.analytics.Analytics;
import com.naical.orderkafka.notification.Notification;
import com.naical.orderkafka.order.Order;

public class JsonMapper {


    public Analytics orderToAnalytics(Order order){

        return Analytics.builder()
                .city(order.getCity())
                .subscription(order.getUser().getSubscription())
                .value(order.getValue())
                .id(order.getId())
                .build();
    }

    public Notification orderToNotication(Order order){

        return Notification.builder()
                .orderId(order.getId())
                .contactNumber(order.getUser().getContactNumber())
                .firstName(order.getUser().getFirstName())
                .lastName(order.getUser().getLastName())
                .id(order.getId())
                .subscription(order.getUser().getSubscription())
                .contactNumber(order.getUser().getContactNumber())
                .build();
    }
}
