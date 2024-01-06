package com.ntsako.orderservice.mapper;

import java.util.List;

import com.ntsako.orderservice.dto.OrderLineItemsDto;
import com.ntsako.orderservice.dto.OrderResponse;
import com.ntsako.orderservice.model.Order;

public class OrderMapper {

	public static OrderResponse mapToOrderResponse(Order order) {

		List<OrderLineItemsDto> orderLineItemsDtoList = order.getOrderlineItems().stream()
				.map(orderLineItem -> OrderLineItemsMapper.mapToOrderLineItemsDto(orderLineItem)).toList();

		return OrderResponse.builder().id(order.getId()).orderNumber(order.getOrderNumber())
				.orderlineItemsDtoList(orderLineItemsDtoList).build();
	}

}
