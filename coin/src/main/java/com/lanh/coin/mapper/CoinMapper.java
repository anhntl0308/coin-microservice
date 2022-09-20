package com.lanh.coin.mapper;

import com.lanh.coin.dto.CoinDtoResponse;
import com.lanh.coin.models.CoinDetailResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CoinMapper {
    @Mapping(target = "description", ignore = true)
    CoinDtoResponse toCoinDtoResponse(CoinDetailResponse coinDetailResponse);

    List<CoinDtoResponse> toListCoinDtoResponse(List<CoinDetailResponse> coinDetailResponses);
}
