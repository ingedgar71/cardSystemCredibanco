package com.credibanco.tarjetas.dto.card;

import java.math.BigDecimal;

public class RechargeBalanceRequest {

    private BigDecimal balance;

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
}
