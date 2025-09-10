package soomsheo.Telo.building.dto;

import java.util.List;

public record BuildingCreateRequest(
        String buildingName,
        String buildingAddress,
        int numberOfHouseholds,
        int numberOfRentedHouseholds,
        List<String> imageURL,
        String landlordID,
        String memberRealName,
        String phoneNumber
) {}