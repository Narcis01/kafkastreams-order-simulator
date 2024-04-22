package com.naical.orderkafka.user;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.naical.orderkafka.item.Item;
import com.naical.orderkafka.order.Order;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Getter
@Setter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO )
    @JsonProperty("id")
    private Long id;

    @JsonProperty("firstName")
    private String firstName;

    @JsonProperty("lastName")
    private String lastName;

    @JsonProperty("subscription")
    private String subscription;

    @JsonProperty("contactNumber")
    private String contactNumber;

    @OneToMany(mappedBy = "user")
    @JsonIgnore
    private Set<Order> order;

    public void addOrder(Order order){
        if(this.order == null){
            this.order = new HashSet<>();
        }

        this.order.add(order);
        order.setUser(this);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", subscription='" + subscription + '\'' +
                '}';
    }
}
