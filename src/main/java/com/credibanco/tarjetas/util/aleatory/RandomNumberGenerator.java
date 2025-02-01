package com.credibanco.tarjetas.util.aleatory;

import java.security.SecureRandom;

public class RandomNumberGenerator {

    private static final SecureRandom RANDOM = new SecureRandom();

    private RandomNumberGenerator() {
    }

    public static String generateNumber(int maxDigitos) {
        if (maxDigitos <= 0) {
            throw new IllegalArgumentException("El número máximo de dígitos debe ser mayor que 0.");
        }

        StringBuilder sb = new StringBuilder();

        // Definir el rango del número aleatorio (mínimo 0, máximo 10^maxDigitos - 1)
        long maxValue = (long) Math.pow(10, maxDigitos) - 1;
        long numeroAleatorio;
        do {
            numeroAleatorio = RANDOM.nextLong(maxValue + 1l); // Genera un número en el rango [0, maxValue]
        } while (numeroAleatorio == 0l);

        int cerosFaltantes = maxDigitos - String.valueOf(numeroAleatorio).length();

        for (int i = 0; i < cerosFaltantes; i++) {
            sb.append('0');
        }

        sb.append(numeroAleatorio);

        return sb.toString();
    }
}
