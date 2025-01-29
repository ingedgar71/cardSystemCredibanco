package com.credibanco.tarjetas.service;

import com.credibanco.tarjetas.persistencia.model.CardEntity;
import com.credibanco.tarjetas.persistencia.repository.CardJpaRepository;
import org.springframework.stereotype.Service;

@Service
public class CardService {

    public final CardJpaRepository cardJpaRepository;

    public CardService(CardJpaRepository cardJpaRepository) {
        this.cardJpaRepository = cardJpaRepository;
    }

    public CardEntity findByIdCard(Long idCard){
        CardEntity cardEntity = cardJpaRepository.findByIdCard(idCard);
        return cardEntity;
    }
}
