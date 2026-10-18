package com.portfolio.product.service;

import com.portfolio.product.config.DatabaseConfig;
import com.portfolio.product.dao.ProductDao;
import com.portfolio.product.exception.ProductException;
import com.portfolio.product.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class ProductServiceTest {

    private ProductService productService;

    @BeforeEach
    void setUp() {
        // Use in-memory SQLite database for testing
        DatabaseConfig dbConfig = new DatabaseConfig("jdbc:sqlite::memory:");
        ProductDao productDao = new ProductDao(dbConfig);
        productService = new ProductService(productDao);
    }

    @Test
    void testAddAndGetProduct() {
        Product product = productService.addProduct("Test Laptop", "Electronics", 999.99, 10);
        assertNotNull(product.getId());
        assertEquals("Test Laptop", product.getName());
        assertEquals("Electronics", product.getCategory());
        assertEquals(999.99, product.getPrice());
        assertEquals(10, product.getStockQuantity());

        Optional<Product> fetched = productService.getProductById(product.getId());
        assertTrue(fetched.isPresent());
        assertEquals(product.getName(), fetched.get().getName());
    }

    @Test
    void testAddProductValidation() {
        assertThrows(ProductException.class, () -> productService.addProduct("", "Electronics", 100.0, 5));
        assertThrows(ProductException.class, () -> productService.addProduct("Mouse", "", 10.0, 5));
        assertThrows(ProductException.class, () -> productService.addProduct("Mouse", "Electronics", -10.0, 5));
        assertThrows(ProductException.class, () -> productService.addProduct("Mouse", "Electronics", 10.0, -1));
    }

    @Test
    void testUpdateProduct() {
        Product product = productService.addProduct("Phone", "Electronics", 500.0, 20);
        productService.updateProduct(product.getId(), "Smart Phone", "Electronics", 550.0, 18);

        Product updated = productService.getProductById(product.getId()).orElseThrow();
        assertEquals("Smart Phone", updated.getName());
        assertEquals(550.0, updated.getPrice());
        assertEquals(18, updated.getStockQuantity());
    }

    @Test
    void testDeleteProduct() {
        Product product = productService.addProduct("Tablet", "Electronics", 300.0, 8);
        Long id = product.getId();

        productService.deleteProduct(id);
        assertTrue(productService.getProductById(id).isEmpty());
    }

    @Test
    void testAdjustStock() {
        Product product = productService.addProduct("Monitor", "Electronics", 150.0, 10);
        
        productService.adjustStock(product.getId(), 5);
        assertEquals(15, productService.getProductById(product.getId()).get().getStockQuantity());

        productService.adjustStock(product.getId(), -7);
        assertEquals(8, productService.getProductById(product.getId()).get().getStockQuantity());

        // Negative stock test
        assertThrows(ProductException.class, () -> productService.adjustStock(product.getId(), -20));
    }

    @Test
    void testSearchAndFilter() {
        productService.addProduct("Dell XPS", "Laptops", 1200.0, 5);
        productService.addProduct("MacBook Air", "Laptops", 1100.0, 7);
        productService.addProduct("Office Desk", "Furniture", 200.0, 4);

        List<Product> laptopSearch = productService.searchProducts("laptop");
        assertEquals(2, laptopSearch.size());

        List<Product> furnitureFilter = productService.filterByCategory("Furniture");
        assertEquals(1, furnitureFilter.size());
        assertEquals("Office Desk", furnitureFilter.get(0).getName());
    }
}
