package com.credibanco.tarjetas.controller;

import com.credibanco.tarjetas.dto.transaction.RequestCancelTransaction;
import com.credibanco.tarjetas.dto.RequestPurchase;
import com.credibanco.tarjetas.dto.transaction.RequestCheckTransaction;
import com.credibanco.tarjetas.dto.transaction.ResponseCheckTransaction;
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

    //Consultar Transaction
    @GetMapping("/check_transaction")
    public ResponseEntity<ResponseCheckTransaction> checkTransaction(@RequestBody RequestCheckTransaction transaction){
        return ResponseEntity.ok(transactionService.checkTransaction(transaction)) ;
    }

    //Anular transaccion
    @PatchMapping("/anulation")
    public void cancelTransaction(@RequestBody RequestCancelTransaction requestCancelTransaction){
        transactionService.cancelTransaction(requestCancelTransaction);
    }

}
