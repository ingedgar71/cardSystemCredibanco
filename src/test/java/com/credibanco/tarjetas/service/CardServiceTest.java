package com.credibanco.tarjetas.service;

import com.credibanco.tarjetas.dto.BalanceCard;
import com.credibanco.tarjetas.dto.card.RequestCreateCard;
import com.credibanco.tarjetas.persistencia.model.CardEntity;
import com.credibanco.tarjetas.persistencia.repository.CardJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CardServiceTest {

    @Mock
    private CardJpaRepository cardJpaRepository;

    @InjectMocks
    private CardService cardService;

    private CardEntity cardEntity;

    @BeforeEach
    void setUp() {
        cardEntity = new CardEntity();
        cardEntity.setCardNumber("1234561234567890");
        cardEntity.setHolderName("DANIEL VELANDIA");
        cardEntity.setExpirationDate(LocalDate.now().plusYears(3));
        cardEntity.setBalance(BigDecimal.ZERO);
        cardEntity.setActive(false);
        cardEntity.setBlocked(false);
    }

    // ============================
    // PRUEBAS PARA createCard()
    // Deberia crear la tarjeta satisfactoriamente
    // ============================

    @Test
    void shouldCreateCardSuccessfully() {
        RequestCreateCard request = new RequestCreateCard();
        request.setProductId("123456");
        request.setHolderName("DANIEL VELANDIA");

        when(cardJpaRepository.findByCardNumber(anyString())).thenReturn(null);
        when(cardJpaRepository.save(any(CardEntity.class))).thenReturn(cardEntity);

        String generatedCardNumber = cardService.createCard(request);

        assertNotNull(generatedCardNumber);
        assertEquals("1234561234567890", generatedCardNumber);
        verify(cardJpaRepository, times(1)).save(any(CardEntity.class));
    }

    // ============================
    // PRUEBAS PARA activateCard()
    // Debería activar la tarjeta
    // ============================

    @Test
    void shouldActivateCard() {
        when(cardJpaRepository.findByCardNumber("1234561234567890")).thenReturn(cardEntity);

        cardService.activateCard("1234561234567890");

        assertTrue(cardEntity.getActive());
        verify(cardJpaRepository, times(1)).save(cardEntity);
    }

    @Test
    void shouldThrowExceptionForInvalidCardIdInActivation() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> cardService.activateCard("123456"));
        assertEquals("Numéro de digitos de cardId no valido", exception.getMessage());
    }

    // ============================
    // PRUEBAS PARA blockCard()
    // ============================

    @Test
    void shouldBlockCard() {
        when(cardJpaRepository.findByCardNumber("1234561234567890")).thenReturn(cardEntity);

        cardService.blockCard("1234561234567890");

        assertTrue(cardEntity.getBlocked());
        verify(cardJpaRepository, times(1)).save(cardEntity);
    }

    @Test
    void shouldThrowExceptionForInvalidCardIdInBlocking() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> cardService.blockCard("1234"));
        assertEquals("Numéro de digitos de cardId no valido", exception.getMessage());
    }

    // ============================
    // PRUEBAS PARA rechargeCard()
    // ============================

    @Test
    void shouldRechargeCardSuccessfully() {
        cardEntity.setActive(true);
        when(cardJpaRepository.findByCardNumber("1234561234567890")).thenReturn(cardEntity);
        when(cardJpaRepository.save(any(CardEntity.class))).thenReturn(cardEntity);

        CardEntity result = cardService.rechargeCard("1234561234567890", new BigDecimal("100.00"));

        assertEquals(new BigDecimal("100.00"), result.getBalance());
        verify(cardJpaRepository, times(1)).save(cardEntity);
    }

    @Test
    void shouldThrowExceptionForBlockedCard() {
        cardEntity.setBlocked(true);
        when(cardJpaRepository.findByCardNumber("1234567890123456")).thenReturn(cardEntity);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> cardService.rechargeCard("1234567890123456", new BigDecimal("50.00")));
        assertEquals("la tarjeta se encuentra bloqueada", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionForInactiveCard() {
        when(cardJpaRepository.findByCardNumber("1234567890123456")).thenReturn(cardEntity);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> cardService.rechargeCard("1234567890123456", new BigDecimal("50.00")));
        assertEquals("la tarjeta no se encuentra activada", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionForExpiredCard() {
        cardEntity.setExpirationDate(LocalDate.now().minusDays(1));
        cardEntity.setActive(true);
        when(cardJpaRepository.findByCardNumber("1234567890123456")).thenReturn(cardEntity);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> cardService.rechargeCard("1234567890123456", new BigDecimal("50.00")));
        assertEquals("la tarjeta esta expirada", exception.getMessage());
    }

    // ============================
    // PRUEBAS PARA checkBalance()
    // ============================

    @Test
    void shouldReturnBalanceCorrectly() {
        when(cardJpaRepository.findByCardNumber("1234561234567890")).thenReturn(cardEntity);

        BalanceCard balance = cardService.checkBalance("1234561234567890");

        assertNotNull(balance);
        assertEquals("1234561234567890", balance.getCardId());
        assertEquals(BigDecimal.ZERO, balance.getBalance());
    }

    @Test
    void shouldThrowExceptionForInvalidCardIdInCheckBalance() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> cardService.checkBalance("123456"));
        assertEquals("Numéro de digitos de cardId no valido", exception.getMessage());
    }
}