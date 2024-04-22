package com.naical.orderkafka.order;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.naical.orderkafka.item.Item;
import com.naical.orderkafka.user.User;
import jakarta.persistence.*;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity(name = "Order_Entity")
@Getter
@Setter
@ToString
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO )
    @JsonProperty("id")
    private Long id;

    @JsonProperty("address")
    private String address;

    @JsonProperty("value")
    private Double value;

    @JsonProperty("city")
    private String city;

    @JsonProperty("user")
    @ManyToOne
    private User user;

    @JsonProperty("item")
    @OneToMany(mappedBy = "order")
    @JsonManagedReference
    private Set<Item> item;



    public void addItem(Item item){
        if(this.item == null){
            this.item = new HashSet<>();
        }

        this.item.add(item);
        item.setOrder(this);
    }

}
