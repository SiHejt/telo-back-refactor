package soomsheo.Telo.building.dto;

import java.util.UUID;

public record NoticeUpdateRequest(
        UUID buildingID,
        String notice
) {}