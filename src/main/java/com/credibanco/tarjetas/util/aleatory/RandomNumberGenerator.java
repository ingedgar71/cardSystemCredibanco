package com.credibanco.tarjetas.util.aleatory;

import java.util.Random;

public class RandomNumberGenerator {

    public static String generateNumber(int maxDigitos) {
        if (maxDigitos <= 0) {
            throw new IllegalArgumentException("El número máximo de dígitos debe ser mayor que 0.");
        }

        // Definir el rango del número aleatorio (mínimo 0, máximo 10^maxDigitos - 1)
        int maxValue = (int) Math.pow(10, maxDigitos) - 1;
        int numeroAleatorio = new Random().nextInt(maxValue + 1); // Genera un número en el rango [0, maxValue]

        // Formatear con ceros a la izquierda
        return String.format("%0" + maxDigitos + "d", numeroAleatorio);
    }
}
