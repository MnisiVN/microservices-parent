package com.ntsako.orderservice.mapper;

import com.ntsako.orderservice.dto.OrderLineItemsDto;
import com.ntsako.orderservice.model.OrderLineItems;

public class OrderLineItemsMapper {

	public static OrderLineItems mapToOrderLineItems(OrderLineItemsDto orderLineItemsDto) {

		return OrderLineItems.builder().price(orderLineItemsDto.getPrice()).quantity(orderLineItemsDto.getQuantity())
				.skuCode(orderLineItemsDto.getSkuCode()).build();

	}

	public static OrderLineItemsDto mapToOrderLineItemsDto(OrderLineItems orderLineItems) {

		return OrderLineItemsDto.builder().id(orderLineItems.getId()).price(orderLineItems.getPrice())
				.quantity(orderLineItems.getQuantity()).skuCode(orderLineItems.getSkuCode()).build();
	}

}
