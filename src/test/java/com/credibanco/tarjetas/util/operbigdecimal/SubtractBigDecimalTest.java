package com.credibanco.tarjetas.util.operbigdecimal;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class SubtractBigDecimalTest {

    // ============================
    // PRUEBAS PARA RESTA DE BIGDECIMALS
    // ============================

    @Test
    void shouldSubtractCorrectly() {
        BigDecimal result = SubtractBigDecimal.execute(new BigDecimal("10.00"), new BigDecimal("2.00"));
        assertEquals(new BigDecimal("8.00"), result, "La resta no es correcta.");
    }

    @Test
    void shouldRoundToTwoDecimalsUsingHalfUp() {
        BigDecimal result = SubtractBigDecimal.execute(new BigDecimal("10.555"), new BigDecimal("2.222"));
        assertEquals(new BigDecimal("8.33"), result, "El redondeo no es correcto.");
    }

    @Test
    void shouldHandleNegativeSubtraction() {
        BigDecimal result = SubtractBigDecimal.execute(new BigDecimal("-5.00"), new BigDecimal("2.00"));
        assertEquals(new BigDecimal("-7.00"), result, "La resta con número negativo no es correcta.");
    }

    @Test
    void shouldHandleZeroSubtraction() {
        BigDecimal result = SubtractBigDecimal.execute(BigDecimal.ZERO, new BigDecimal("5.00"));
        assertEquals(new BigDecimal("-5.00"), result, "La resta con 0 no es correcta.");
    }

    @Test
    void shouldHandleLargeNumbers() {
        BigDecimal result = SubtractBigDecimal.execute(new BigDecimal("1000000"), new BigDecimal("314"));
        assertEquals(new BigDecimal("999686.00"), result, "La resta con números grandes no es correcta.");
    }

    // ============================
    // PRUEBAS PARA VALIDACIONES Y EXCEPCIONES
    // ============================

    @Test
    void shouldThrowExceptionWhenFirstNumberIsNull() {
        Exception exception = assertThrows(ArithmeticException.class, () -> SubtractBigDecimal.execute(null, new BigDecimal("10.00")));
        assertEquals("Los terminos no pueden ser null", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenSecondNumberIsNull() {
        Exception exception = assertThrows(ArithmeticException.class, () -> SubtractBigDecimal.execute(new BigDecimal("10.00"), null));
        assertEquals("Los terminos no pueden ser null", exception.getMessage());
    }
}