package com.credibanco.tarjetas.dto;

import java.math.BigDecimal;

public class BalanceCard {
    private String cardId;
    private BigDecimal balance;

    public String getCardId() {
        return cardId;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
}
