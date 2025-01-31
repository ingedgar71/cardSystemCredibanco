package com.credibanco.tarjetas.controller;


import com.credibanco.tarjetas.dto.BalanceCard;
import com.credibanco.tarjetas.dto.EnrollCard;
import com.credibanco.tarjetas.dto.card.RequestCreateCard;
import com.credibanco.tarjetas.service.CardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/card")
@Tag(name = "Tarjetas", description = "Gestión de Tarjetas de Crédito o Débito")
public class CardController {

    private final CardService cardService;

    public CardController(CardService cardService) {
        this.cardService = cardService;
    }

    @Operation(summary = "Crear una tarjeta", description = "Retorna el numero de la tarjeta generada")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "La tarjeta fue creada correctamente"),
    })
    @PostMapping("/number")
    public ResponseEntity<String> createCard(@RequestBody RequestCreateCard card){
        String numberCard = cardService.createCard(card);
        return new ResponseEntity<>(numberCard,HttpStatus.CREATED);
    }

    @Operation(summary = "Activar una tarjeta", description = "Cambia el estado de activación de tarjeta a True")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "La tarjeta fue activada con éxito"),

    })
    @PatchMapping("/enroll")
    public void activateCard(@RequestBody EnrollCard enrollCard) {
        cardService.activateCard(enrollCard.getCardId());
    }

    @Operation(summary = "Bloquear una tarjeta", description = "Cambia el estado de bloqueo de tarjeta a True")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "La tarjeta fue bloqueada con éxito"),
    })
    @PatchMapping("/{cardId}")
    public void blockCard(@PathVariable("cardId") String cardId) {
        cardService.blockCard(cardId);
    }

    @Operation(summary = "Recargar con dinero una tarjeta", description = "Permite recargar con dinero una tarjeta existente")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "La tarjeta fue recargada con éxito"),
    })
    @PatchMapping("/balance")
    public void rechargeCard(@RequestBody BalanceCard balanceCard) {
        cardService.rechargeCard(balanceCard.getCardId(), balanceCard.getBalance());
    }
    @Operation(summary = "Consultar el saldo de una tarjeta", description = "Permite consultar el saldo de una tarjeta existente")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "La tarjeta fue consultada con éxito"),
    })
    @GetMapping("/balance/{cardId}")
    public ResponseEntity<BalanceCard> checkAccountBalance(@PathVariable("cardId") String cardId) {
        BalanceCard balanceCard = cardService.checkBalance(cardId);
        return ResponseEntity.ok(balanceCard);
    }


}
