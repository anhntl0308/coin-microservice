package com.lanh.coin.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.math.BigDecimal;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CoinResponse {
    private String id;
    private BigDecimal current_price;
    private BigDecimal price_change_percentage_24h;
}
