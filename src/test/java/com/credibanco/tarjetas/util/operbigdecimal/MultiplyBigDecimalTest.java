package com.credibanco.tarjetas.util.operbigdecimal;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static org.junit.jupiter.api.Assertions.*;

class MultiplyBigDecimalTest {

    // ============================
    // PRUEBAS PARA MULTIPLICACIÓN DE BIGDECIMALS
    // ============================

    @Test
    void shouldMultiplyCorrectly() {
        BigDecimal result = MultiplyBigDecimal.execute(new BigDecimal("5.00"), new BigDecimal("2.00"));
        assertEquals(new BigDecimal("10.00"), result, "La multiplicación no es correcta.");
    }

    @Test
    void shouldRoundToTwoDecimalsUsingHalfUp() {
        BigDecimal result = MultiplyBigDecimal.execute(new BigDecimal("10.555"), new BigDecimal("2"));
        assertEquals(new BigDecimal("21.11"), result, "El redondeo no es correcto.");
    }

    @Test
    void shouldHandleNegativeMultiplication() {
        BigDecimal result = MultiplyBigDecimal.execute(new BigDecimal("-5.00"), new BigDecimal("2.00"));
        assertEquals(new BigDecimal("-10.00"), result, "La multiplicación con número negativo no es correcta.");
    }

    @Test
    void shouldHandleZeroMultiplication() {
        BigDecimal result = MultiplyBigDecimal.execute(BigDecimal.ZERO, new BigDecimal("5.00"));
        assertEquals(BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP), result, "La multiplicación con 0 no es correcta.");
    }

    @Test
    void shouldHandleLargeNumbers() {
        BigDecimal result = MultiplyBigDecimal.execute(new BigDecimal("1000000"), new BigDecimal("3.14"));
        assertEquals(new BigDecimal("3140000.00"), result, "La multiplicación con números grandes no es correcta.");
    }

    // ============================
    // PRUEBAS PARA VALIDACIONES Y EXCEPCIONES
    // ============================

    @Test
    void shouldThrowExceptionWhenFirstNumberIsNull() {
        Exception exception = assertThrows(ArithmeticException.class, () -> MultiplyBigDecimal.execute(null, new BigDecimal("10.00")));
        assertEquals("Los terminos no pueden ser null", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenSecondNumberIsNull() {
        Exception exception = assertThrows(ArithmeticException.class, () -> MultiplyBigDecimal.execute(new BigDecimal("10.00"), null));
        assertEquals("Los terminos no pueden ser null", exception.getMessage());
    }
}