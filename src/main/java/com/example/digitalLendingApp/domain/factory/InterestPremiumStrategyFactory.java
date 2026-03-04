package com.example.digitalLendingApp.domain.factory;

import com.example.digitalLendingApp.domain.strategy.*;
import java.util.List;

public class InterestPremiumStrategyFactory {

    public List<InterestPremiumStrategy> getStrategies() {
        return List.of(
                new RiskPremiumStrategy(),
                new EmploymentPremiumStrategy(),
                new LoanSizePremiumStrategy()
        );
    }
}
