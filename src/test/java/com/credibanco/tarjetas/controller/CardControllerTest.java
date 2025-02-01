package com.credibanco.tarjetas.controller;

import com.credibanco.tarjetas.dto.card.EnrollCard;
import com.credibanco.tarjetas.dto.card.BalanceCard;
import com.credibanco.tarjetas.dto.card.CreateCardRequest;
import com.credibanco.tarjetas.dto.card.RechargeBalanceRequest;
import com.credibanco.tarjetas.service.CardService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CardControllerTest {

    @Mock
    private CardService cardService;

    @InjectMocks
    private CardController cardController;

    private CreateCardRequest createCardRequest;
    private EnrollCard enrollCard;
    private RechargeBalanceRequest rechargeBalanceRequest;
    private BalanceCard balanceCard;

    @BeforeEach
    void setUp() {
        createCardRequest = new CreateCardRequest();
        createCardRequest.setProductId("123456");
        createCardRequest.setHolderName("ANGELICA LOPEZ");

        enrollCard = new EnrollCard();
        enrollCard.setCardId("1234561234567890");

        rechargeBalanceRequest = new RechargeBalanceRequest();
        rechargeBalanceRequest.setBalance(new BigDecimal("500.00"));

        balanceCard = new BalanceCard();
        balanceCard.setBalance(new BigDecimal("1000.00"));
    }

    @Test
    void createCard_ShouldReturnCreated() {
        when(cardService.createCard(any(CreateCardRequest.class))).thenReturn("1234561234567890");

        ResponseEntity<String> response = cardController.createCard(createCardRequest);

        assertEquals(201, response.getStatusCode().value());
        assertEquals("1234561234567890", response.getBody());
        verify(cardService, times(1)).createCard(createCardRequest);
    }

    @Test
    void activateCard_ShouldReturnOk() {
        doNothing().when(cardService).activateCard("1234561234567890");

        ResponseEntity<String> response = cardController.activateCard(enrollCard);

        assertEquals(200, response.getStatusCode().value());
        assertEquals("Tarjeta activada correctamente", response.getBody());
        verify(cardService, times(1)).activateCard("1234561234567890");
    }

    @Test
    void blockCard_ShouldReturnOk() {
        doNothing().when(cardService).blockCard("1234561234567890");

        ResponseEntity<String> response = cardController.blockCard("1234561234567890");

        assertEquals(200, response.getStatusCode().value());
        assertEquals("Tarjeta bloqueada correctamente", response.getBody());
        verify(cardService, times(1)).blockCard("1234561234567890");
    }

    @Test
    void rechargeCard_ShouldReturnOk() {
        doNothing().when(cardService).rechargeCard("1234561234567890", new BigDecimal("500.00"));

        ResponseEntity<String> response = cardController.rechargeCard("1234561234567890", rechargeBalanceRequest);

        assertEquals(200, response.getStatusCode().value());
        assertEquals("Tarjeta recargada correctamente", response.getBody());
        verify(cardService, times(1)).rechargeCard("1234561234567890", new BigDecimal("500.00"));
    }

    @Test
    void checkAccountBalance_ShouldReturnBalance() {
        when(cardService.checkBalance("1234561234567890")).thenReturn(balanceCard);

        ResponseEntity<BalanceCard> response = cardController.checkAccountBalance("1234561234567890");

        assertEquals(200, response.getStatusCode().value());
        assertEquals(new BigDecimal("1000.00"), response.getBody().getBalance());
        verify(cardService, times(1)).checkBalance("1234561234567890");
    }

}