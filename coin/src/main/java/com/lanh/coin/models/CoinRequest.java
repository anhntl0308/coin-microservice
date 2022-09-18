package com.lanh.coin.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CoinRequest {
    private String vs_currency;
    private String page;
    private String per_page;
}
