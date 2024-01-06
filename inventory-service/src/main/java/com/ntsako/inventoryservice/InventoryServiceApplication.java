package com.ntsako.inventoryservice;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.ntsako.inventoryservice.model.Inventory;
import com.ntsako.inventoryservice.repository.InventoryRepository;

@SpringBootApplication
public class InventoryServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(InventoryServiceApplication.class, args);
	}

    @Bean
    CommandLineRunner loadData(InventoryRepository inventoryRepository) {
		
		return args -> {
			Inventory inventory = Inventory.builder()
					.skuCode("Iphone_13")
					.quantity(100)
					.build();
			
			inventoryRepository.save(inventory);
			
			Inventory inventory1 = Inventory.builder()
					.skuCode("Iphone_13_red")
					.quantity(0)
					.build();
			
			inventoryRepository.save(inventory1);
		};
	}

}
