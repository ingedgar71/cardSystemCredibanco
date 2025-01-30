package com.credibanco.tarjetas.service;

import com.credibanco.tarjetas.dto.BalanceCard;
import com.credibanco.tarjetas.dto.Card;
import com.credibanco.tarjetas.dto.card.RequestCreateCard;
import com.credibanco.tarjetas.persistencia.model.CardEntity;
import com.credibanco.tarjetas.persistencia.repository.CardJpaRepository;
import com.credibanco.tarjetas.util.DateUtil;
import com.credibanco.tarjetas.util.aleatory.RandomNumberGenerator;
import com.credibanco.tarjetas.util.operBigDecimal.AddBigDecimal;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;
import java.time.Year;

@Service
public class CardService {

    public final CardJpaRepository cardJpaRepository;

    public CardService(CardJpaRepository cardJpaRepository) {
        this.cardJpaRepository = cardJpaRepository;
    }

    public String createCard(RequestCreateCard card){
        CardEntity cardEntity = new CardEntity();
        String numberCard;
        CardEntity cardEntity1;

        //se valida que no exista ya una tarjeta con el mismo numero
        do{
            numberCard = card.getProductId().concat(RandomNumberGenerator.generateNumber(10));
            cardEntity1 = cardJpaRepository.findByCardNumber(numberCard);
        }while(cardEntity1 != null);

        //Numero de tarjeta
        cardEntity.setCardNumber(numberCard);
        //Nombre del cliente
        cardEntity.setHolderName(card.getHolderName());
        //Fecha de expiración de la nueva tarjeta
        LocalDate expirationDate = DateUtil.getPlusYears(LocalDate.now(), 3);
        cardEntity.setExpirationDate(expirationDate);

        cardEntity1 =  cardJpaRepository.save(cardEntity);

        return cardEntity1.getCardNumber();
    }

    public void activateCard(String cardId){

        if(cardId.length() != 16){
            throw new IllegalArgumentException("cardId no valido");
        }
        CardEntity cardEntity = cardJpaRepository.findByCardNumber(cardId);

        if(cardEntity != null){
            cardEntity.setActive(true);
            cardJpaRepository.save(cardEntity);
        }
    }

    public void blockCard(String cardId){

        if(cardId.length() != 16){
            throw new IllegalArgumentException("cardId no valido");
        }
        CardEntity cardEntity = cardJpaRepository.findByCardNumber(cardId);

        if(cardEntity != null){
            cardEntity.setBlocked(true);
            cardJpaRepository.save(cardEntity);
        }
    }

    public CardEntity findByIdCard(Long idCard){
        CardEntity cardEntity = cardJpaRepository.findByIdCard(idCard);
        return cardEntity;
    }
    /**
     * Recargar tarjeta
     * */
    public CardEntity rechargeCard(String cardId, BigDecimal balance){

        if(cardId.length() != 16){
            throw new IllegalArgumentException("cardId no valido");
        }

        if(balance.compareTo(BigDecimal.ZERO) < 0){
            throw new IllegalArgumentException("El monto a recargar no valido");
        }

        CardEntity cardEntity = cardJpaRepository.findByCardNumber(cardId);

        if(cardEntity == null){
            throw new IllegalArgumentException("Tarjeta no existe");
        }

        if(cardEntity.getBlocked()){
            throw new IllegalArgumentException("la tarjeta se encuentra bloqueada");
        }

        if(!cardEntity.getActive()){
            throw new IllegalArgumentException("la tarjeta no se encuentra activada");
        }

        if(cardEntity.getExpirationDate().isBefore(LocalDate.now()) ){
            throw new IllegalArgumentException("la tarjeta esta expirada");
        }

        BigDecimal totalBalance = AddBigDecimal.execute(cardEntity.getBalance(), balance);
        cardEntity.setBalance(totalBalance);
        return cardJpaRepository.save(cardEntity);

    }

    public BalanceCard checkBalance(String cardId){

        if(cardId.length() != 16){
            throw new IllegalArgumentException("cardId no valido");
        }
        CardEntity cardEntity = cardJpaRepository.findByCardNumber(cardId);

        BalanceCard balanceCard = new BalanceCard();
        balanceCard.setCardId(cardEntity.getCardNumber());
        balanceCard.setBalance(cardEntity.getBalance());

        return balanceCard;


    }
}
