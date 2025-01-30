package com.credibanco.tarjetas.persistencia.repository;

import com.credibanco.tarjetas.persistencia.model.TransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionJpaRepository extends JpaRepository<TransactionEntity,Long> {

    TransactionEntity findByTransactionNumber(String transactionNumber);
    TransactionEntity findByTransactionNumberAndCardEntityIdCard(String transactionNumber, Long idCard);
}
