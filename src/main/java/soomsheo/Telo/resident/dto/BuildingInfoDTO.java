package soomsheo.Telo.resident.dto;

import soomsheo.Telo.building.domain.Building;
import soomsheo.Telo.member.domain.Member;
import soomsheo.Telo.util.EncryptionUtil;

import java.util.List;
import java.util.UUID;

public record BuildingInfoDTO(
        UUID buildingID,
        String buildingName,
        String buildingAddress,
        String notice,
        String landlordMemberID,
        String landlordRealName,
        List<String> buildingImageURL
) {
    public static BuildingInfoDTO fromEntity(Building building) {
        Member landlord = building.getLandlord();
        String landlordMemberId = (landlord != null) ? landlord.getMemberID() : null;
        String landlordRealName = (landlord != null) ? landlord.getMemberRealName() : null;

        return new BuildingInfoDTO(
                building.getBuildingID(),
                building.getBuildingName(),
                building.getBuildingAddress(),
                building.getNotice(),
                landlordMemberId,
                landlordRealName,
                building.getImageURL()
        );
    }
}