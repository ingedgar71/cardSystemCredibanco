package com.credibanco.tarjetas.controller;


import com.credibanco.tarjetas.dto.Card;
import com.credibanco.tarjetas.dto.EnrollCard;
import com.credibanco.tarjetas.dto.BalanceCard;
import com.credibanco.tarjetas.dto.card.RequestCreateCard;
import com.credibanco.tarjetas.persistencia.model.CardEntity;
import com.credibanco.tarjetas.service.CardService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/card")
public class CardController {

    private final CardService cardService;

    public CardController(CardService cardService) {
        this.cardService = cardService;
    }

    @GetMapping("/findByIdCard/{idcard}")
    public ResponseEntity<CardEntity> findByIdCard(@PathVariable("idcard") Long idCard) {
        CardEntity cardEntity = cardService.findByIdCard(idCard);

        if (cardEntity != null) {
            return new ResponseEntity<>(cardEntity, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

    }

    @PostMapping("/number")
    public ResponseEntity<String> createCard(@RequestBody RequestCreateCard card){
        String numberCard = cardService.createCard(card);
        return new ResponseEntity<>(numberCard,HttpStatus.CREATED);
    }

    @PatchMapping("/enroll")
    public void activateCard(@RequestBody EnrollCard enrollCard) {
        cardService.activateCard(enrollCard.getCardId());
    }

    @PatchMapping("/{cardId}")
    public void blockCard(@PathVariable("cardId") String cardId) {
        cardService.blockCard(cardId);
    }

    //Recargar Saldo
    @PatchMapping("/balance")
    public void rechargeCard(@RequestBody BalanceCard balanceCard) {
        cardService.rechargeCard(balanceCard.getCardId(), balanceCard.getBalance());
    }
    //Consultar Saldo
    @GetMapping("/balance/{cardId}")
    public ResponseEntity<BalanceCard> checkAccountBalance(@PathVariable("cardId") String cardId) {
        BalanceCard balanceCard = cardService.checkBalance(cardId);
        return ResponseEntity.ok(balanceCard);
    }


}
