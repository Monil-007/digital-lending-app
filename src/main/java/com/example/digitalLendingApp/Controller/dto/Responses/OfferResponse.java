package com.example.digitalLendingApp.Controller.dto.Responses;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OfferResponse {

    private BigDecimal interestRate;
    private Integer tenureMonths;
    private BigDecimal emi;
    private BigDecimal totalPayable;

}