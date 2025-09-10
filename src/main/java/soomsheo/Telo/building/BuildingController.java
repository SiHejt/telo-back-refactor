package soomsheo.Telo.building;

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