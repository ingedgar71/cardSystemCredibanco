package com.credibanco.tarjetas.util.operBigDecimal;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class MultiplyBigDecimal{

    public static BigDecimal execute(BigDecimal num1, BigDecimal num2) {

        if(num1 == null || num2 == null){
            throw new ArithmeticException("Los terminos no pueden ser null");
        }

        BigDecimal resul = num1.multiply(num2).setScale(2, RoundingMode.HALF_UP);
        return resul;
    }
}
