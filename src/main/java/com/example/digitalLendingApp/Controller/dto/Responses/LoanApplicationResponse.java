package com.example.digitalLendingApp.Controller.dto.Responses;

import com.example.digitalLendingApp.domain.enums.RiskBand;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LoanApplicationResponse {

    private UUID applicationId;
    private String status;
    private RiskBand riskBand;
    private OfferResponse offer;
    private List<String> rejectionReasons;

}
