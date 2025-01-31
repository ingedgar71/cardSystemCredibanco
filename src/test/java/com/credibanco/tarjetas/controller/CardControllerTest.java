package com.credibanco.tarjetas.controller;

import com.credibanco.tarjetas.dto.BalanceCard;
import com.credibanco.tarjetas.dto.EnrollCard;
import com.credibanco.tarjetas.dto.card.RequestCreateCard;
import com.credibanco.tarjetas.persistencia.model.CardEntity;
import com.credibanco.tarjetas.service.CardService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CardControllerTest {

    @Mock
    private CardService cardService;

    @InjectMocks
    private CardController cardController;

    private CardEntity cardEntity;
    private BalanceCard balanceCard;

    @BeforeEach
    void setUp() {
        cardEntity = new CardEntity();
        cardEntity.setIdCard(1L);
        cardEntity.setCardNumber("1234567890123456");

        balanceCard = new BalanceCard();
        balanceCard.setCardId("1234567890123456");
        balanceCard.setBalance(new BigDecimal("500.00"));
    }


    // ============================
    // PRUEBAS PARA createCard()
    // ============================

    @Test
    void shouldCreateCardSuccessfully() {
        RequestCreateCard request = new RequestCreateCard();
        request.setProductId("1234");
        request.setHolderName("John Doe");

        when(cardService.createCard(any(RequestCreateCard.class))).thenReturn("1234567890123456");

        ResponseEntity<String> response = cardController.createCard(request);

        assertEquals(201, response.getStatusCodeValue());
        assertEquals("1234567890123456", response.getBody());
    }

    // ============================
    // PRUEBAS PARA activateCard()
    // ============================

    @Test
    void shouldActivateCard() {
        EnrollCard enrollCard = new EnrollCard();
        enrollCard.setCardId("1234567890123456");

        doNothing().when(cardService).activateCard("1234567890123456");

        cardController.activateCard(enrollCard);

        verify(cardService, times(1)).activateCard("1234567890123456");
    }

    // ============================
    // PRUEBAS PARA blockCard()
    // ============================

    @Test
    void shouldBlockCard() {
        doNothing().when(cardService).blockCard("1234567890123456");

        cardController.blockCard("1234567890123456");

        verify(cardService, times(1)).blockCard("1234567890123456");
    }

    // ============================
    // PRUEBAS PARA rechargeCard()
    // ============================

//    @Test
//    void shouldRechargeCard() {
//        doNothing().when(cardService).rechargeCard("1234567890123456", new BigDecimal("100.00"));
//
//        cardController.rechargeCard(balanceCard);
//
//        verify(cardService, times(1)).rechargeCard("1234567890123456", new BigDecimal("500.00"));
//    }

    // ============================
    // PRUEBAS PARA checkAccountBalance()
    // ============================

    @Test
    void shouldReturnBalanceSuccessfully() {
        when(cardService.checkBalance("1234567890123456")).thenReturn(balanceCard);

        ResponseEntity<BalanceCard> response = cardController.checkAccountBalance("1234567890123456");

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(balanceCard, response.getBody());
    }

}