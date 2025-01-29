package com.credibanco.tarjetas.persistencia.model;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name="card")
public class CardEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id_card")
    private Long idCard;

    @Column(name="number", length = 16, nullable = false)
    private String number;

    @Column(name="holder_name", length = 100,nullable = false)
    private String holderName;

    @Column(name="expiration_date", nullable = false)
    private LocalDate expirationDate;

    @Column(name="balance")
    private BigDecimal balance = BigDecimal.ZERO;

    @Column(name="is_active",nullable = false)
    private Boolean isActive = false;

    @Column(name="is_blocked",nullable = false)
    private Boolean isBlocked = false;


    //Getters and Setters


    public Long getIdCard() {
        return idCard;
    }

    public void setIdCard(Long idCard) {
        this.idCard = idCard;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getHolderName() {
        return holderName;
    }

    public void setHolderName(String holderName) {
        this.holderName = holderName;
    }

    public LocalDate getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(LocalDate expirationDate) {
        this.expirationDate = expirationDate;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public Boolean getBlocked() {
        return isBlocked;
    }

    public void setBlocked(Boolean blocked) {
        isBlocked = blocked;
    }
}
