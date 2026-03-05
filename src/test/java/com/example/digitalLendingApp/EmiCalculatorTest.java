package com.example.digitalLendingApp;

import com.example.digitalLendingApp.domain.template.StandardEmiCalculator;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

public class EmiCalculatorTest {

    @Test
    void testEmiCalculation() {

        StandardEmiCalculator calculator =
                new StandardEmiCalculator();

        BigDecimal emi = calculator.calculate(
                BigDecimal.valueOf(500000),
                BigDecimal.valueOf(12),
                36
        );

        assertNotNull(emi);
        assertEquals(2, emi.scale());
    }
}
