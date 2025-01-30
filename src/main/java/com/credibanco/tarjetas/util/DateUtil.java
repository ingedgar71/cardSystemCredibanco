package com.credibanco.tarjetas.util;

import java.time.LocalDate;
import java.time.YearMonth;

public class DateUtil {

    /**
     * Método que retorna el número de días de un mes dado el mes y el año
     */
    public static int getDaysInMonth(int month, int year) {
        // Validar el rango del mes
        if (month < 1 || month > 12) {
            throw new IllegalArgumentException("El mes " + month + " no es válido. Debe estar entre 1 y 12.");
        }

        // Validar el rango del año (opcional, depende de tu contexto)
        if (year < 2000 || year > 2200) {
            throw new IllegalArgumentException("El año " + year + " no es válido.");
        }

        // Usar YearMonth para calcular los días del mes
        YearMonth yearMonth = YearMonth.of(year, month);
        return yearMonth.lengthOfMonth();
    }

    /**
     * Metodo que retorna un objeto tipo LocalDate dado el año - mes y dia
     */
    public static LocalDate getLocalDate(int year, int month, int day) {
        // Validar que el año, mes y día sean valores válidos
        if (year < 1 || year > 9999) {
            throw new IllegalArgumentException("El año debe estar entre 1 y 9999.");
        }
        if (month < 1 || month > 12) {
            throw new IllegalArgumentException("El mes debe estar entre 1 y 12.");
        }
        if (day < 1 || day > 31) {
            throw new IllegalArgumentException("El día debe estar entre 1 y 31.");
        }

        // Crear el LocalDate
        try {
            return LocalDate.of(year, month, day);
        } catch (Exception e) {
            throw new IllegalArgumentException("La fecha no es válida: " + e.getMessage());
        }
    }
    /**
     * Método que suma años a un LocalDate dado.
     * El dia de la fecha devuelta corresponde al ultimo dia del mes
     * */
    public static LocalDate getPlusYears(LocalDate date, int years){
        int diasMes = date.lengthOfMonth();
        int mes = date.getMonth().getValue();
        int anio = date.getYear();

        return getLocalDate(anio, mes, diasMes);
    }


}
