package com.lanh.coin.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lanh.coin.config.CoinGeckoConfig;
import com.lanh.coin.constant.CoinConstant;
import com.lanh.coin.dto.CoinDtoRequest;
import com.lanh.coin.dto.CoinDtoResponse;
import com.lanh.coin.mapper.CoinMapper;
import com.lanh.coin.models.CoinRequest;
import com.lanh.coin.models.CoinResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
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
                CoinDtoResponse coinDtoResponse = coinMapper.toCoinDtoResponse(item);
                CoinDtoResponse response = getCoin(item.getId());
                coinDtoResponse.setDescription(response.getDescription());
                coinDtoResponse.setTrade_url(response.getTrade_url());
                return coinDtoResponse;}
            ).collect(Collectors.toList());

        } catch (HttpClientErrorException e) {
            log.error("Error call api coinGecko: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Error call api coinGecko: {}", e.getMessage());
            throw new RuntimeException();
        }
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
            CoinResponse coinResponses = objectMapper.readValue(responseEntity.getBody(), CoinResponse.class);
            CoinDtoResponse coinDtoResponse = coinMapper.toCoinDtoResponse(coinResponses);
            coinDtoResponse.setDescription(coinResponses.getDescription().getEn());
            coinDtoResponse.setTrade_url(coinResponses.getTickers().get(0).getTrade_url());
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
