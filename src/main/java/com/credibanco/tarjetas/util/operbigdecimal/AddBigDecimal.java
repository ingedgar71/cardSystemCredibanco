package com.credibanco.tarjetas.util.operbigdecimal;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class AddBigDecimal{

    private AddBigDecimal() {}

    public static BigDecimal execute(BigDecimal... numbers) {
        if(numbers == null || numbers.length == 0){
            throw new ArithmeticException("Debe proporcionar al menos un número.");
        }

        BigDecimal result = BigDecimal.ZERO;

        for(BigDecimal number : numbers){
            if(number == null){
                throw new ArithmeticException("Los términos no pueden ser null.");
            }
            result = result.add(number);
        }
        return result.setScale(2, RoundingMode.HALF_UP);
    }
}
