package com.lanh.coin.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CoinDtoRequest {
    private String currency;
    private Integer page;
    private Integer per_page;
}
