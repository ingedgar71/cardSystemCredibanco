package com.credibanco.tarjetas.service;

import com.credibanco.tarjetas.dto.transaction.RequestCancelTransaction;
import com.credibanco.tarjetas.dto.transaction.ResponseCheckTransaction;
import com.credibanco.tarjetas.persistencia.model.CardEntity;
import com.credibanco.tarjetas.persistencia.model.TransactionEntity;
import com.credibanco.tarjetas.persistencia.repository.CardJpaRepository;
import com.credibanco.tarjetas.persistencia.repository.TransactionJpaRepository;
import com.credibanco.tarjetas.util.operbigdecimal.AddBigDecimal;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

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
        cardEntity.setCardNumber("1234561234567890");
        cardEntity.setBalance(new BigDecimal("1000.00"));
        cardEntity.setActive(true);
        cardEntity.setBlocked(false);
        cardEntity.setExpirationDate(LocalDate.now().plusYears(3));

        transactionEntity = new TransactionEntity();
        transactionEntity.setTransactionNumber("987654");
        transactionEntity.setAmount(new BigDecimal("200.00"));
        transactionEntity.setCancelled(false);
        transactionEntity.setTimestamp(LocalDateTime.now());
        transactionEntity.setCardEntity(cardEntity);
    }

    @Test
    void makePurchase_ShouldThrowExceptionWhenCardDoesNotExist() {
        when(cardJpaRepository.findByCardNumber("1234561234567890")).thenReturn(null);
        assertThrows(IllegalArgumentException.class, () -> transactionService.makePurchase("1234561234567890", new BigDecimal("100.00")));
    }

    @Test
    void makePurchase_ShouldThrowExceptionWhenCardIsBlocked() {
        cardEntity.setBlocked(true);
        when(cardJpaRepository.findByCardNumber("1234561234567890")).thenReturn(cardEntity);
        assertThrows(IllegalArgumentException.class, () -> transactionService.makePurchase("1234561234567890", new BigDecimal("100.00")));
    }

    @Test
    void makePurchase_ShouldThrowExceptionWhenInsufficientBalance() {
        when(cardJpaRepository.findByCardNumber("1234561234567890")).thenReturn(cardEntity);
        assertThrows(IllegalArgumentException.class, () -> transactionService.makePurchase("1234561234567890", new BigDecimal("2000.00")));
    }

    @Test
    void checkTransaction_ShouldThrowExceptionWhenTransactionDoesNotExist() {
        when(transactionJpaRepository.findByTransactionNumberAndCardEntityCardNumber("987654", "1234561234567890"))
                .thenReturn(null);
        assertThrows(IllegalArgumentException.class, () -> transactionService.checkTransaction("987654", "1234561234567890"));
    }

    @Test
    void cancelTransaction_ShouldThrowExceptionWhenTransactionDoesNotExist() {
        RequestCancelTransaction request = new RequestCancelTransaction();
        request.setTransactionId("987654");
        request.setCardId("1234561234567890");
        when(transactionJpaRepository.findByTransactionNumberAndCardEntityCardNumber("987654", "1234561234567890"))
                .thenReturn(null);
        assertThrows(IllegalArgumentException.class, () -> transactionService.cancelTransaction(request));
    }

    @Test
    void cancelTransaction_ShouldThrowExceptionWhenTransactionAlreadyCancelled() {
        RequestCancelTransaction request = new RequestCancelTransaction();
        request.setTransactionId("987654");
        request.setCardId("1234561234567890");
        transactionEntity.setCancelled(true);
        when(transactionJpaRepository.findByTransactionNumberAndCardEntityCardNumber("987654", "1234561234567890"))
                .thenReturn(transactionEntity);
        assertThrows(IllegalArgumentException.class, () -> transactionService.cancelTransaction(request));
    }

    @Test
    void cancelTransaction_ShouldThrowExceptionWhenTransactionIsOlderThan24Hours() {
        RequestCancelTransaction request = new RequestCancelTransaction();
        request.setTransactionId("987654");
        request.setCardId("1234561234567890");
        transactionEntity.setTimestamp(LocalDateTime.now().minusHours(25));
        when(transactionJpaRepository.findByTransactionNumberAndCardEntityCardNumber("987654", "1234561234567890"))
                .thenReturn(transactionEntity);
        assertThrows(IllegalArgumentException.class, () -> transactionService.cancelTransaction(request));
    }

    @Test
    void makePurchase_ShouldThrowExceptionWhenCardLengthNot16() {
        assertThrows(IllegalArgumentException.class, () -> transactionService.makePurchase("12345", new BigDecimal("25000.00")));
    }

    @Test
    void makePurchase_ShouldThrowExceptionWhenPriceIsNullOrZero() {
        assertThrows(IllegalArgumentException.class, () -> transactionService.makePurchase("1234561234567890", null));
        assertThrows(IllegalArgumentException.class, () -> transactionService.makePurchase("1234561234567890", BigDecimal.ZERO));
    }

    @Test
    void makePurchase_ShouldThrowExceptionWhenCardIsNotActive() {
        when(cardJpaRepository.findByCardNumber("1234561234567890")).thenReturn(cardEntity);
        cardEntity.setActive(false);
        assertThrows(IllegalArgumentException.class, () -> transactionService.makePurchase("1234561234567890", new BigDecimal("25000.00")));

    }

    @Test
    void makePurchase_ShouldThrowExceptionWhenCardIsExpired() {
        when(cardJpaRepository.findByCardNumber("1234561234567890")).thenReturn(cardEntity);
        cardEntity.setExpirationDate(LocalDate.now().minusDays(1));
        assertThrows(IllegalArgumentException.class, () -> transactionService.makePurchase("1234561234567890", new BigDecimal("25000.00")));

    }
    @Test
    void checkTransaction_ShouldReturnTransactionDetails() {
        when(transactionJpaRepository.findByTransactionNumberAndCardEntityCardNumber("987654", "1234561234567890"))
                .thenReturn(transactionEntity);

        ResponseCheckTransaction response = transactionService.checkTransaction("987654", "1234561234567890");

        assertNotNull(response);
        assertEquals("1234561234567890", response.getCardId());
        assertEquals(new BigDecimal("200.00"), response.getAmount());
        assertFalse(response.getCancelled());
        verify(transactionJpaRepository, times(1)).findByTransactionNumberAndCardEntityCardNumber("987654", "1234561234567890");
    }

    @Test
    void makePurchase_ShouldUpdateBalanceAndSaveCard() {
        when(cardJpaRepository.findByCardNumber("1234561234567890")).thenReturn(cardEntity);
        when(transactionJpaRepository.save(any(TransactionEntity.class))).thenReturn(transactionEntity);
        when(cardJpaRepository.save(any(CardEntity.class))).thenReturn(cardEntity);

        String transactionNumber = transactionService.makePurchase("1234561234567890", new BigDecimal("100.00"));

        assertEquals(new BigDecimal("900.00"), cardEntity.getBalance());
        assertNotNull(transactionNumber);
        verify(cardJpaRepository, times(1)).save(cardEntity);
        verify(transactionJpaRepository, times(1)).save(any(TransactionEntity.class));
    }

    @Test
    void cancelTransaction_ShouldReturnAmountToCardBalance() {
        RequestCancelTransaction request = new RequestCancelTransaction();
        request.setTransactionId("987654");
        request.setCardId("1234561234567890");

        when(transactionJpaRepository.findByTransactionNumberAndCardEntityCardNumber("987654", "1234561234567890"))
                .thenReturn(transactionEntity);
        when(cardJpaRepository.save(any(CardEntity.class))).thenReturn(cardEntity);
        when(transactionJpaRepository.save(any(TransactionEntity.class))).thenReturn(transactionEntity);

        transactionService.cancelTransaction(request);

        BigDecimal expectedBalance = AddBigDecimal.execute(new BigDecimal("1000.00"), new BigDecimal("200.00"));
        assertEquals(expectedBalance, cardEntity.getBalance());
        assertTrue(transactionEntity.getCancelled());
        verify(cardJpaRepository, times(1)).save(cardEntity);
        verify(transactionJpaRepository, times(1)).save(transactionEntity);
    }

}
