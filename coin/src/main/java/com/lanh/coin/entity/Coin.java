package com.lanh.coin.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigDecimal;
@Data
@Entity
public class Coin {
    @Id
    private String id;
    @Column(columnDefinition = "varchar(2000)")
    private String image;
    private String symbol;
    @Column(columnDefinition = "varchar(2000)")
    private String name;
    private BigDecimal price_change_percentage_24h;
    private BigDecimal current_price;
    @Column(columnDefinition = "text")
    private String description;
    @Column(columnDefinition = "varchar(2000)")
    private String trade_url;
}
