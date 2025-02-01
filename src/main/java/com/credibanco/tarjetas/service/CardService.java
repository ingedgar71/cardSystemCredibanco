package com.credibanco.tarjetas.service;

import com.credibanco.tarjetas.dto.card.BalanceCard;
import com.credibanco.tarjetas.dto.card.CreateCardRequest;
import com.credibanco.tarjetas.persistencia.model.CardEntity;
import com.credibanco.tarjetas.persistencia.repository.CardJpaRepository;
import com.credibanco.tarjetas.util.DateUtil;
import com.credibanco.tarjetas.util.aleatory.RandomNumberGenerator;
import com.credibanco.tarjetas.util.operbigdecimal.AddBigDecimal;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;

@Service
public class CardService {

    private final CardJpaRepository cardJpaRepository;
    private static final String NUMERO_DE_DIGITOS_INVALIDO = "Numéro de digitos de cardId no valido";
    private static final String TARJETA_NO_EXISTE = "Tarjeta no existe.";

    public CardService(CardJpaRepository cardJpaRepository) {
        this.cardJpaRepository = cardJpaRepository;
    }

    public String createCard(CreateCardRequest card) {
        CardEntity cardEntity = new CardEntity();
        String numberCard;
        CardEntity cardEntity1;

        //se valida que no exista ya una tarjeta con el mismo numero
        do {
            numberCard = card.getProductId().concat(RandomNumberGenerator.generateNumber(10));
            cardEntity1 = cardJpaRepository.findByCardNumber(numberCard);
        } while (cardEntity1 != null);

        //Numero de tarjeta
        cardEntity.setCardNumber(numberCard);
        //Nombre del cliente
        cardEntity.setHolderName(card.getHolderName());
        //Fecha de expiración de la nueva tarjeta
        LocalDate expirationDate = DateUtil.getPlusYears(LocalDate.now(), 3);
        cardEntity.setExpirationDate(expirationDate);

        cardEntity1 = cardJpaRepository.save(cardEntity);

        return cardEntity1.getCardNumber();
    }

    //Activar Tarjeta
    public void activateCard(String cardId) {

        if (cardId.length() != 16) {
            throw new IllegalArgumentException(NUMERO_DE_DIGITOS_INVALIDO);
        }
        CardEntity cardEntity = cardJpaRepository.findByCardNumber(cardId);

        if (cardEntity == null) {
            throw new IllegalArgumentException(TARJETA_NO_EXISTE);
        }

        cardEntity.setActive(true);
        cardJpaRepository.save(cardEntity);

    }

    //Bloquear tarjeta
    public void blockCard(String cardId) {

        if (cardId.length() != 16) {
            throw new IllegalArgumentException(NUMERO_DE_DIGITOS_INVALIDO);
        }

        CardEntity cardEntity = cardJpaRepository.findByCardNumber(cardId);

        if (cardEntity == null) {
            throw new IllegalArgumentException(TARJETA_NO_EXISTE);
        }

        cardEntity.setBlocked(true);
        cardJpaRepository.save(cardEntity);

    }


    /**
     * Recargar tarjeta
     */
    public void rechargeCard(String cardId, BigDecimal balance) {

        if (cardId.length() != 16) {
            throw new IllegalArgumentException(NUMERO_DE_DIGITOS_INVALIDO);
        }

        if (balance.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("El monto a recargar no valido");
        }

        CardEntity cardEntity = cardJpaRepository.findByCardNumber(cardId);

        if (cardEntity == null) {
            throw new IllegalArgumentException("TARJETA_NO_EXISTE");
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

        BigDecimal totalBalance = AddBigDecimal.execute(cardEntity.getBalance(), balance);
        cardEntity.setBalance(totalBalance);
        cardJpaRepository.save(cardEntity);
    }

    public BalanceCard checkBalance(String cardId) {

        if (cardId.length() != 16) {
            throw new IllegalArgumentException(NUMERO_DE_DIGITOS_INVALIDO);
        }
        CardEntity cardEntity = cardJpaRepository.findByCardNumber(cardId);

        if (cardEntity == null) {
            throw new IllegalArgumentException(TARJETA_NO_EXISTE);
        }

        BalanceCard balanceCard = new BalanceCard();
        balanceCard.setBalance(cardEntity.getBalance());

        return balanceCard;
    }
}
