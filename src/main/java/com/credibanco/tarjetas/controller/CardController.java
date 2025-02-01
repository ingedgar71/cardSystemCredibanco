package com.credibanco.tarjetas.controller;


import com.credibanco.tarjetas.dto.card.BalanceCard;
import com.credibanco.tarjetas.dto.card.CreateCardRequest;
import com.credibanco.tarjetas.dto.card.EnrollCard;
import com.credibanco.tarjetas.dto.card.RechargeBalanceRequest;
import com.credibanco.tarjetas.service.CardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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

    @Operation(summary = "Crear una tarjeta", description = "Genera un número de tarjeta único y lo devuelve en la respuesta.")
    @ApiResponse(responseCode = "201", description = "La tarjeta fue creada correctamente")
    @ApiResponse(responseCode = "400", description = "Solicitud inválida")
    @ApiResponse(responseCode = "500", description = "Error interno del servidor")

    @PostMapping
    public ResponseEntity<String> createCard(
            @RequestBody  CreateCardRequest card) {

        String numberCard = cardService.createCard(card);
        return ResponseEntity.status(HttpStatus.CREATED).body(numberCard);
    }

    @Operation(summary = "Activar una tarjeta", description = "Activa una tarjeta previamente creada.")
    @ApiResponse(responseCode = "200", description = "La tarjeta fue activada con éxito")
    @ApiResponse(responseCode = "400", description = "Datos de activación inválidos")
    @ApiResponse(responseCode = "404", description = "Tarjeta no encontrada")

    @PatchMapping("/enroll")
    public ResponseEntity<String> activateCard(
            @RequestBody @Parameter(description = "Número de la tarjeta a activar") EnrollCard enrollCard) {
        cardService.activateCard(enrollCard.getCardId());
        return ResponseEntity.ok("Tarjeta activada correctamente");
    }

    @Operation(summary = "Bloquear una tarjeta", description = "Marca una tarjeta como bloqueada.")
    @ApiResponse(responseCode = "200", description = "La tarjeta fue bloqueada con éxito")
    @ApiResponse(responseCode = "404", description = "Tarjeta no encontrada")
    @ApiResponse(responseCode = "500", description = "Error interno al bloquear la tarjeta")

    @PatchMapping("/{cardId}/block")
    public ResponseEntity<String> blockCard(@PathVariable @Parameter(description = "Número de la tarjeta a bloquear") String cardId) {
        cardService.blockCard(cardId);
        return ResponseEntity.ok("Tarjeta bloqueada correctamente");
    }

    @Operation(summary = "Recargar saldo a una tarjeta", description = "Añade fondos a una tarjeta activa.")
    @ApiResponse(responseCode = "200", description = "Saldo recargado exitosamente")
    @ApiResponse(responseCode = "400", description = "Solicitud inválida")
    @ApiResponse(responseCode = "404", description = "Tarjeta no encontrada")

    @PostMapping("/{cardId}/recharge")
    public ResponseEntity<String> rechargeCard(
            @PathVariable @Parameter(description = "Número de la tarjeta a recargar") String cardId,
            @RequestBody RechargeBalanceRequest rechargeRequest) {
        cardService.rechargeCard(cardId, rechargeRequest.getBalance());
        return ResponseEntity.ok("Tarjeta recargada correctamente");
    }

    @Operation(summary = "Consultar saldo de una tarjeta", description = "Obtiene el saldo disponible de una tarjeta existente.")
    @ApiResponse(responseCode = "200", description = "Saldo consultado exitosamente")
    @ApiResponse(responseCode = "404", description = "Tarjeta no encontrada")
    @GetMapping("/{cardId}/balance")
    public ResponseEntity<BalanceCard> checkAccountBalance(
            @PathVariable @Parameter(description = "Número de la tarjeta a consultar") String cardId) {
        BalanceCard balanceCard = cardService.checkBalance(cardId);
        return ResponseEntity.ok(balanceCard);
    }


}
