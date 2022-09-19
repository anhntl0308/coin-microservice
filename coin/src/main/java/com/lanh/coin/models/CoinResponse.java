package com.lanh.coin.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CoinResponse {
    private String id;
    private String symbol;
    private String name;
    private Object image;
    private BigDecimal current_price;
    private BigDecimal price_change_percentage_24h;
    private Description description;
    private ArrayList<Tickers> tickers;

    @Data
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Description{
        private String en;
    }

    @Data
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Tickers{
        private String trade_url;
    }
}
