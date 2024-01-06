package com.ntsako.inventoryservice.service;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ntsako.inventoryservice.model.Inventory;
import com.ntsako.inventoryservice.repository.InventoryRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InventoryService {
	
	private final InventoryRepository inventoryRepository;
	
	@Transactional(readOnly = true)
	public boolean isInstock(String skuCode) {
		Optional<Inventory> optional = inventoryRepository.findBySkuCode(skuCode);
		
		if (optional.isPresent()) {
			Inventory inventory = optional.get();
			if (inventory.getQuantity() > 0) {
				return true;
			} else {
				return false;
			}
		}
		
		return false;
	}

}
