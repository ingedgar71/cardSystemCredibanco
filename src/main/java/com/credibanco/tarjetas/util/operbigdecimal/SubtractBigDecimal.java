package com.credibanco.tarjetas.util.operbigdecimal;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class SubtractBigDecimal{

    private SubtractBigDecimal(){}

    public static BigDecimal execute(BigDecimal num1, BigDecimal num2) {
        if(num1 == null || num2 == null){
            throw new ArithmeticException("Los terminos no pueden ser null");
        }
        return num1.subtract(num2).setScale(2, RoundingMode.HALF_UP);

    }
}
