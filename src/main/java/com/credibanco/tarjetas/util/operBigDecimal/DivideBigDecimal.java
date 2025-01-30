package com.credibanco.tarjetas.util.operBigDecimal;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;

public class DivideBigDecimal{

    public static BigDecimal execute(BigDecimal num1, BigDecimal num2) {

        if(num2 == null || num2.compareTo(new BigDecimal(BigInteger.ZERO)) == 0) {
            throw new ArithmeticException("División por Cero no permitido");
        }

        return num1.divide(num2, 2 ,RoundingMode.HALF_UP);
    }
}
