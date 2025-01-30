package com.credibanco.tarjetas.persistencia.repository;

import com.credibanco.tarjetas.persistencia.model.CardEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CardJpaRepository extends JpaRepository<CardEntity, Long> {

    CardEntity findByIdCard(Long id);
    CardEntity findByCardNumber(String cardNumber);
}
