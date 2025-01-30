package com.credibanco.tarjetas.persistencia.model;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name="card")
public class CardEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id_card")
    private Long idCard;
    //Número de tarjeta unico
    @Column(name="card_number", length = 16, nullable = false)
    private String cardNumber;
//    @Column(name="product_id", length = 6, nullable = false)
//    private String productId;
    //nombre del titular
    @Column(name="holder_name", length = 100,nullable = false)
    private String holderName;
    //Fecha de vencimiento mm/yyyy
    @Column(name="expiration_date", nullable = false)
    private LocalDate expirationDate;
    //saldo disponible
    @Column(name="balance", precision = 12,scale = 2)
    private BigDecimal balance = BigDecimal.ZERO;
    //estado de activacion
    @Column(name="is_active",nullable = false)
    private Boolean isActive = false;
    //estado de bloqueo
    @Column(name="is_blocked",nullable = false)
    private Boolean isBlocked = false;
    //Fecha de creación
    @Column(name="created_at", updatable = false)
    private LocalDateTime createdAt;
    //Fecha de actualización
    @Column(name="updated_at", updatable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate(){
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate(){
        this.updatedAt = LocalDateTime.now();
    }


    //Getters and Setters


    public Long getIdCard() {
        return idCard;
    }

    public void setIdCard(Long idCard) {
        this.idCard = idCard;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String number) {
        this.cardNumber = number;
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

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }


}
