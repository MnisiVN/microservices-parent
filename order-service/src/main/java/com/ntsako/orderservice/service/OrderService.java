package com.ntsako.orderservice.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ntsako.orderservice.dto.OrderRequest;
import com.ntsako.orderservice.dto.OrderResponse;
import com.ntsako.orderservice.mapper.OrderLineItemsMapper;
import com.ntsako.orderservice.mapper.OrderMapper;
import com.ntsako.orderservice.model.Order;
import com.ntsako.orderservice.model.OrderLineItems;
import com.ntsako.orderservice.repository.OrderRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {
	
	private final OrderRepository orderRepository;
	
	public OrderResponse placeOrder(OrderRequest orderRequest) {
		
		List<OrderLineItems> orderLineItems = orderRequest.getOrderLineItemsDtoList().stream().map(orderLineItemDTO -> OrderLineItemsMapper.mapToOrderLineItems(orderLineItemDTO)).toList();
		
		Order order = Order.builder()
				.orderNumber(UUID.randomUUID().toString())
				.orderlineItems(orderLineItems)
				.build();
		
		Order savedOrder = orderRepository.save(order);
		
		return OrderMapper.mapToOrderResponse(savedOrder);
	}

}
