package soomsheo.Telo.resident.dto;

import soomsheo.Telo.building.domain.Resident;
import soomsheo.Telo.resident.dto.BuildingInfoDTO;

import java.util.List;

public record ResidentDTO(
        String apartmentNumber,
        String rentType,
        String monthlyRentAmount,
        String monthlyRentPaymentDate,
        String deposit,
        String contractExpirationDate,
        List<String> contractImageURL,
        BuildingInfoDTO buildingInfo // Building 객체 대신 BuildingInfoDTO를 사용
) {
    public static ResidentDTO fromEntity(Resident resident) {
        return new ResidentDTO(
                resident.getApartmentNumber(),
                resident.getRentType(),
                resident.getMonthlyRentAmount(),
                resident.getMonthlyRentPaymentDate(),
                resident.getDeposit(),
                resident.getContractExpirationDate(),
                resident.getContractImageURL(),
                BuildingInfoDTO.fromEntity(resident.getBuilding()) // 위에서 만든 DTO 사용
        );
    }
}