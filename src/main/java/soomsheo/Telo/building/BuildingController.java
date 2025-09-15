package soomsheo.Telo.building;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import soomsheo.Telo.member.domain.Member; // Member 엔티티 import
import soomsheo.Telo.building.domain.Building;
import soomsheo.Telo.building.dto.BuildingCreateRequest;
import soomsheo.Telo.building.dto.BuildingResponse;
import soomsheo.Telo.building.dto.NoticeUpdateRequest;
import soomsheo.Telo.member.MemberService;

import java.security.NoSuchAlgorithmException;
import java.util.*;

@Tag(name = "건물 관리", description = "건물 등록, 조회, 수정 등 관련 API")
@RestController
@RequestMapping("/api/buildings")
public class BuildingController {
    private final BuildingService buildingService;
    private final MemberService memberService;
    private final BuildingRepository buildingRepository;

    public BuildingController(BuildingService buildingService, MemberService memberService, BuildingRepository buildingRepository) {
        this.buildingService = buildingService;
        this.memberService = memberService;
        this.buildingRepository = buildingRepository;
    }

    @PostMapping("/landlord/building-register")
    public ResponseEntity<BuildingResponse> createBuilding(@RequestBody BuildingCreateRequest buildingCreateRequest) {
        try {
            Member landlord = memberService.findByMemberID(buildingCreateRequest.landlordID());
            if (landlord == null) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

            Building building = new Building(
                    buildingCreateRequest.buildingName(),
                    buildingCreateRequest.buildingAddress(),
                    buildingCreateRequest.numberOfHouseholds(),
                    buildingCreateRequest.numberOfRentedHouseholds(),
                    buildingCreateRequest.imageURL(),
                    landlord,
                    ""
            );

            buildingService.saveBuilding(building, buildingCreateRequest.memberRealName(), buildingCreateRequest.phoneNumber());

            return new ResponseEntity<>(BuildingResponse.from(building, landlord), HttpStatus.CREATED);
        } catch (NoSuchAlgorithmException e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/landlord/building-list/{landlordID}")
    public ResponseEntity<List<BuildingResponse>> getBuildingsByLandlordID(@PathVariable String landlordID) {
        List<Building> buildings = buildingService.findByLandlordID(landlordID);
        if (buildings.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        List<BuildingResponse> responses = buildings.stream()
                .map(building -> {
                    Member landlord = building.getLandlord();
                    return BuildingResponse.from(building, landlord);
                })
                .toList();
        return new ResponseEntity<>(responses, HttpStatus.OK);
    }

    @GetMapping("/autocomplete")
    @Operation(summary = "건물 이름 자동완성", description = "검색어를 포함하는 건물 목록을 조회하여 자동완성을 지원합니다.")
    @ApiResponse(responseCode = "200", description = "조회 성공")
    public List<String> autocompleteAddress(@RequestParam String address) {
        return buildingService.findMatchingBuildingAddresses(address);
    }

    @GetMapping("/address-compare")
    public ResponseEntity<BuildingResponse> getBuildingInfo(@RequestParam String buildingAddress) {
        try {
            List<Building> matchingBuildings = buildingRepository.findByBuildingAddressContaining(buildingAddress);

            if (!matchingBuildings.isEmpty()) {
                Building foundBuilding = matchingBuildings.stream()
                        .filter(b -> b.getBuildingAddress().trim().equals(buildingAddress.trim()))
                        .findFirst()
                        .orElse(null);

                if (foundBuilding != null) {
                    Member landlord = foundBuilding.getLandlord();
                    return new ResponseEntity<>(BuildingResponse.from(foundBuilding, landlord), HttpStatus.OK);
                }
            }
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{buildingID}")
    public ResponseEntity<BuildingResponse> getBuilding(@PathVariable UUID buildingID) {
        try {
            Optional<Building> buildingOptional = buildingService.getBuildingById(buildingID);
            return buildingOptional.map(building -> {
                        Member landlord = building.getLandlord();
                        return new ResponseEntity<>(BuildingResponse.from(building, landlord), HttpStatus.OK);
                    })
                    .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PatchMapping("/{buildingID}/notice")
    @Operation(summary = "건물 공지사항 수정", description = "특정 건물의 공지사항을 수정합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "수정 성공"),
            @ApiResponse(responseCode = "404", description = "해당 건물을 찾을 수 없음")
    })
    public void updateNotice(
            @Parameter(name = "buildingID", description = "공지를 수정할 건물의 ID", required = true) @PathVariable Long buildingID,
            @RequestBody NoticeUpdateRequest noticeUpdateRequest) {
        // 공지 수정 로직
    }
    public ResponseEntity<BuildingResponse> updateNotice(@PathVariable UUID buildingID, @RequestBody NoticeUpdateRequest dto) {
        try {
            Building updatedBuilding = buildingService.updateNotice(buildingID, dto.notice());
            if (updatedBuilding != null) {
                Member landlord = updatedBuilding.getLandlord();
                return ResponseEntity.ok(BuildingResponse.from(updatedBuilding, landlord));
            }
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}