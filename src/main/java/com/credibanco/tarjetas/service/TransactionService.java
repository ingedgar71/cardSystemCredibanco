package com.credibanco.tarjetas.service;

import com.credibanco.tarjetas.dto.transaction.RequestCancelTransaction;
import com.credibanco.tarjetas.dto.transaction.ResponseCheckTransaction;
import com.credibanco.tarjetas.persistencia.model.CardEntity;
import com.credibanco.tarjetas.persistencia.model.TransactionEntity;
import com.credibanco.tarjetas.persistencia.repository.CardJpaRepository;
import com.credibanco.tarjetas.persistencia.repository.TransactionJpaRepository;
import com.credibanco.tarjetas.util.aleatory.RandomNumberGenerator;
import com.credibanco.tarjetas.util.operbigdecimal.AddBigDecimal;
import com.credibanco.tarjetas.util.operbigdecimal.SubtractBigDecimal;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
public class TransactionService {

    public final TransactionJpaRepository transactionJpaRepository;
    public final CardJpaRepository cardJpaRepository;
    public static final String TARJETA_NO_EXISTE = "Tarjeta no existe.";

    public TransactionService(TransactionJpaRepository transactionJpaRepository, CardJpaRepository cardJpaRepository) {
        this.transactionJpaRepository = transactionJpaRepository;
        this.cardJpaRepository = cardJpaRepository;
    }

    @Transactional
    public String makePurchase(String cardId, BigDecimal price) {

        if (cardId.length() != 16) {
            throw new IllegalArgumentException("cardId no valido");
        }

        if (price == null || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("El precio no puede nulo o menor a 0");
        }

        CardEntity cardEntity = cardJpaRepository.findByCardNumber(cardId);

        if (cardEntity == null) {
            throw new IllegalArgumentException(TARJETA_NO_EXISTE);
        }

        if (cardEntity.getBlocked().booleanValue()) {
            throw new IllegalArgumentException("la tarjeta se encuentra bloqueada");
        }

        if (!cardEntity.getActive().booleanValue()) {
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
        TransactionEntity transaction = transactionJpaRepository.save(transactionEntity);
        return transaction.getTransactionNumber();
    }

    public ResponseCheckTransaction checkTransaction(String transactionId, String cardId) {
        TransactionEntity transactionEntity = transactionJpaRepository.findByTransactionNumberAndCardEntityCardNumber(transactionId, cardId);

        if(transactionEntity == null){
            throw new IllegalArgumentException(TARJETA_NO_EXISTE);
        }

        ResponseCheckTransaction transaction1 = new ResponseCheckTransaction();

        transaction1.setCardId(transactionEntity.getCardEntity().getCardNumber());
        transaction1.setTransactionDate(transactionEntity.getTimestamp());
        transaction1.setAmount(transactionEntity.getAmount());
        transaction1.setCancelled(transactionEntity.getCancelled());
        return transaction1;
    }
    @Transactional
    public void cancelTransaction(RequestCancelTransaction transaction) {

        TransactionEntity transactionEntity = transactionJpaRepository.findByTransactionNumberAndCardEntityCardNumber(transaction.getTransactionId(), transaction.getCardId());
        //valida si la transacción existe
        if(transactionEntity == null){
            throw new IllegalArgumentException("La transacción que se desea anular no existe.");
        }

        //valida que la transacción no este cancelada o anulada
        if(transactionEntity.getCancelled().booleanValue()){
            throw new IllegalArgumentException("La transacción ya se encuentra cancelada.");
        }

        LocalDateTime transactionExpiration = transactionEntity.getTimestamp().plusHours(24);
        //valida si la transacción tiene más de 24 horas de realizada
        if(transactionExpiration.isBefore(LocalDateTime.now())){
            throw new IllegalArgumentException("Fecha de transacción es mayor a 24 horas");
        }

        //retornar el valor de la transacción al balance de la tarjeta
        transactionEntity.getCardEntity().setBalance(AddBigDecimal.execute(transactionEntity.getCardEntity().getBalance(),transactionEntity.getAmount())) ;

        cardJpaRepository.save(transactionEntity.getCardEntity());

        transactionEntity.setCancelled(true);

        transactionJpaRepository.save(transactionEntity);
    }

}
