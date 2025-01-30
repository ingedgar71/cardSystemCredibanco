package com.credibanco.tarjetas.controller;

import com.credibanco.tarjetas.dto.CancelTransaction;
import com.credibanco.tarjetas.dto.RequestPurchase;
import com.credibanco.tarjetas.service.TransactionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/transaction")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }
    //Realizar compra
    @PostMapping("/purchase")
    public ResponseEntity<Void> makePurchase(@RequestBody RequestPurchase purchase){
        transactionService.makePurchase(purchase.getCardId(),purchase.getPrice());
        return ResponseEntity.ok().build();
    }
    @GetMapping("/{transactionId}")
    public ResponseEntity<Void> checkTransaction(@PathVariable ("transactionId") String numTransaction){
        return null;
    }

    //Anular transaccion
    @PatchMapping("/anulation")
    public void cancelTransaction(@RequestBody CancelTransaction cancelTransaction){
        transactionService.cancelTransaction(cancelTransaction.getCardId(), cancelTransaction.getTransactionId());
    }

}
