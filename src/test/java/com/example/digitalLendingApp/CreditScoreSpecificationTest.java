package com.example.digitalLendingApp;

import com.example.digitalLendingApp.domain.model.LoanContext;
import com.example.digitalLendingApp.domain.specification.CreditScoreSpecification;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

public class CreditScoreSpecificationTest {

    @Test
    void testCreditScoreAboveMinimum() {
        CreditScoreSpecification spec = new CreditScoreSpecification();
        LoanContext context = new LoanContext(
                BigDecimal.valueOf(500000),
                36,
                30,
                BigDecimal.valueOf(50000),
                com.example.digitalLendingApp.domain.enums.EmploymentType.SALARIED,
                750,
                com.example.digitalLendingApp.domain.enums.RiskBand.LOW
        );

        assertTrue(spec.isSatisfiedBy(context));
    }

    @Test
    void testCreditScoreAtMinimum() {
        CreditScoreSpecification spec = new CreditScoreSpecification();
        LoanContext context = new LoanContext(
                BigDecimal.valueOf(500000),
                36,
                30,
                BigDecimal.valueOf(50000),
                com.example.digitalLendingApp.domain.enums.EmploymentType.SALARIED,
                600,
                com.example.digitalLendingApp.domain.enums.RiskBand.MEDIUM
        );

        assertTrue(spec.isSatisfiedBy(context));
    }

    @Test
    void testCreditScoreBelowMinimum() {
        CreditScoreSpecification spec = new CreditScoreSpecification();
        LoanContext context = new LoanContext(
                BigDecimal.valueOf(500000),
                36,
                30,
                BigDecimal.valueOf(50000),
                com.example.digitalLendingApp.domain.enums.EmploymentType.SALARIED,
                550,
                com.example.digitalLendingApp.domain.enums.RiskBand.HIGH
        );

        assertFalse(spec.isSatisfiedBy(context));
        assertEquals("CREDIT_SCORE_BELOW_MINIMUM", spec.reason());
    }
}
