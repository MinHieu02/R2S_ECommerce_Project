package com.r2s.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "orders")
public class OrderEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "order")
    private List<OrderDetailEntity> orderDetails = new ArrayList<>();

    @Column(name = "total", nullable = false)
    private Double total;

    @Column(name = "orders_day", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date ordersDay;
}
