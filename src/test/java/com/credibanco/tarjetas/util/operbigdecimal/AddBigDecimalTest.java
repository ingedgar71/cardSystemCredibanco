package com.credibanco.tarjetas.util.operbigdecimal;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class AddBigDecimalTest {

    @Test
    void shouldSumCorrectlyMultipleBigDecimals() {
        BigDecimal result = AddBigDecimal.execute(
                new BigDecimal("10.50"),
                new BigDecimal("20.30"),
                new BigDecimal("5.20")
        );
        assertEquals(new BigDecimal("36.00"), result, "La suma no es correcta.");
    }

    @Test
    void shouldRoundToTwoDecimalsUsingHalfUp() {
        BigDecimal result = AddBigDecimal.execute(
                new BigDecimal("10.555"),
                new BigDecimal("20.555")
        );
        assertEquals(new BigDecimal("31.11"), result, "El redondeo no es correcto.");
    }

    @Test
    void shouldHandleNegativeNumbers() {
        BigDecimal result = AddBigDecimal.execute(
                new BigDecimal("-5.75"),
                new BigDecimal("10.50")
        );
        assertEquals(new BigDecimal("4.75"), result, "La suma con negativos no es correcta.");
    }

    @Test
    void shouldHandleZeroAndPositiveNumbers() {
        BigDecimal result = AddBigDecimal.execute(
                BigDecimal.ZERO,
                new BigDecimal("15.75")
        );
        assertEquals(new BigDecimal("15.75"), result, "El resultado con cero es incorrecto.");
    }

    @Test
    void shouldReturnSameNumberWhenOnlyOneIsProvided() {
        BigDecimal result = AddBigDecimal.execute(new BigDecimal("99.99"));
        assertEquals(new BigDecimal("99.99"), result, "El resultado no es correcto con un solo número.");
    }

    // ============================
    // PRUEBAS PARA VALIDACIONES Y EXCEPCIONES
    // ============================

    @Test
    void shouldThrowExceptionWhenNoNumbersProvided() {
        Exception exception = assertThrows(ArithmeticException.class, () -> AddBigDecimal.execute());
        assertEquals("Debe proporcionar al menos un número.", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenNumbersArrayIsNull() {
        Exception exception = assertThrows(ArithmeticException.class, () -> AddBigDecimal.execute((BigDecimal[]) null));
        assertEquals("Debe proporcionar al menos un número.", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenAnyElementIsNull() {
        Exception exception = assertThrows(ArithmeticException.class, () -> AddBigDecimal.execute(
                new BigDecimal("10.50"),
                null,
                new BigDecimal("5.20")
        ));
        assertEquals("Los términos no pueden ser null.", exception.getMessage());
    }
}