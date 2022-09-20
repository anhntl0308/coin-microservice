package com.lanh.coin.mapper;

import com.lanh.coin.dto.CoinDtoResponse;
import com.lanh.coin.entity.Coin;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CoinMapper {
    @Mapping(target = "image", ignore = true)
    Coin toCoin(CoinDtoResponse coinDtoResponse);
}
