package com.ntsako.productservice.mapper;

import com.ntsako.productservice.dto.ProductResponse;
import com.ntsako.productservice.model.Product;

public class ProductMapper {
	
	public static ProductResponse mapToProductResponse(Product product) {
		return ProductResponse.builder().id(product.getId()).name(product.getName())
				.description(product.getDescription()).price(product.getPrice()).build();
	}

}
