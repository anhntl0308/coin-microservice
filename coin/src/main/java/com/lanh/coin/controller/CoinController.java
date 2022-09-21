package com.lanh.coin.controller;

import com.lanh.coin.dto.CoinDtoRequest;
import com.lanh.coin.dto.CoinDtoResponse;
import com.lanh.coin.services.CoinService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/coins")
public class CoinController {
    private final CoinService coinService;

    @GetMapping
    public List<CoinDtoResponse> getAllCoins(@RequestBody CoinDtoRequest coinDtoRequest){
        return coinService.getAll(coinDtoRequest);
    }

    @GetMapping("/{coin}")
    public CoinDtoResponse getCoin(@PathVariable String coin){
        return coinService.getCoin(coin);
    }
}
