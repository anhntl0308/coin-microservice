package com.lanh.coin.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CoinDetailResponse {
    private String id;
    private String symbol;
    private String name;
    private Image image;
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

    @Data
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Image{
        private String thumb;
        private String small;
        private String large;
    }
}
