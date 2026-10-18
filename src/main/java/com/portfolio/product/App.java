package com.portfolio.product;

import com.portfolio.product.config.DatabaseConfig;
import com.portfolio.product.dao.ProductDao;
import com.portfolio.product.service.ProductService;
import com.portfolio.product.ui.ConsoleUI;

public class App {
    public static void main(String[] args) {
        DatabaseConfig dbConfig = new DatabaseConfig();
        ProductDao productDao = new ProductDao(dbConfig);
        ProductService productService = new ProductService(productDao);
        ConsoleUI consoleUI = new ConsoleUI(productService);

        consoleUI.start();
    }
}
