package soomsheo.Telo.resident.dto;

import soomsheo.Telo.building.domain.Resident;
import soomsheo.Telo.util.EncryptionUtil;

import java.util.List;
import java.util.UUID;

/**
 * 세입자 정보 조회를 위한 데이터 전송 객체 (Response)
 */
public record ResidentResponse(
        String residentName,
        String phoneNumber,
        String apartmentNumber,
        String rentType,
        String monthlyRentAmount,
        String monthlyRentPaymentDate,
        String deposit,
        String contractExpirationDate,
        UUID buildingID,
        List<String> contractImageURL
) {
    
    public static ResidentResponse fromEntity(Resident resident) {
        try {
            return new ResidentResponse(
                    resident.getTenant().getMemberRealName(),
                    EncryptionUtil.decrypt(resident.getTenant().getEncryptedPhoneNumber()),
                    resident.getApartmentNumber(),
                    resident.getRentType(),
                    resident.getMonthlyRentAmount(),
                    resident.getMonthlyRentPaymentDate(),
                    resident.getDeposit(),
                    resident.getContractExpirationDate(),
                    resident.getBuilding().getBuildingID(),
                    resident.getContractImageURL()
            );
        } catch (Exception e) {
            // 실제 운영 코드에서는 로깅 후 더 구체적인 예외를 던져야함
            throw new RuntimeException("Failed to create ResidentResponse DTO from entity", e);
        }
    }
}