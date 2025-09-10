package soomsheo.Telo.building.dto;

import soomsheo.Telo.building.domain.Building;
import soomsheo.Telo.member.domain.Member;

import java.util.List;
import java.util.UUID;

public record BuildingResponse(
        UUID buildingID,
        String buildingName,
        String buildingAddress,
        int numberOfHouseholds,
        int numberOfRentedHouseholds,
        List<String> imageURL,
        String landlordMemberID,
        String landlordRealName,
        String notice
) {
    public static BuildingResponse from(Building building, Member landlord) {
        String memberId = (building.getLandlord() != null) ? building.getLandlord().getMemberID() : null;
        String realName = (landlord != null) ? landlord.getMemberRealName() : null;

        return new BuildingResponse(
                building.getBuildingID(),
                building.getBuildingName(),
                building.getBuildingAddress(),
                building.getNumberOfHouseholds(),
                building.getNumberOfRentedHouseholds(),
                building.getImageURL(),
                memberId,
                realName,
                building.getNotice()
        );
    }

    public static BuildingResponse from(Building building) {
        String memberId = (building.getLandlord() != null) ? building.getLandlord().getMemberID() : null;

        return new BuildingResponse(
                building.getBuildingID(),
                building.getBuildingName(),
                building.getBuildingAddress(),
                building.getNumberOfHouseholds(),
                building.getNumberOfRentedHouseholds(),
                building.getImageURL(),
                memberId,
                null,
                building.getNotice()
        );
    }
}