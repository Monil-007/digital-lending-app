package com.example.digitalLendingApp;

import com.example.digitalLendingApp.domain.factory.InterestPremiumStrategyFactory;
import com.example.digitalLendingApp.domain.strategy.EmploymentPremiumStrategy;
import com.example.digitalLendingApp.domain.strategy.InterestPremiumStrategy;
import com.example.digitalLendingApp.domain.strategy.LoanSizePremiumStrategy;
import com.example.digitalLendingApp.domain.strategy.RiskPremiumStrategy;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InterestPremiumStrategyFactoryTest {

    @Test
    void testGetStrategiesReturnsCorrectOrder() {
        InterestPremiumStrategyFactory factory = new InterestPremiumStrategyFactory();
        List<InterestPremiumStrategy> strategies = factory.getStrategies();
        
        assertNotNull(strategies);
        assertEquals(3, strategies.size());
        
        assertTrue(strategies.get(0) instanceof RiskPremiumStrategy);
        assertTrue(strategies.get(1) instanceof EmploymentPremiumStrategy);
        assertTrue(strategies.get(2) instanceof LoanSizePremiumStrategy);
    }

    @Test
    void testGetStrategiesReturnsNewInstances() {
        InterestPremiumStrategyFactory factory1 = new InterestPremiumStrategyFactory();
        InterestPremiumStrategyFactory factory2 = new InterestPremiumStrategyFactory();
        
        List<InterestPremiumStrategy> strategies1 = factory1.getStrategies();
        List<InterestPremiumStrategy> strategies2 = factory2.getStrategies();
        
        assertNotSame(strategies1, strategies2);
        assertEquals(strategies1.size(), strategies2.size());
        
        for (int i = 0; i < strategies1.size(); i++) {
            assertNotSame(strategies1.get(i), strategies2.get(i));
            assertEquals(strategies1.get(i).getClass(), strategies2.get(i).getClass());
        }
    }

    @Test
    void testGetStrategiesReturnsImmutableList() {
        InterestPremiumStrategyFactory factory = new InterestPremiumStrategyFactory();
        List<InterestPremiumStrategy> strategies = factory.getStrategies();
        
        assertThrows(UnsupportedOperationException.class, () -> {
            strategies.add(new EmploymentPremiumStrategy());
        });
        
        assertThrows(UnsupportedOperationException.class, () -> {
            strategies.remove(0);
        });
        
        assertThrows(UnsupportedOperationException.class, () -> {
            strategies.clear();
        });
    }

    @Test
    void testFactoryConsistency() {
        InterestPremiumStrategyFactory factory = new InterestPremiumStrategyFactory();
        
        List<InterestPremiumStrategy> strategies1 = factory.getStrategies();
        List<InterestPremiumStrategy> strategies2 = factory.getStrategies();
        
        assertEquals(strategies1.size(), strategies2.size());
        
        for (int i = 0; i < strategies1.size(); i++) {
            assertEquals(strategies1.get(i).getClass(), strategies2.get(i).getClass());
        }
    }
}
