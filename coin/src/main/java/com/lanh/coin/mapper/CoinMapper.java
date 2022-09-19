package com.lanh.coin.mapper;

import com.lanh.coin.dto.CoinDtoResponse;
import com.lanh.coin.models.CoinResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CoinMapper {
    @Mapping(target = "description", ignore = true)
    @Mapping(target = "tickers", ignore = true)
    CoinDtoResponse toCoinDtoResponse(CoinResponse coinResponse);
    List<CoinDtoResponse> toListCoinDtoResponse(List<CoinResponse> coinResponse);
}
