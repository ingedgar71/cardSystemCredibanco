package com.credibanco.tarjetas.dto.transaction;

import com.credibanco.tarjetas.exception.GlobalExceptionHandler;

public class RequestCheckTransaction extends GlobalExceptionHandler {
    private String cardId;
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
