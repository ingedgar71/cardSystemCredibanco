package com.credibanco.tarjetas.util;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class DateUtilTest {

    @Test
    void shouldReturnCorrectDaysInMonth() {
        assertEquals(31, DateUtil.getDaysInMonth(1, 2023)); // Enero
        assertEquals(28, DateUtil.getDaysInMonth(2, 2023)); // Febrero no bisiesto
        assertEquals(29, DateUtil.getDaysInMonth(2, 2024)); // Febrero bisiesto
        assertEquals(30, DateUtil.getDaysInMonth(4, 2023)); // Abril
        assertEquals(31, DateUtil.getDaysInMonth(12, 2023)); // Diciembre
    }

    @Test
    void shouldThrowExceptionForInvalidMonth() {
        assertThrows(IllegalArgumentException.class, () -> DateUtil.getDaysInMonth(0, 2023));
        assertThrows(IllegalArgumentException.class, () -> DateUtil.getDaysInMonth(13, 2023));
    }

    @Test
    void shouldThrowExceptionForInvalidYear() {
        assertThrows(IllegalArgumentException.class, () -> DateUtil.getDaysInMonth(5, 1999));
        assertThrows(IllegalArgumentException.class, () -> DateUtil.getDaysInMonth(5, 2201));
    }

    // ============================
    // PRUEBAS PARA getLocalDate()
    // ============================

    @Test
    void shouldReturnValidLocalDate() {
        LocalDate date = DateUtil.getLocalDate(2023, 5, 15);
        assertEquals(2023, date.getYear());
        assertEquals(5, date.getMonthValue());
        assertEquals(15, date.getDayOfMonth());
    }

    @Test
    void shouldThrowExceptionForInvalidYearInLocalDate() {
        assertThrows(IllegalArgumentException.class, () -> DateUtil.getLocalDate(0, 5, 10));
        assertThrows(IllegalArgumentException.class, () -> DateUtil.getLocalDate(10000, 5, 10));
    }

    @Test
    void shouldThrowExceptionForInvalidMonthInLocalDate() {
        assertThrows(IllegalArgumentException.class, () -> DateUtil.getLocalDate(2023, 0, 10));
        assertThrows(IllegalArgumentException.class, () -> DateUtil.getLocalDate(2023, 13, 10));
    }

    @Test
    void shouldThrowExceptionForInvalidDayInLocalDate() {
        assertThrows(IllegalArgumentException.class, () -> DateUtil.getLocalDate(2023, 5, 0));
        assertThrows(IllegalArgumentException.class, () -> DateUtil.getLocalDate(2023, 5, 32));
    }

    @Test
    void shouldThrowExceptionForInvalidDate() {
        assertThrows(IllegalArgumentException.class, () -> DateUtil.getLocalDate(2023, 2, 30)); // Febrero no tiene 30
        assertThrows(IllegalArgumentException.class, () -> DateUtil.getLocalDate(2023, 4, 31)); // Abril no tiene 31
    }

    // ============================
    // PRUEBAS PARA getPlusYears()
    // ============================

    @Test
    void shouldAddYearsAndSetLastDayOfMonth() {
        LocalDate date = LocalDate.of(2023, 5, 15);
        LocalDate result = DateUtil.getPlusYears(date, 2);

        assertEquals(2025, result.getYear());
        assertEquals(5, result.getMonthValue());
        assertEquals(31, result.getDayOfMonth()); // Último día de mayo es 31
    }

    @Test
    void shouldHandleLeapYearWhenAddingYears() {
        LocalDate date = LocalDate.of(2020, 2, 29);
        LocalDate result = DateUtil.getPlusYears(date, 4); // Año bisiesto siguiente

        assertEquals(2024, result.getYear());
        assertEquals(2, result.getMonthValue());
        assertEquals(29, result.getDayOfMonth());
    }

    @Test
    void shouldHandleNonLeapYearWhenAddingYears() {
        LocalDate date = LocalDate.of(2020, 2, 29);
        LocalDate result = DateUtil.getPlusYears(date, 1); // 2021 no es bisiesto

        assertEquals(2021, result.getYear());
        assertEquals(2, result.getMonthValue());
        assertEquals(28, result.getDayOfMonth()); // Cambia a 28 porque 2021 no es bisiesto
    }

}