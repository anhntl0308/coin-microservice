package com.lanh.coin.repository;

import com.lanh.coin.entity.Coin;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CoinRepository extends JpaRepository<Coin, Coin.CoinId> {
    List<Coin> findAllByIdCurrency(String currency, Pageable pageable);
}
