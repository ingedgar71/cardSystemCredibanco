package com.credibanco.tarjetas.util.operBigDecimal;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class PercentageBigDecimal{



    public static BigDecimal execute(BigDecimal base, BigDecimal percentage) {
        if(base == null || percentage == null){
            throw new ArithmeticException("Los terminos no pueden ser null");
        }
        return base.multiply(percentage).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
    }
}
