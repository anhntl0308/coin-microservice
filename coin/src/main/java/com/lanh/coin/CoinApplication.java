package com.lanh.coin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class CoinApplication {

    public static void main(String[] args) {
        SpringApplication.run(CoinApplication.class, args);
    }

}
