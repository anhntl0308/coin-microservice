package com.lanh.coin.exception;

import com.lanh.coin.dto.ErrorResponseDto;
import org.apache.commons.lang.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;

@ControllerAdvice
public class CoinExceptionController {

    @ExceptionHandler(value = RuntimeException.class)
    public ResponseEntity<Object> runtimeException(RuntimeException exception) {
        ErrorResponseDto<String> responseDto = new ErrorResponseDto<>();
        responseDto.setStatus("failed");
        responseDto.setMessage(!StringUtils.isEmpty(exception.getMessage()) ? exception.getMessage() : "Có lỗi xảy ra! xin vui lòng thử lại sau.");
        return new ResponseEntity<>(responseDto, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = HttpClientErrorException.class)
    public ResponseEntity<Object> httpClientErrorException(HttpClientErrorException exception) {
        ErrorResponseDto<String> responseDto = new ErrorResponseDto<>();
        responseDto.setStatus(String.valueOf(exception.getStatusCode()));
        responseDto.setMessage(!StringUtils.isEmpty(exception.getMessage()) ? exception.getMessage() : "Có lỗi xảy ra! xin vui lòng thử lại sau.");
        return new ResponseEntity<>(responseDto, HttpStatus.BAD_REQUEST);
    }
}
