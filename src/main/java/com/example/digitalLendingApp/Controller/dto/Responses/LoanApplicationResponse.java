package com.example.digitalLendingApp.Controller.dto.Responses;

import com.example.digitalLendingApp.domain.enums.RiskBand;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class LoanApplicationResponse {

    private UUID applicationId;
    private String status;
    private RiskBand riskBand;
    private OfferResponse offer;
    private List<String> rejectionReasons;

}
