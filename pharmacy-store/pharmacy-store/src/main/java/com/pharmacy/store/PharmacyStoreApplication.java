package com.pharmacy.store;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EntityScan(basePackages = "com.pharmacy.store.model")
@EnableJpaRepositories(basePackages = "com.pharmacy.store.repository")
@EnableTransactionManagement
public class PharmacyStoreApplication {

    public static void main(String[] args) {
        SpringApplication.run(PharmacyStoreApplication.class, args);
        System.out.println("=================================");
        System.out.println("Pharmacy Store Application Started!");
        System.out.println("Access the application at: http://localhost:8080");
        System.out.println("H2 Database Console: http://localhost:8080/h2-console");
        System.out.println("=================================");
    }
}