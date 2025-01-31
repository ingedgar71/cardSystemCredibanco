package com.credibanco.tarjetas.service;

import com.credibanco.tarjetas.dto.transaction.RequestCancelTransaction;
import com.credibanco.tarjetas.dto.transaction.RequestCheckTransaction;
import com.credibanco.tarjetas.dto.transaction.ResponseCheckTransaction;
import com.credibanco.tarjetas.persistencia.model.CardEntity;
import com.credibanco.tarjetas.persistencia.model.TransactionEntity;
import com.credibanco.tarjetas.persistencia.repository.CardJpaRepository;
import com.credibanco.tarjetas.persistencia.repository.TransactionJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;


@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

    @Mock
    private TransactionJpaRepository transactionJpaRepository;

    @Mock
    private CardJpaRepository cardJpaRepository;

    @InjectMocks
    private TransactionService transactionService;

    private CardEntity cardEntity;
    private TransactionEntity transactionEntity;


    @BeforeEach
    void setUp() {
        cardEntity = new CardEntity();
        cardEntity.setIdCard(1L);
        cardEntity.setCardNumber("1234567890123456");
        cardEntity.setBalance(new BigDecimal("500.00"));
        cardEntity.setActive(true);
        cardEntity.setBlocked(false);
        cardEntity.setExpirationDate(LocalDate.now().plusYears(3));

        transactionEntity = new TransactionEntity();
        transactionEntity.setTransactionNumber("123456");
        transactionEntity.setCardEntity(cardEntity);
        transactionEntity.setAmount(new BigDecimal("50.00"));
        transactionEntity.setTimestamp(LocalDateTime.now());
        transactionEntity.setCancelled(false);
    }

    // ============================
    // PRUEBAS PARA makePurchase()
    // ============================

    @Test
    void shouldMakePurchaseSuccessfully() {
        when(cardJpaRepository.findByCardNumber("1234567890123456")).thenReturn(cardEntity);
        when(cardJpaRepository.save(any(CardEntity.class))).thenReturn(cardEntity);
        when(transactionJpaRepository.findByTransactionNumberAndCardEntityIdCard(anyString(), anyLong())).thenReturn(null);
        when(transactionJpaRepository.save(any(TransactionEntity.class))).thenReturn(transactionEntity);

        transactionService.makePurchase("1234567890123456", new BigDecimal("100.00"));

        verify(cardJpaRepository, times(1)).save(any(CardEntity.class));
        verify(transactionJpaRepository, times(1)).save(any(TransactionEntity.class));
    }

    @Test
    void shouldThrowExceptionForBlockedCard() {
        cardEntity.setBlocked(true);
        when(cardJpaRepository.findByCardNumber("1234567890123456")).thenReturn(cardEntity);

        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                transactionService.makePurchase("1234567890123456", new BigDecimal("50.00"))
        );
        assertEquals("la tarjeta se encuentra bloqueada", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionForInsufficientBalance() {
        when(cardJpaRepository.findByCardNumber("1234567890123456")).thenReturn(cardEntity);

        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                transactionService.makePurchase("1234567890123456", new BigDecimal("600.00"))
        );
        assertEquals("Saldo Insuficiente", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionForInvalidCardId() {
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                transactionService.makePurchase("1234", new BigDecimal("100.00"))
        );
        assertEquals("cardId no valido", exception.getMessage());
    }

    // ============================
    // PRUEBAS PARA checkTransaction()
    // ============================

    @Test
    void shouldReturnTransactionDetails() {
        when(transactionJpaRepository.findByTransactionNumberAndCardEntityCardNumber("123456", "1234567890123456"))
                .thenReturn(transactionEntity);

        RequestCheckTransaction request = new RequestCheckTransaction();
        request.setTransactionId("123456");
        request.setCardId("1234567890123456");

        ResponseCheckTransaction response = transactionService.checkTransaction(request);

        assertNotNull(response);
        assertEquals("1234567890123456", response.getCardId());
        assertEquals(new BigDecimal("50.00"), response.getAmount());
        assertFalse(response.getCancelled());
    }

    @Test
    void shouldThrowExceptionWhenTransactionNotFound() {
        when(transactionJpaRepository.findByTransactionNumberAndCardEntityCardNumber(anyString(), anyString()))
                .thenReturn(null);

        RequestCheckTransaction request = new RequestCheckTransaction();
        request.setTransactionId("999999");
        request.setCardId("1234567890123456");

        Exception exception = assertThrows(NullPointerException.class, () -> transactionService.checkTransaction(request));
    }

    // ============================
    // PRUEBAS PARA cancelTransaction()
    // ============================

    @Test
    void shouldCancelTransactionSuccessfully() {
        when(transactionJpaRepository.findByTransactionNumberAndCardEntityCardNumber("123456", "1234567890123456"))
                .thenReturn(transactionEntity);

        RequestCancelTransaction request = new RequestCancelTransaction();
        request.setTransactionId("123456");
        request.setCardId("1234567890123456");

        transactionService.cancelTransaction(request);

        assertTrue(transactionEntity.getCancelled());
        verify(transactionJpaRepository, times(1)).save(transactionEntity);
    }

    @Test
    void shouldThrowExceptionWhenTransactionNotFoundInCancel() {
        when(transactionJpaRepository.findByTransactionNumberAndCardEntityCardNumber(anyString(), anyString()))
                .thenReturn(null);

        RequestCancelTransaction request = new RequestCancelTransaction();
        request.setTransactionId("999999");
        request.setCardId("1234567890123456");

        Exception exception = assertThrows(IllegalArgumentException.class, () -> transactionService.cancelTransaction(request));
        assertEquals("La transacción que se desea anular no existe.", exception.getMessage());
    }

}