package com.credibanco.tarjetas.dto.card;

import jakarta.validation.constraints.NotBlank;

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
