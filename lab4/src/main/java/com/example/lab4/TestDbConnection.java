package com.example.lab4;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@Configuration
public class TestDbConnection {
    
    @Bean
    public CommandLineRunner testDatabaseConnection(DataSource dataSource) {
        return args -> {
            System.out.println("Testing database connection...");
            try (Connection connection = dataSource.getConnection()) {
                System.out.println("Database connected successfully!");
                System.out.println("Database: " + connection.getCatalog());
                System.out.println("Connection valid: " + !connection.isClosed());
            } catch (SQLException e) {
                System.err.println("Failed to connect to the database:");
                e.printStackTrace();
            }
        };
    }
} 