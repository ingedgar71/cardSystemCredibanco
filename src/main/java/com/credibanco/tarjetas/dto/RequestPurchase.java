package com.credibanco.tarjetas.dto;

import java.math.BigDecimal;

public class RequestPurchase {

    private String cardId;
    private BigDecimal price;

    public String getCardId() {
        return cardId;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
