package com.lanh.coin.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.math.BigDecimal;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CoinDtoResponse {
    private Object image;
    private String symbol;
    private String name;
    private BigDecimal price_change_percentage_24h;
    private BigDecimal current_price;
    private String description;
    private String trade_url;
}
