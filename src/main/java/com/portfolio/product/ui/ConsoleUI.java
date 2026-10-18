package com.portfolio.product.ui;

import com.portfolio.product.exception.ProductException;
import com.portfolio.product.model.Product;
import com.portfolio.product.service.ProductService;

import java.util.List;
import java.util.Scanner;

public class ConsoleUI {
    private final ProductService productService;
    private final Scanner scanner;

    public ConsoleUI(ProductService productService) {
        this.productService = productService;
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        productService.loadSampleData();
        boolean running = true;

        while (running) {
            printMenu();
            String choice = scanner.nextLine().trim();

            try {
                switch (choice) {
                    case "1":
                        listAllProducts();
                        break;
                    case "2":
                        addProduct();
                        break;
                    case "3":
                        updateProduct();
                        break;
                    case "4":
                        deleteProduct();
                        break;
                    case "5":
                        searchProducts();
                        break;
                    case "6":
                        filterAndSortProducts();
                        break;
                    case "7":
                        manageInventory();
                        break;
                    case "0":
                        running = false;
                        System.out.println("Exiting application. Goodbye!");
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            } catch (ProductException e) {
                System.out.println("[Error] " + e.getMessage());
            } catch (Exception e) {
                System.out.println("[Unexpected Error] " + e.getMessage());
            }
            System.out.println();
        }
    }

    private void printMenu() {
        System.out.println("========================================");
        System.out.println("   PRODUCT MANAGEMENT SYSTEM (CLI)     ");
        System.out.println("========================================");
        System.out.println("1. View All Products");
        System.out.println("2. Add New Product");
        System.out.println("3. Update Product");
        System.out.println("4. Delete Product");
        System.out.println("5. Search Products by Name/Category");
        System.out.println("6. Filter & Sort Products");
        System.out.println("7. Inventory & Stock Management");
        System.out.println("0. Exit");
        System.out.print("Enter your choice: ");
    }

    private void listAllProducts() {
        List<Product> products = productService.getAllProducts();
        displayProductTable(products);
    }

    private void addProduct() {
        System.out.print("Enter product name: ");
        String name = scanner.nextLine();
        System.out.print("Enter category: ");
        String category = scanner.nextLine();
        System.out.print("Enter price: ");
        double price = parseDoubleInput();
        System.out.print("Enter initial stock quantity: ");
        int stock = parseIntInput();

        Product created = productService.addProduct(name, category, price, stock);
        System.out.println("Successfully added product: " + created);
    }

    private void updateProduct() {
        System.out.print("Enter product ID to update: ");
        Long id = parseLongInput();

        System.out.print("Enter new product name: ");
        String name = scanner.nextLine();
        System.out.print("Enter new category: ");
        String category = scanner.nextLine();
        System.out.print("Enter new price: ");
        double price = parseDoubleInput();
        System.out.print("Enter new stock quantity: ");
        int stock = parseIntInput();

        productService.updateProduct(id, name, category, price, stock);
        System.out.println("Product ID " + id + " successfully updated.");
    }

    private void deleteProduct() {
        System.out.print("Enter product ID to delete: ");
        Long id = parseLongInput();

        productService.deleteProduct(id);
        System.out.println("Product ID " + id + " successfully deleted.");
    }

    private void searchProducts() {
        System.out.print("Enter search keyword (name or category): ");
        String keyword = scanner.nextLine();
        List<Product> results = productService.searchProducts(keyword);
        displayProductTable(results);
    }

    private void filterAndSortProducts() {
        System.out.print("Filter by category (leave blank for all): ");
        String category = scanner.nextLine();

        List<Product> products = productService.filterByCategory(category);

        System.out.print("Sort by (name, price, stock): ");
        String sortBy = scanner.nextLine();
        System.out.print("Ascending? (true/false): ");
        boolean ascending = Boolean.parseBoolean(scanner.nextLine());

        List<Product> sorted = productService.sortProducts(products, sortBy, ascending);
        displayProductTable(sorted);
    }

    private void manageInventory() {
        System.out.print("Enter product ID for stock adjustment: ");
        Long id = parseLongInput();
        System.out.print("Enter quantity to add (positive) or remove (negative): ");
        int change = parseIntInput();

        productService.adjustStock(id, change);
        System.out.println("Stock updated successfully for product ID " + id);
    }

    private void displayProductTable(List<Product> products) {
        if (products.isEmpty()) {
            System.out.println("No products found.");
            return;
        }
        System.out.println("----------------------------------------------------------------------------------");
        System.out.printf("%-5s | %-25s | %-15s | %-10s | %-8s | %-20s\n", "ID", "Name", "Category", "Price", "Stock", "Created At");
        System.out.println("----------------------------------------------------------------------------------");
        for (Product p : products) {
            System.out.printf("%-5d | %-25s | %-15s | $%-9.2f | %-8d | %-20s\n",
                    p.getId(),
                    p.getName().length() > 25 ? p.getName().substring(0, 22) + "..." : p.getName(),
                    p.getCategory(),
                    p.getPrice(),
                    p.getStockQuantity(),
                    p.getCreatedAt() != null ? p.getCreatedAt().toString() : "N/A"
            );
        }
        System.out.println("----------------------------------------------------------------------------------");
    }

    private double parseDoubleInput() {
        while (true) {
            try {
                return Double.parseDouble(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.print("Invalid number. Please enter a valid decimal price: ");
            }
        }
    }

    private int parseIntInput() {
        while (true) {
            try {
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.print("Invalid number. Please enter a valid integer: ");
            }
        }
    }

    private Long parseLongInput() {
        while (true) {
            try {
                return Long.parseLong(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.print("Invalid ID. Please enter a valid integer ID: ");
            }
        }
    }
}
