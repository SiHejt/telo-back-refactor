package soomsheo.Telo.resident.dto;

import java.util.List;

public record ResidentCreationRequest(
        String residentName,
        String phoneNumber,
        String apartmentNumber,
        String rentType,
        String monthlyRentAmount,
        String monthlyRentPaymentDate,
        String deposit,
        String contractExpirationDate,
        List<String> contractImageURL
) {}