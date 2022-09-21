package com.lanh.coin.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lanh.coin.config.CoinGeckoConfig;
import com.lanh.coin.constant.CoinConstant;
import com.lanh.coin.dto.CoinDtoRequest;
import com.lanh.coin.dto.CoinDtoResponse;
import com.lanh.coin.entity.Coin;
import com.lanh.coin.mapper.CoinMapper;
import com.lanh.coin.models.CoinDetailResponse;
import com.lanh.coin.models.CoinRequest;
import com.lanh.coin.models.CoinResponse;
import com.lanh.coin.repository.CoinRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.UnknownHttpStatusCodeException;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
@Slf4j
public class CoinService {
    private final RestTemplate restTemplate;
    private final CoinGeckoConfig coinGeckoConfig;
    private final ObjectMapper objectMapper;
    private final CoinMapper coinMapper;
    private final CoinRepository coinRepository;

    public List<CoinDtoResponse> getAll(CoinDtoRequest request) {

        validateCoinRequest(request);

        CoinRequest coinRequest = buildCoinRequest(request);

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);

        Map<String, String> params = new HashMap<>();
        params.put("vs_currency", coinRequest.getVs_currency());
        params.put("page", coinRequest.getPage());
        params.put("per_page", coinRequest.getPer_page());

        String coinUrl = coinGeckoConfig.getBaseUrl() + CoinConstant.PathUrl.LIST_ALL;

        String urlTemplate = UriComponentsBuilder.fromHttpUrl(coinUrl)
                .queryParam("vs_currency", "{vs_currency}")
                .queryParam("page", "{page}")
                .queryParam("per_page", "{per_page}")
                .encode()
                .toUriString();

        try {
            ResponseEntity<String> responseEntity = restTemplate.exchange(urlTemplate, HttpMethod.GET, new HttpEntity<>(null, headers), String.class, params);
            List<CoinResponse> coinResponses = objectMapper.readValue(responseEntity.getBody(), new TypeReference<List<CoinResponse>>() {
            });

            return coinResponses.stream().map((item) -> {
                CoinDtoResponse response = getCoin(item.getId());
                response.setCurrent_price(item.getCurrent_price());
                response.setPrice_change_percentage_24h(item.getPrice_change_percentage_24h());
                Coin coin = new Coin();
                coin.setId(new Coin.CoinId(item.getId(), request.getCurrency().toUpperCase()));
                try {
                    coin.setData(objectMapper.writeValueAsString(response));
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
                coinRepository.save(coin);
                return response;
            }).collect(Collectors.toList());


        } catch (HttpClientErrorException | HttpServerErrorException | UnknownHttpStatusCodeException |
                 IllegalArgumentException e) {
            log.error("Error call api coinGecko Server: {}", e.getMessage());
            return findCoinByCurrency(request);
        } catch (Exception e) {
            log.error("Error call api coinGecko: {}", e.getMessage());
            throw new RuntimeException();
        }
    }

    private List<CoinDtoResponse> findCoinByCurrency(CoinDtoRequest request) {
        Pageable pageable = PageRequest.of(request.getPage() - 1, request.getPer_page());
        List<Coin> coins = coinRepository.findAllByIdCurrency(request.getCurrency().toUpperCase(), pageable);
        return coins.stream().map(item -> {
            try {
                return objectMapper.readValue(item.getData(), CoinDtoResponse.class);
            } catch (JsonProcessingException ex) {
                log.error("Error call api coinGecko: {}", ex.getMessage());
                throw new RuntimeException(ex);
            }
        }).collect(Collectors.toList());
    }

    public CoinDtoResponse getCoin(String coin) {
        if (StringUtils.isEmpty(coin)) {
            throw new RuntimeException("Id coin không được để trống và nhỏ hơn 0");
        }
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);

        Map<String, String> params = new HashMap<>();
        params.put("coin", coin);

        String coinUrl = coinGeckoConfig.getBaseUrl() + "/{coin}";

        try {
            ResponseEntity<String> responseEntity = restTemplate.exchange(coinUrl, HttpMethod.GET, new HttpEntity<>(null, headers), String.class, params);
            CoinDetailResponse coinDetailResponses = objectMapper.readValue(responseEntity.getBody(), CoinDetailResponse.class);
            CoinDtoResponse coinDtoResponse = coinMapper.toCoinDtoResponse(coinDetailResponses);
            coinDtoResponse.setDescription(coinDetailResponses.getDescription().getEn());
            coinDtoResponse.setTrade_url(coinDetailResponses.getTickers().get(0).getTrade_url());
            return coinDtoResponse;
        } catch (HttpClientErrorException e) {
            log.error("Error call api coinGecko: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Error call api coinGecko: {}", e.getMessage());
            throw new RuntimeException();
        }
    }

    private CoinRequest buildCoinRequest(CoinDtoRequest request) {
        CoinRequest coinRequest = new CoinRequest();
        coinRequest.setVs_currency(request.getCurrency());
        coinRequest.setPage(request.getPage() == null ? "" : String.valueOf(request.getPage()));
        coinRequest.setPer_page(request.getPage() == null ? "" : String.valueOf(request.getPer_page()));
        return coinRequest;
    }

    private void validateCoinRequest(CoinDtoRequest request) {
        if (request.getCurrency() == null) {
            throw new RuntimeException("Xin vui lòng nhập currency");
        }
    }
}
