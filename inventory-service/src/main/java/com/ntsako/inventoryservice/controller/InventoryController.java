package com.ntsako.inventoryservice.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ntsako.inventoryservice.service.InventoryService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
public class InventoryController {
	
	private final InventoryService inventoryService;
	
	@GetMapping("/{skuCode}")
	public ResponseEntity<Boolean> isInstock(@PathVariable String skuCode) {
		boolean isInstock = inventoryService.isInstock(skuCode);
		return new ResponseEntity<>(isInstock, HttpStatus.OK);
	}

}
