package com.lanh.coin.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponseDto<T> {
    private String status;
    private String message;
    private T data;
}
