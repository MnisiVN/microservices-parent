package com.ntsako.orderservice;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.List;

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
import com.ntsako.orderservice.dto.OrderLineItemsDto;
import com.ntsako.orderservice.dto.OrderRequest;
import com.ntsako.orderservice.dto.OrderResponse;
import com.ntsako.orderservice.model.Order;
import com.ntsako.orderservice.model.OrderLineItems;
import com.ntsako.orderservice.repository.OrderRepository;

@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
@ContextConfiguration(classes = OrderServiceApplication.class)
class OrderServiceApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private OrderRepository orderRepository;

	@Container
	private static final MySQLContainer<?> mysqlContainer = new MySQLContainer<>("mysql:8.0.19")
			.withDatabaseName("test_oder_service_db").withUsername("root").withPassword("")
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
		List<OrderLineItems> orderLineItems = Arrays
				.asList(OrderLineItems.builder().price(24000).skuCode("Iphone_13").quantity(2).build());
		orderRepository.save(Order.builder().orderlineItems(orderLineItems).build());
	}

	@AfterEach
	void tearDown() {

		orderRepository.deleteAll();
	}
	
	@Test
	void testMySQLContainerIsRunning() {
		Assertions.assertTrue(mysqlContainer.isRunning());
	}
	
	@Test
	 void shouldPlaceOrder() throws Exception {
		OrderRequest orderRequest = getOrderRequest();
		String oderRequestStr = objectMapper.writeValueAsString(orderRequest);
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/api/order")
				.contentType(MediaType.APPLICATION_JSON).content(oderRequestStr)).andExpect(status().isCreated())
				.andReturn();

		String responseContent = mvcResult.getResponse().getContentAsString();

		OrderResponse orderResponse = objectMapper.readValue(responseContent, OrderResponse.class);
		Assertions.assertNotNull(orderResponse);
		Assertions.assertNotNull(orderResponse.getId());
	}
	
	private OrderRequest getOrderRequest() {
		
		List<OrderLineItemsDto> orderLineItemsDtoList = Arrays
				.asList(OrderLineItemsDto.builder().price(22000).skuCode("Iphone_13").quantity(3).build());
		return OrderRequest.builder().orderLineItemsDtoList(orderLineItemsDtoList).build();
	}
}
