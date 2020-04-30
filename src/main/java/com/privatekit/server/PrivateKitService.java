package com.privatekit.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = { "com.privatekit" })
@EntityScan(basePackages={"com.privatekit.server.entity"})
@EnableJpaRepositories(basePackages={"com.privatekit.server.repository"})
public class PrivateKitService {
    public static void main(String[] args) {
        SpringApplication.run(PrivateKitService.class, args);
    }
}
