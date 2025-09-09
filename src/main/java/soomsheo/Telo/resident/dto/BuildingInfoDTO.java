package soomsheo.Telo.resident.dto;

import soomsheo.Telo.building.domain.Building;
import soomsheo.Telo.util.EncryptionUtil;

import java.util.List;
import java.util.UUID;

public record BuildingInfoDTO(
        UUID buildingID,
        String buildingName,
        String buildingAddress,
        String notice,
        String landlordID,
        List<String> buildingImageURL
) {
    public static BuildingInfoDTO fromEntity(Building building) {
        try {
            return new BuildingInfoDTO(
                    building.getBuildingID(),
                    building.getBuildingName(),
                    //EncryptionUtil.decrypt(building.getEncryptedBuildingAddress()),
                    building.getBuildingAddress(),
                    building.getNotice(),
                    building.getLandlordID(),
                    building.getImageURL()
            );
        } catch (Exception e) {
            throw new RuntimeException("Failed to decrypt building address", e);
        }
    }
}