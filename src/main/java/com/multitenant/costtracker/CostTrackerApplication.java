package com.multitenant.costtracker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = "com.multitenant.costtracker")
@EntityScan(basePackages = "com.multitenant.costtracker.entity")
@EnableJpaRepositories(basePackages = "com.multitenant.costtracker.repository")
public class CostTrackerApplication {

    public static void main(String[] args) {
        SpringApplication.run(CostTrackerApplication.class, args);
    }
}
