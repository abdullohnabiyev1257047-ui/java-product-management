package com.portfolio.product.service;

import com.portfolio.product.dao.ProductDao;
import com.portfolio.product.exception.ProductException;
import com.portfolio.product.model.Product;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ProductService {
    private final ProductDao productDao;

    public ProductService(ProductDao productDao) {
        this.productDao = productDao;
    }

    public Product addProduct(String name, String category, double price, int stockQuantity) {
        validateProductInput(name, category, price, stockQuantity);
        Product product = new Product(name.trim(), category.trim(), price, stockQuantity);
        return productDao.save(product);
    }

    public Optional<Product> getProductById(Long id) {
        if (id == null || id <= 0) {
            throw new ProductException("Invalid product ID.");
        }
        return productDao.findById(id);
    }

    public List<Product> getAllProducts() {
        return productDao.findAll();
    }

    public void updateProduct(Long id, String name, String category, double price, int stockQuantity) {
        if (id == null || id <= 0) {
            throw new ProductException("Invalid product ID for update.");
        }
        validateProductInput(name, category, price, stockQuantity);

        Product existing = getProductById(id)
                .orElseThrow(() -> new ProductException("Product with ID " + id + " not found."));

        existing.setName(name.trim());
        existing.setCategory(category.trim());
        existing.setPrice(price);
        existing.setStockQuantity(stockQuantity);

        productDao.update(existing);
    }

    public void deleteProduct(Long id) {
        if (id == null || id <= 0) {
            throw new ProductException("Invalid product ID for deletion.");
        }
        if (productDao.findById(id).isEmpty()) {
            throw new ProductException("Product with ID " + id + " not found.");
        }
        productDao.delete(id);
    }

    public void adjustStock(Long id, int quantityChange) {
        Product product = getProductById(id)
                .orElseThrow(() -> new ProductException("Product with ID " + id + " not found."));

        int newStock = product.getStockQuantity() + quantityChange;
        if (newStock < 0) {
            throw new ProductException("Stock quantity cannot be negative. Current stock: " + product.getStockQuantity());
        }

        product.setStockQuantity(newStock);
        productDao.update(product);
    }

    public List<Product> searchProducts(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return getAllProducts();
        }
        String lowerKeyword = keyword.toLowerCase().trim();
        return productDao.findAll().stream()
                .filter(p -> p.getName().toLowerCase().contains(lowerKeyword) ||
                        p.getCategory().toLowerCase().contains(lowerKeyword))
                .collect(Collectors.toList());
    }

    public List<Product> filterByCategory(String category) {
        if (category == null || category.trim().isEmpty()) {
            return getAllProducts();
        }
        String lowerCategory = category.toLowerCase().trim();
        return productDao.findAll().stream()
                .filter(p -> p.getCategory().toLowerCase().equals(lowerCategory))
                .collect(Collectors.toList());
    }

    public List<Product> sortProducts(List<Product> products, String sortBy, boolean ascending) {
        Comparator<Product> comparator;
        switch (sortBy.toLowerCase()) {
            case "price":
                comparator = Comparator.comparingDouble(Product::getPrice);
                break;
            case "stock":
                comparator = Comparator.comparingInt(Product::getStockQuantity);
                break;
            case "name":
            default:
                comparator = Comparator.comparing(Product::getName, String.CASE_INSENSITIVE_ORDER);
                break;
        }

        if (!ascending) {
            comparator = comparator.reversed();
        }

        return products.stream().sorted(comparator).collect(Collectors.toList());
    }

    public void loadSampleData() {
        if (productDao.findAll().isEmpty()) {
            addProduct("MacBook Pro 16", "Electronics", 2499.99, 15);
            addProduct("Wireless Mouse", "Electronics", 49.99, 120);
            addProduct("Ergonomic Chair", "Furniture", 199.50, 25);
            addProduct("Coffee Beans 1kg", "Groceries", 24.99, 80);
            addProduct("Standing Desk", "Furniture", 450.00, 10);
        }
    }

    private void validateProductInput(String name, String category, double price, int stockQuantity) {
        if (name == null || name.trim().isEmpty()) {
            throw new ProductException("Product name cannot be empty.");
        }
        if (category == null || category.trim().isEmpty()) {
            throw new ProductException("Product category cannot be empty.");
        }
        if (price < 0) {
            throw new ProductException("Product price cannot be negative.");
        }
        if (stockQuantity < 0) {
            throw new ProductException("Stock quantity cannot be negative.");
        }
    }
}
