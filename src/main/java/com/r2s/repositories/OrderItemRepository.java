package com.r2s.repositories;

import com.r2s.entities.OrderDetailEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderDetailEntity, Long> {
}