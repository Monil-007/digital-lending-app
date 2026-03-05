package com.example.digitalLendingApp.domain.template;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Component
public class StandardEmiCalculator extends AbstractEmiCalculator {

    @Override
    protected BigDecimal compute(BigDecimal p, BigDecimal r, int n) {

        BigDecimal onePlusRPowerN =
                (BigDecimal.ONE.add(r)).pow(n);

        BigDecimal numerator = p.multiply(r).multiply(onePlusRPowerN);

        BigDecimal denominator =
                onePlusRPowerN.subtract(BigDecimal.ONE);

        return numerator.divide(denominator, 10, RoundingMode.HALF_UP);
    }
}
