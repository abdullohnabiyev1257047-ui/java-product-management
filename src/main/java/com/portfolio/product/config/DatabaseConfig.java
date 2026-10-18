package com.portfolio.product.config;

import com.portfolio.product.exception.ProductException;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.stream.Collectors;

public class DatabaseConfig {
    private static final String DEFAULT_URL = "jdbc:sqlite:products.db";
    private final String dbUrl;

    public DatabaseConfig() {
        this.dbUrl = DEFAULT_URL;
        initializeDatabase();
    }

    public DatabaseConfig(String dbUrl) {
        this.dbUrl = dbUrl;
        initializeDatabase();
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(dbUrl);
    }

    private void initializeDatabase() {
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            
            InputStream is = getClass().getClassLoader().getResourceAsStream("schema.sql");
            if (is == null) {
                throw new ProductException("schema.sql file not found in resources!");
            }

            String schemaSql;
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
                schemaSql = reader.lines().collect(Collectors.joining("\n"));
            }

            stmt.execute(schemaSql);
        } catch (Exception e) {
            throw new ProductException("Failed to initialize database: " + e.getMessage(), e);
        }
    }
}
