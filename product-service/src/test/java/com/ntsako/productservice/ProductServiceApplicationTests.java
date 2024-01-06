package com.ntsako.productservice;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ntsako.productservice.dto.ProductRequest;
import com.ntsako.productservice.dto.ProductResponse;
import com.ntsako.productservice.model.Product;
import com.ntsako.productservice.repository.ProductRepository;

@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
@ContextConfiguration(classes = ProductServiceApplication.class)
class ProductServiceApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private ProductRepository productRepository;

	@Container
	private static final MySQLContainer<?> mysqlContainer = new MySQLContainer<>("mysql:8.0.19")
			.withDatabaseName("test_product_service_db").withUsername("root").withPassword("")
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
		
		productRepository.save(Product.builder()
				.name("Samsung A20")
				.description("Balck Samsung A20")
				.price(19200)
				.build());
	}

	@AfterEach
	void tearDown() {
		
		productRepository.deleteAll();
	}
	
	@Test
	void testMySQLContainerIsRunning() {
		Assertions.assertTrue(mysqlContainer.isRunning());
	}

	@Test
	void shouldCreateProduct() throws Exception {
		ProductRequest productRequest = getProductRequest();
		String productRequestStr = objectMapper.writeValueAsString(productRequest);
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/api/product")
				.contentType(MediaType.APPLICATION_JSON).content(productRequestStr)).andExpect(status().isCreated())
				.andReturn();

		String responseContent = mvcResult.getResponse().getContentAsString();

		ProductResponse productResponse = objectMapper.readValue(responseContent, ProductResponse.class);
		Assertions.assertNotNull(productResponse);
		Assertions.assertNotNull(productResponse.getId());
	}

	@Test
	void shouldGetAllProduct() throws Exception {
		MvcResult mvcResult = mockMvc
				.perform(MockMvcRequestBuilders.get("/api/product").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andReturn();

		String responseContent = mvcResult.getResponse().getContentAsString();

		List<ProductResponse> productList = objectMapper.readValue(responseContent,
				new TypeReference<List<ProductResponse>>() {
				});
		Assertions.assertTrue(productList.size() == 1);
		Assertions.assertTrue(productList.get(0).getName().equals("Samsung A20"));
		Assertions.assertTrue(productList.get(0).getDescription().equals("Balck Samsung A20"));
		Assertions.assertTrue(productList.get(0).getPrice() == 19200);
	}

	private ProductRequest getProductRequest() {
		return ProductRequest.builder()
				.name("Iphone 13")
				.description("Iphone 13")
				.price(20000)
				.build();
	}

}
