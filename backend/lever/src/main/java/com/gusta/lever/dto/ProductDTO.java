package com.gusta.lever.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {
    private UUID uuid;
    private String company;
    private String companyEmail;
    private String cnpj;
    private String address;
    private String prodType;
    private Double weight;
    private Integer amount;
    private LocalDate scheduledDate;
    private String warnings;
    private Boolean collected;

    public boolean allNullOrEmpty() {
        return (company == null || company.isEmpty()) &&
                (companyEmail == null || companyEmail.isEmpty()) &&
                (cnpj == null || cnpj.isEmpty()) &&
                (address == null || address.isEmpty()) &&
                (prodType == null || prodType.isEmpty()) &&
                (warnings == null || warnings.isEmpty()) &&
                (scheduledDate == null) &&
                (weight == null || weight == 0.0) &&
                (amount == null || amount == 0) &&
                collected == null;
    }
}
