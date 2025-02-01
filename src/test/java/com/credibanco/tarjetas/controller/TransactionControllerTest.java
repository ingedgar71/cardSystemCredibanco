package com.credibanco.tarjetas.controller;

import com.credibanco.tarjetas.dto.transaction.RequestPurchase;
import com.credibanco.tarjetas.dto.transaction.RequestCancelTransaction;
import com.credibanco.tarjetas.dto.transaction.RequestCheckTransaction;
import com.credibanco.tarjetas.dto.transaction.ResponseCheckTransaction;
import com.credibanco.tarjetas.dto.transaction.ResponsePurchase;
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
        requestPurchase.setCardId("1234561234567890");
        requestPurchase.setPrice(new BigDecimal("100.00"));

        requestCheckTransaction = new RequestCheckTransaction();
        requestCheckTransaction.setTransactionId("123456");
        requestCheckTransaction.setCardId("1234561234567890");

        responseCheckTransaction = new ResponseCheckTransaction();
        responseCheckTransaction.setCardId("1234561234567890");
        responseCheckTransaction.setAmount(new BigDecimal("100.00"));
        responseCheckTransaction.setCancelled(false);

        requestCancelTransaction = new RequestCancelTransaction();
        requestCancelTransaction.setTransactionId("123456");
        requestCancelTransaction.setCardId("1234561234567890");
    }

    //Debería realizar una compra satisfactoriamente
    @Test
    void shouldMakePurchaseSuccessfully() {
        when(transactionService.makePurchase("1234561234567890", new BigDecimal("100.00"))).thenReturn("987654");

        ResponseEntity<ResponsePurchase> response = transactionController.makePurchase(requestPurchase);

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals("987654", response.getBody().getNumberTransaction());
        verify(transactionService, times(1)).makePurchase("1234561234567890", new BigDecimal("100.00"));
    }

    // Deberia retornar detalles de la transacción
    @Test
    void shouldReturnTransactionDetails() {
        when(transactionService.checkTransaction("123456","1234561234567890")).thenReturn(responseCheckTransaction);

        ResponseEntity<ResponseCheckTransaction> response = transactionController.checkTransaction("123456","1234561234567890");

        assertEquals(200, response.getStatusCode().value());
        assertEquals("1234561234567890", response.getBody().getCardId());
        assertEquals(new BigDecimal("100.00"), response.getBody().getAmount());
        verify(transactionService, times(1)).checkTransaction("123456", "1234561234567890");
    }

    //Debería cancelar la transacción dada
    @Test
    void shouldCancelTransactionSuccessfully() {
        doNothing().when(transactionService).cancelTransaction(requestCancelTransaction);

        transactionController.cancelTransaction(requestCancelTransaction);

        verify(transactionService, times(1)).cancelTransaction(requestCancelTransaction);
    }
}