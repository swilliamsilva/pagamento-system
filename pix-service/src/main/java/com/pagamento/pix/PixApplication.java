package com.pagamento.pix;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class PixApplication {
    public static void main(String[] args) {
        SpringApplication.run(PixApplication.class, args);
    }
}
