package com.lanh.coin.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
public class CoinGeckoConfig {
    @Value("${coin_gecko_base_url}")
    private String baseUrl;
}
