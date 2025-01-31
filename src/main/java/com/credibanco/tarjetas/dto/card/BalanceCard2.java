package com.credibanco.tarjetas.dto.card;

import java.math.BigDecimal;

public class BalanceCard2 {

    private BigDecimal balance;
    private String cardId;

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public String getCardId() {
        return cardId;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
    }
}
