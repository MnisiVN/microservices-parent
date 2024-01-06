package com.ntsako.orderservice.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ntsako.orderservice.dto.OrderRequest;
import com.ntsako.orderservice.dto.OrderResponse;
import com.ntsako.orderservice.service.OrderService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
public class OrderController {
	
	private final OrderService orderService;

	@PostMapping
	public ResponseEntity<OrderResponse> placeOrder(@RequestBody OrderRequest orderRequest) {
		OrderResponse placedOrderResponse = orderService.placeOrder(orderRequest);
		return new ResponseEntity<>(placedOrderResponse, HttpStatus.CREATED);
	}
}
