package com.ntsako.orderservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ntsako.orderservice.model.Order;

public interface OrderRepository extends JpaRepository<Order, Long>{

}
