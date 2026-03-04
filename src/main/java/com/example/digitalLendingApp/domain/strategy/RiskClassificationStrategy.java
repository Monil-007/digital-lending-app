package com.example.digitalLendingApp.domain.strategy;

import com.example.digitalLendingApp.domain.enums.RiskBand;

public interface RiskClassificationStrategy {
    RiskBand classify(int creditScore);
}
