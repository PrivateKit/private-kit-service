package com.privatekit.services;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = { "com.privatekit" })
@EntityScan(basePackages={"com.privatekit.services.entity"})
@EnableJpaRepositories(basePackages={"com.privatekit.services.repository"})
public class PrivateKitService {
    public static void main(String[] args) {
        SpringApplication.run(PrivateKitService.class, args);
    }
}
