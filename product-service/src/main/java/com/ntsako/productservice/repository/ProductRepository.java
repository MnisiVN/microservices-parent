package com.ntsako.productservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ntsako.productservice.model.Product;

public interface ProductRepository extends JpaRepository<Product, Long>{

}
