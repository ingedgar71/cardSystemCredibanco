package com.credibanco.tarjetas.dto.transaction;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ResponseCheckTransaction {
    private String cardId;
    private BigDecimal amount;
    private LocalDateTime transactionDate;
    private Boolean cancelled;

    public String getCardId() {
        return cardId;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public LocalDateTime getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(LocalDateTime transactionDate) {
        this.transactionDate = transactionDate;
    }

    public Boolean getCancelled() {
        return cancelled;
    }

    public void setCancelled(Boolean cancelled) {
        this.cancelled = cancelled;
    }
}
