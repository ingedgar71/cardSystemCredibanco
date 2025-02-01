package com.credibanco.tarjetas.controller;

import com.credibanco.tarjetas.dto.transaction.RequestCancelTransaction;
import com.credibanco.tarjetas.dto.transaction.RequestPurchase;
import com.credibanco.tarjetas.dto.transaction.ResponseCheckTransaction;
import com.credibanco.tarjetas.dto.transaction.ResponsePurchase;
import com.credibanco.tarjetas.service.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/transaction")
@Tag(name = "Transacciones", description = "Gestión de compras y cancelaciones de transacciones")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @Operation(summary = "Realizar compra", description = "Procesa una compra con la tarjeta y el monto indicado.")
    @ApiResponse(responseCode = "200", description = "Compra realizada exitosamente")
    @ApiResponse(responseCode = "400", description = "Datos inválidos en la compra")
    @ApiResponse(responseCode = "404", description = "Tarjeta no encontrada")
    @ApiResponse(responseCode = "500", description = "Error interno al procesar la compra")

    @PostMapping("/purchase")
    public ResponseEntity<ResponsePurchase> makePurchase(@Valid @RequestBody  RequestPurchase purchase) {

        ResponsePurchase response = new ResponsePurchase();
        response.setNumberTransaction(transactionService.makePurchase(purchase.getCardId(), purchase.getPrice()));
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Consultar transacción", description = "Obtiene información detallada de una transacción específica.")
    @ApiResponse(responseCode = "200", description = "Consulta exitosa")
    @ApiResponse(responseCode = "404", description = "Transacción no encontrada")

    @GetMapping("/{transactionId}")
    public ResponseEntity<ResponseCheckTransaction> checkTransaction(
            @PathVariable @Parameter(description = "ID de la transacción a consultar") String transactionId,
            @RequestParam @Parameter(description = "ID de la tarjeta asociada a la transacción") String cardId) {

        return ResponseEntity.ok(transactionService.checkTransaction(transactionId, cardId));
    }

    @Operation(summary = "Cancelar transacción", description = "Anula una transacción previamente realizada.")
    @ApiResponse(responseCode = "200", description = "Transacción anulada correctamente")
    @ApiResponse(responseCode = "400", description = "Datos inválidos en la solicitud de anulación")
    @ApiResponse(responseCode = "404", description = "Transacción no encontrada")
    @ApiResponse(responseCode = "500", description = "Error interno al anular la transacción")

    @PatchMapping("/cancel")
    public ResponseEntity<Void> cancelTransaction(@RequestBody @Valid RequestCancelTransaction requestCancelTransaction) {
        transactionService.cancelTransaction(requestCancelTransaction);
        return ResponseEntity.ok().build();
    }
}
