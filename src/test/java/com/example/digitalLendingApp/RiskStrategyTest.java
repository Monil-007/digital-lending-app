package com.example.digitalLendingApp;


import com.example.digitalLendingApp.domain.enums.RiskBand;
import com.example.digitalLendingApp.domain.strategy.CreditScoreRiskStrategy;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class RiskStrategyTest {

    @Test
    void testRiskBand() {
        CreditScoreRiskStrategy strategy =
                new CreditScoreRiskStrategy();

        assertEquals(RiskBand.LOW, strategy.classify(800));
        assertEquals(RiskBand.MEDIUM, strategy.classify(700));
        assertEquals(RiskBand.HIGH, strategy.classify(620));
    }
}
