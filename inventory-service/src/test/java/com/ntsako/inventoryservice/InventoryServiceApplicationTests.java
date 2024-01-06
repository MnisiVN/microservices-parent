package com.ntsako.inventoryservice;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ntsako.inventoryservice.model.Inventory;
import com.ntsako.inventoryservice.repository.InventoryRepository;

@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
@ContextConfiguration(classes = InventoryServiceApplication.class)
class InventoryServiceApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private InventoryRepository inventoryRepository;

	@Container
	private static final MySQLContainer<?> mysqlContainer = new MySQLContainer<>("mysql:8.0.19")
			.withDatabaseName("test_inventory_service_db").withUsername("root").withPassword("")
			.waitingFor(Wait.forHealthcheck());

	@BeforeAll
	static void setUp() {
		mysqlContainer.start();
	}

	@DynamicPropertySource
	static void setProperties(DynamicPropertyRegistry registry) {
		registry.add("spring.datasource.url", () -> mysqlContainer.getJdbcUrl());
		registry.add("spring.datasource.username", () -> mysqlContainer.getUsername());
		registry.add("spring.datasource.password", () -> mysqlContainer.getPassword());
		registry.add("spring.jpa.hibernate.ddl-auto", () -> "update");
		registry.add("spring.jpa.properties.hibernate.dialect", () -> "org.hibernate.dialect.MySQLDialect");
	}

	@BeforeEach
	void setup() {
		
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
	}

	@AfterEach
	void tearDown() {
		
		inventoryRepository.deleteAll();
	}
	
	@Test
	void testMySQLContainerIsRunning() {
		Assertions.assertTrue(mysqlContainer.isRunning());
	}
	
	@Test
	void shouldCheckIfIsInstock() throws Exception {
		MvcResult mvcResult = mockMvc
				.perform(MockMvcRequestBuilders.get("/api/inventory/Iphone_13").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andReturn();

		String responseContent = mvcResult.getResponse().getContentAsString();
		
		boolean isInStock = objectMapper.readValue(responseContent, Boolean.class);
		
		Assertions.assertTrue(isInStock);
	}
	
	@Test
	void shouldCheckIfNotIsInstock() throws Exception {
		MvcResult mvcResult = mockMvc
				.perform(MockMvcRequestBuilders.get("/api/inventory/Iphone_13_red").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andReturn();

		String responseContent = mvcResult.getResponse().getContentAsString();
		
		boolean isInStock = objectMapper.readValue(responseContent, Boolean.class);
		
		Assertions.assertFalse(isInStock);
	}

}
