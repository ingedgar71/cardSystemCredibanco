package com.credibanco.tarjetas.dto.transaction;

import jakarta.validation.constraints.NotBlank;

import java.math.BigDecimal;

public class RequestPurchase {
    @NotBlank(message = "El cardId no puede estar vacío")
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
