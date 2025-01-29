package com.credibanco.tarjetas.controller;


import com.credibanco.tarjetas.persistencia.model.CardEntity;
import com.credibanco.tarjetas.service.CardService;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
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
}
