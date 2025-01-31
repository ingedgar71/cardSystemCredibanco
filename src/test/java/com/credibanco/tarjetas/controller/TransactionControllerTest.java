package com.credibanco.tarjetas.controller;

import com.credibanco.tarjetas.dto.RequestPurchase;
import com.credibanco.tarjetas.dto.transaction.RequestCancelTransaction;
import com.credibanco.tarjetas.dto.transaction.RequestCheckTransaction;
import com.credibanco.tarjetas.dto.transaction.ResponseCheckTransaction;
import com.credibanco.tarjetas.service.TransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionControllerTest {

    @Mock
    private TransactionService transactionService;

    @InjectMocks
    private TransactionController transactionController;

    private RequestPurchase requestPurchase;
    private RequestCheckTransaction requestCheckTransaction;
    private ResponseCheckTransaction responseCheckTransaction;
    private RequestCancelTransaction requestCancelTransaction;

    @BeforeEach
    void setUp() {
        requestPurchase = new RequestPurchase();
        requestPurchase.setCardId("1234567890123456");
        requestPurchase.setPrice(new BigDecimal("100.00"));

        requestCheckTransaction = new RequestCheckTransaction();
        requestCheckTransaction.setTransactionId("TX123456");
        requestCheckTransaction.setCardId("1234567890123456");

        responseCheckTransaction = new ResponseCheckTransaction();
        responseCheckTransaction.setCardId("1234567890123456");
        responseCheckTransaction.setAmount(new BigDecimal("100.00"));
        responseCheckTransaction.setCancelled(false);

        requestCancelTransaction = new RequestCancelTransaction();
        requestCancelTransaction.setTransactionId("TX123456");
        requestCancelTransaction.setCardId("1234567890123456");
    }

    // ============================
    // PRUEBAS PARA makePurchase()
    // ============================

    @Test
    void shouldMakePurchaseSuccessfully() {
        doNothing().when(transactionService).makePurchase("1234567890123456", new BigDecimal("100.00"));

        ResponseEntity<Void> response = transactionController.makePurchase(requestPurchase);

        assertEquals(200, response.getStatusCodeValue());
        verify(transactionService, times(1)).makePurchase("1234567890123456", new BigDecimal("100.00"));
    }

    // ============================
    // PRUEBAS PARA checkTransaction()
    // ============================

    @Test
    void shouldReturnTransactionDetails() {
        when(transactionService.checkTransaction(requestCheckTransaction)).thenReturn(responseCheckTransaction);

        ResponseEntity<ResponseCheckTransaction> response = transactionController.checkTransaction(requestCheckTransaction);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(responseCheckTransaction, response.getBody());
    }

    // ============================
    // PRUEBAS PARA cancelTransaction()
    // ============================

    @Test
    void shouldCancelTransactionSuccessfully() {
        doNothing().when(transactionService).cancelTransaction(requestCancelTransaction);

        transactionController.cancelTransaction(requestCancelTransaction);

        verify(transactionService, times(1)).cancelTransaction(requestCancelTransaction);
    }
}