package com.credibanco.tarjetas.service;

import com.credibanco.tarjetas.dto.card.BalanceCard;
import com.credibanco.tarjetas.dto.card.CreateCardRequest;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CardServiceTest {

    @Mock
    private CardJpaRepository cardJpaRepository;

    @InjectMocks
    private CardService cardService;

    private CreateCardRequest createCardRequest;
    private CardEntity cardEntity;

    @BeforeEach
    void setUp() {

        createCardRequest = new CreateCardRequest();
        createCardRequest.setProductId("123456");
        createCardRequest.setHolderName("DANIEL VELANDIA");

        cardEntity = new CardEntity();
        cardEntity.setCardNumber("1234561234567890");
        cardEntity.setHolderName("DANIEL VELANDIA");
        cardEntity.setExpirationDate(LocalDate.now().plusYears(3));
        cardEntity.setBalance(BigDecimal.ZERO);
        cardEntity.setActive(false);
        cardEntity.setBlocked(false);
    }

    // Debería crear una tarjeta exitosamente
    @Test
    void createCard_ShouldReturnNewCardNumber() {
        when(cardJpaRepository.findByCardNumber(anyString())).thenReturn(null);
        when(cardJpaRepository.save(any(CardEntity.class))).thenReturn(cardEntity);

        String cardNumber = cardService.createCard(createCardRequest);

        assertNotNull(cardNumber);
        assertEquals("1234561234567890", cardNumber);
        verify(cardJpaRepository, times(1)).save(any(CardEntity.class));
    }

    //Deberia activar la tarjeta solicitada
    @Test
    void activateCard_ShouldActivateCard() {
        when(cardJpaRepository.findByCardNumber("1234561234567890")).thenReturn(cardEntity);

        cardService.activateCard("1234561234567890");

        assertTrue(cardEntity.getActive());
        verify(cardJpaRepository, times(1)).save(cardEntity);
    }

    //Debería marcar la tarjeta como bloqueada
    @Test
    void blockCard_ShouldBlockCard() {
        when(cardJpaRepository.findByCardNumber("1234561234567890")).thenReturn(cardEntity);

        cardService.blockCard("1234561234567890");

        assertTrue(cardEntity.getBlocked());
        verify(cardJpaRepository, times(1)).save(cardEntity);
    }
    //Debería recargar la tarjeta
    @Test
    void rechargeCard_ShouldIncreaseBalance() {
        when(cardJpaRepository.findByCardNumber("1234561234567890")).thenReturn(cardEntity);

        cardEntity.setActive(true);
        cardService.rechargeCard("1234561234567890", new BigDecimal("500.00"));

        assertEquals(new BigDecimal("500.00"), cardEntity.getBalance());
        verify(cardJpaRepository, times(1)).save(cardEntity);
    }

    @Test
    void checkBalance_ShouldReturnCorrectBalance() {
        when(cardJpaRepository.findByCardNumber("1234561234567890")).thenReturn(cardEntity);

        BalanceCard balanceCard = cardService.checkBalance("1234561234567890");

        assertEquals(BigDecimal.ZERO, balanceCard.getBalance());
        verify(cardJpaRepository, times(1)).findByCardNumber("1234561234567890");
    }
    //Debería disparar excepción si el ProductId no es valido
    @Test
    void createCard_ShouldThrowExceptionWhenProductIdIsInvalid() {
        createCardRequest.setProductId(null);
        assertThrows(NullPointerException.class, () -> cardService.createCard(createCardRequest));
    }
    //En el proceso de activación Debería disparar excepción si el numero de tarjeta no existe
    @Test
    void activateCard_ShouldThrowExceptionWhenCardNotFound() {
        when(cardJpaRepository.findByCardNumber("1234561234567890")).thenReturn(null);
        assertThrows(IllegalArgumentException.class, () -> cardService.activateCard("1234561234567890"));
    }
    //En el proceso de bloqueo Debería disparar excepción si el numero de tarjeta no existe
    @Test
    void blockCard_ShouldThrowExceptionWhenCardNotFound() {
        when(cardJpaRepository.findByCardNumber("1234561234567890")).thenReturn(null);
        assertThrows(IllegalArgumentException.class, () -> cardService.blockCard("1234561234567890"));
    }
    //En el proceso de recarga de tarjeta Debería disparar excepción si la tarjeta está bloqueada
    @Test
    void rechargeCard_ShouldThrowExceptionWhenCardIsBlocked() {
        cardEntity.setBlocked(true);
        when(cardJpaRepository.findByCardNumber("1234561234567890")).thenReturn(cardEntity);
        assertThrows(IllegalArgumentException.class, () -> cardService.rechargeCard("1234561234567890", new BigDecimal("500.00")));
    }
    //En el proceso de recarga de tarjeta Debería disparar excepción si el valor de la recarga es negativo
    @Test
    void rechargeCard_ShouldThrowExceptionWhenBalanceIsNegative() {
        assertThrows(IllegalArgumentException.class, () -> cardService.rechargeCard("1234561234567890", new BigDecimal("-100.00")));
    }
    //En el proceso de consulta de saldo de tarjeta Debería disparar excepción si la tarjeta no existe
    @Test
    void checkBalance_ShouldThrowExceptionWhenCardNotFound() {
        when(cardJpaRepository.findByCardNumber("1234561234567890")).thenReturn(null);
        assertThrows(IllegalArgumentException.class, () -> cardService.checkBalance("1234561234567890"));
    }


    @Test
    void activateCard_ShouldThrowExceptionWhenCardIdLengthIsInvalid() {
        assertThrows(IllegalArgumentException.class, () -> cardService.activateCard("12345"));
    }

    @Test
    void blockCard_ShouldThrowExceptionWhenCardIdLengthIsInvalid() {
        assertThrows(IllegalArgumentException.class, () -> cardService.blockCard("12345"));
    }

    @Test
    void rechargeCard_ShouldThrowExceptionWhenCardIdLengthIsInvalid() {
        assertThrows(IllegalArgumentException.class, () -> cardService.rechargeCard("12345", new BigDecimal("500.00")));
    }

    @Test
    void checkBalance_ShouldThrowExceptionWhenCardIdLengthIsInvalid() {
        assertThrows(IllegalArgumentException.class, () -> cardService.checkBalance("12345"));
    }

    @Test
    void rechargeCard_ShouldThrowExceptionWhenCardDoesNotExist() {
        when(cardJpaRepository.findByCardNumber("1234561234567890")).thenReturn(null);
        assertThrows(IllegalArgumentException.class, () -> cardService.rechargeCard("1234561234567890", new BigDecimal("500.00")));
    }

    @Test
    void rechargeCard_ShouldThrowExceptionWhenCardIsNotActivated() {
        cardEntity.setActive(false);
        when(cardJpaRepository.findByCardNumber("1234561234567890")).thenReturn(cardEntity);
        assertThrows(IllegalArgumentException.class, () -> cardService.rechargeCard("1234561234567890", new BigDecimal("500.00")));
    }

    @Test
    void rechargeCard_ShouldThrowExceptionWhenCardIsExpired() {
        cardEntity.setExpirationDate(LocalDate.now().minusDays(1));
        cardEntity.setActive(true);
        when(cardJpaRepository.findByCardNumber("1234561234567890")).thenReturn(cardEntity);
        assertThrows(IllegalArgumentException.class, () -> cardService.rechargeCard("1234561234567890", new BigDecimal("500.00")));
    }

}


