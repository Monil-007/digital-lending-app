package com.example.digitalLendingApp.domain.strategy;

import com.example.digitalLendingApp.domain.enums.RiskBand;

public class CreditScoreRiskStrategy implements RiskClassificationStrategy {

    @Override
    public RiskBand classify(int score) {
        if (score >= 750) return RiskBand.LOW;
        if (score >= 650) return RiskBand.MEDIUM;
        return RiskBand.HIGH;
    }
}
