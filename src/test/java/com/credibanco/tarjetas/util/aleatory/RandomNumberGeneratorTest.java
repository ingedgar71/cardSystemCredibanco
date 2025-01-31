package com.credibanco.tarjetas.util.aleatory;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RandomNumberGeneratorTest {

    @Test
    void shouldGenerateNumberWithExactDigits() {
        int maxDigitos = 10;
        String generatedNumber = RandomNumberGenerator.generateNumber(maxDigitos);
        assertEquals(maxDigitos, generatedNumber.length(), "El número generado no tiene la longitud esperada.");
    }

    @Test
    void shouldThrowExceptionWhenDigitsIsZeroOrNegative(){
        assertThrows(IllegalArgumentException.class, () -> RandomNumberGenerator.generateNumber(0));
        assertThrows(IllegalArgumentException.class, () -> RandomNumberGenerator.generateNumber(-5));
    }

    @Test
    void shouldOnlyContainNumericCharacters() {
        int maxDigitos = 8;
        String generatedNumber = RandomNumberGenerator.generateNumber(maxDigitos);

        assertTrue(generatedNumber.matches("\\d+"), "El número generado contiene caracteres no numéricos.");
    }

    @Test
    void shouldGenerateNumberWithinRange() {
        int maxDigitos = 5;
        long maxValue = (long) Math.pow(10, maxDigitos) - 1;
        long generatedNumber = Long.parseLong(RandomNumberGenerator.generateNumber(maxDigitos));

        assertTrue(generatedNumber >= 0 && generatedNumber <= maxValue,
                "El número generado está fuera del rango permitido.");
    }
}