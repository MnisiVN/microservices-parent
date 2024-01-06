package com.ntsako.productservice.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ntsako.productservice.dto.ProductRequest;
import com.ntsako.productservice.dto.ProductResponse;
import com.ntsako.productservice.mapper.ProductMapper;
import com.ntsako.productservice.model.Product;
import com.ntsako.productservice.repository.ProductRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ProductService {

	private final ProductRepository productRepository;

	public ProductResponse createProduct(ProductRequest productRequest) {
		Product product = Product.builder().name(productRequest.getName()).description(productRequest.getDescription())
				.price(productRequest.getPrice()).build();

		Product savedProduct = productRepository.save(product);
		log.info("Product with id: {} is saved.", savedProduct.getId());
		
		return ProductMapper.mapToProductResponse(savedProduct);
	}

	public List<ProductResponse> getAllProducts() {
		List<Product> products = productRepository.findAll();

		return products.stream().map(product -> ProductMapper.mapToProductResponse(product)).toList();
	}

	
}
