package com.credibanco.tarjetas.util.operbigdecimal;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static org.junit.jupiter.api.Assertions.*;

class DivideBigDecimalTest {

    // ============================
    // PRUEBAS PARA DIVISIÓN DE BIGDECIMALS
    // ============================

    @Test
    void shouldDivideCorrectly() {
        BigDecimal result = DivideBigDecimal.execute(new BigDecimal("10.00"), new BigDecimal("2.00"));
        assertEquals(new BigDecimal("5.00"), result, "La división no es correcta.");
    }

    @Test
    void shouldRoundToTwoDecimalsUsingHalfUp() {
        BigDecimal result = DivideBigDecimal.execute(new BigDecimal("10.555"), new BigDecimal("3"));
        assertEquals(new BigDecimal("3.52"), result, "El redondeo no es correcto.");
    }

    @Test
    void shouldHandleNegativeDivision() {
        BigDecimal result = DivideBigDecimal.execute(new BigDecimal("-10.00"), new BigDecimal("2.00"));
        assertEquals(new BigDecimal("-5.00"), result, "La división con número negativo no es correcta.");
    }

    @Test
    void shouldHandleZeroNumerator() {
        BigDecimal result = DivideBigDecimal.execute(BigDecimal.ZERO, new BigDecimal("5.00"));
        assertEquals(BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP), result, "La división con numerador 0 no es correcta.");
    }

    @Test
    void shouldHandleExactDivision() {
        BigDecimal result = DivideBigDecimal.execute(new BigDecimal("100.00"), new BigDecimal("5.00"));
        assertEquals(new BigDecimal("20.00"), result, "La división exacta no es correcta.");
    }

    // ============================
    // PRUEBAS PARA VALIDACIONES Y EXCEPCIONES
    // ============================

    @Test
    void shouldThrowExceptionWhenDividingByZero() {
        Exception exception = assertThrows(ArithmeticException.class, () -> DivideBigDecimal.execute(new BigDecimal("10.00"), BigDecimal.ZERO));
        assertEquals("División por Cero no permitido", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenDivisorIsNull() {
        Exception exception = assertThrows(ArithmeticException.class, () -> DivideBigDecimal.execute(new BigDecimal("10.00"), null));
        assertEquals("División por Cero no permitido", exception.getMessage());
    }
}