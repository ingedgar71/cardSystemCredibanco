package com.credibanco.tarjetas.dto.transaction;

import jakarta.validation.constraints.NotBlank;

public class RequestCancelTransaction {
    @NotBlank(message = "El cardId no puede estar vacío")
    private String cardId;
    @NotBlank(message = "El transactionId no puede estar vacío")
    private String transactionId;

    public String getCardId() {
        return cardId;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }
}
