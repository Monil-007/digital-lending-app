package com.example.digitalLendingApp;

import com.example.digitalLendingApp.domain.model.LoanContext;
import com.example.digitalLendingApp.domain.specification.AgeTenureSpecification;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

public class AgeTenureSpecificationTest {

    @Test
    void testAgeTenureWithinLimit() {
        AgeTenureSpecification spec = new AgeTenureSpecification();
        LoanContext context = new LoanContext(
                BigDecimal.valueOf(500000),
                60, // 5 years
                30,
                BigDecimal.valueOf(50000),
                com.example.digitalLendingApp.domain.enums.EmploymentType.SALARIED,
                750,
                com.example.digitalLendingApp.domain.enums.RiskBand.LOW
        );

        assertTrue(spec.isSatisfiedBy(context));
    }

    @Test
    void testAgeTenureAtLimit() {
        AgeTenureSpecification spec = new AgeTenureSpecification();
        LoanContext context = new LoanContext(
                BigDecimal.valueOf(500000),
                420, // 35 years
                30,
                BigDecimal.valueOf(50000),
                com.example.digitalLendingApp.domain.enums.EmploymentType.SALARIED,
                750,
                com.example.digitalLendingApp.domain.enums.RiskBand.LOW
        );

        assertTrue(spec.isSatisfiedBy(context)); // 30 + 35 = 65
    }

    @Test
    void testAgeTenureExceedsLimit() {
        AgeTenureSpecification spec = new AgeTenureSpecification();
        LoanContext context = new LoanContext(
                BigDecimal.valueOf(500000),
                480, // 40 years
                30,
                BigDecimal.valueOf(50000),
                com.example.digitalLendingApp.domain.enums.EmploymentType.SALARIED,
                750,
                com.example.digitalLendingApp.domain.enums.RiskBand.LOW
        );

        assertFalse(spec.isSatisfiedBy(context)); // 30 + 40 = 70 > 65
        assertEquals("AGE_TENURE_LIMIT_EXCEEDED", spec.reason());
    }

    @Test
    void testOlderApplicantShortTenure() {
        AgeTenureSpecification spec = new AgeTenureSpecification();
        LoanContext context = new LoanContext(
                BigDecimal.valueOf(500000),
                60, // 5 years
                55,
                BigDecimal.valueOf(50000),
                com.example.digitalLendingApp.domain.enums.EmploymentType.SALARIED,
                750,
                com.example.digitalLendingApp.domain.enums.RiskBand.LOW
        );

        assertTrue(spec.isSatisfiedBy(context)); // 55 + 5 = 60 <= 65
    }

    @Test
    void testOlderApplicantLongTenure() {
        AgeTenureSpecification spec = new AgeTenureSpecification();
        LoanContext context = new LoanContext(
                BigDecimal.valueOf(500000),
                240, // 20 years
                50,
                BigDecimal.valueOf(50000),
                com.example.digitalLendingApp.domain.enums.EmploymentType.SALARIED,
                750,
                com.example.digitalLendingApp.domain.enums.RiskBand.LOW
        );

        assertFalse(spec.isSatisfiedBy(context)); // 50 + 20 = 70 > 65
        assertEquals("AGE_TENURE_LIMIT_EXCEEDED", spec.reason());
    }
}
