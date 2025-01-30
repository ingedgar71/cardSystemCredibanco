package com.credibanco.tarjetas.service;

import com.credibanco.tarjetas.persistencia.model.CardEntity;
import com.credibanco.tarjetas.persistencia.model.TransactionEntity;
import com.credibanco.tarjetas.persistencia.repository.CardJpaRepository;
import com.credibanco.tarjetas.persistencia.repository.TransactionJpaRepository;
import com.credibanco.tarjetas.util.aleatory.RandomNumberGenerator;
import com.credibanco.tarjetas.util.operBigDecimal.SubtractBigDecimal;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;

@Service
public class TransactionService {

    public final TransactionJpaRepository transactionJpaRepository;
    public final CardJpaRepository cardJpaRepository;

    public TransactionService(TransactionJpaRepository transactionJpaRepository, CardJpaRepository cardJpaRepository) {
        this.transactionJpaRepository = transactionJpaRepository;
        this.cardJpaRepository = cardJpaRepository;
    }

    @Transactional
    public void makePurchase(String cardId, BigDecimal price) {

        if (cardId.length() != 16) {
            throw new IllegalArgumentException("cardId no valido");
        }

        if (price != null && price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("El precio no puede nulo o menor a 0");
        }

        CardEntity cardEntity = cardJpaRepository.findByCardNumber(cardId);

        if (cardEntity == null) {
            throw new IllegalArgumentException("Tarjeta no existe");
        }

        if (cardEntity.getBlocked()) {
            throw new IllegalArgumentException("la tarjeta se encuentra bloqueada");
        }

        if (!cardEntity.getActive()) {
            throw new IllegalArgumentException("la tarjeta no se encuentra activada");
        }

        if (cardEntity.getExpirationDate().isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("la tarjeta esta expirada");
        }

        if (price.compareTo(cardEntity.getBalance()) > 0) {
            throw new IllegalArgumentException("Saldo Insuficiente");
        }

        //Actualizamos saldo de la tarjeta
        BigDecimal newBalance = SubtractBigDecimal.execute(cardEntity.getBalance(), price);
        cardEntity.setBalance(newBalance);
        CardEntity cardEntity1 = cardJpaRepository.save(cardEntity);

        TransactionEntity transactionEntity1;
        String numTransaction;

        //generamos el numero de transacción
        do {
            numTransaction = RandomNumberGenerator.generateNumber(6);
            //validar que el numero de transacción generado no exista para esa misma tarjeta
            transactionEntity1 = transactionJpaRepository.findByTransactionNumberAndCardEntityIdCard(numTransaction, cardEntity.getIdCard());
        } while (transactionEntity1 != null);

        //Creamos transacción
        TransactionEntity transactionEntity = new TransactionEntity();
        transactionEntity.setCardEntity(cardEntity1);
        transactionEntity.setTransactionNumber(numTransaction);
        transactionEntity.setAmount(price);
        transactionJpaRepository.save(transactionEntity);

    }

    public void cancelTransaction(String cardId, String transactionId) {

        TransactionEntity transactionEntity = transactionJpaRepository.findByTransactionNumber(transactionId);
        transactionEntity.setCancelled(true);

        transactionJpaRepository.save(transactionEntity);
    }

}
