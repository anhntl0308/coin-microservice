package com.lanh.coin.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import java.io.Serializable;

@Data
@Entity
public class Coin {
    @EmbeddedId
    private CoinId id;
    @Column(columnDefinition = "text")
    private String data;

    @Embeddable
   public static class CoinId implements Serializable {
        private static final long serialVersionUID = 1L;

        private String id;
        private String currency;

        public CoinId(String id, String currency) {
            this.id = id;
            this.currency = currency;
        }

        public CoinId() {

        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getCurrency() {
            return currency;
        }

        public void setCurrency(String currency) {
            this.currency = currency;
        }
    }
}


