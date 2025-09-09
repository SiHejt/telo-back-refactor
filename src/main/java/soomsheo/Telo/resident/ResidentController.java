package soomsheo.Telo.resident;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import soomsheo.Telo.member.domain.Member;
import soomsheo.Telo.building.domain.Building;
import soomsheo.Telo.building.domain.Resident;
import soomsheo.Telo.resident.dto.ResidentCreationRequest;
import soomsheo.Telo.resident.dto.ResidentDTO;
import soomsheo.Telo.resident.dto.ResidentRegisterDTO;
import soomsheo.Telo.building.BuildingService;
import soomsheo.Telo.member.MemberService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/residents")
public class ResidentController {
    private final ResidentService residentService;
    private final BuildingService buildingService;
    private final MemberService memberService;

    public ResidentController(ResidentService residentService, BuildingService buildingService, MemberService memberService) {
        this.residentService = residentService;
        this.buildingService = buildingService;
        this.memberService = memberService;
    }

    @GetMapping("/landlord/resident-list/{buildingID}")
    public ResponseEntity<List<ResidentRegisterDTO>> getResidentsByBuildingID(@PathVariable UUID buildingID) throws Exception {
        List<ResidentRegisterDTO> residents = residentService.getAllResidents(buildingID);

        if (residents.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(residents);
    }

    @GetMapping("/tenant/resident-list/{tenantID}")
    public ResponseEntity<List<ResidentDTO>> getResidentsByMemberID(@PathVariable String tenantID) {
        try {
            List<ResidentDTO> residentDTOs = residentService.getResidentsByMemberID(tenantID);
            return ResponseEntity.ok(residentDTOs);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PostMapping("/tenant/resident-register/{buildingID}/{tenantID}")
    public ResponseEntity<Resident> createResident(
            @PathVariable UUID buildingID,
            @PathVariable String tenantID,
            @RequestBody ResidentCreationRequest request
    ) throws Exception {
        Building building = buildingService.findByBuildingID(buildingID);
        Member tenant = memberService.findByMemberID(tenantID);

        if (building == null || tenant == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Resident resident = new Resident(
                tenant,
                request.apartmentNumber(), // <--- request.get...() 대신 record 접근 방식 사용
                request.rentType(),
                request.monthlyRentAmount(),
                request.monthlyRentPaymentDate(),
                request.deposit(),
                request.contractExpirationDate(),
                building,
                request.contractImageURL()
        );

        residentService.saveResident(resident, request.residentName(), request.phoneNumber());

        buildingService.incrementRentedHouseholds(buildingID);

        return new ResponseEntity<>(resident, HttpStatus.CREATED);
    }

    @GetMapping("/{tenantID}/{landlordID}")
    public List<Resident> getResidentsByTenantIdAndLandlordId(@PathVariable String tenantID, @PathVariable String landlordID) {
        return residentService.getResidentsByTenantIdAndLandlordId(tenantID, landlordID);
    }
}
