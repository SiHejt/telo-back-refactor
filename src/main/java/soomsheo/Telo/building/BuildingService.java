package soomsheo.Telo.building;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import soomsheo.Telo.member.domain.Member;
import soomsheo.Telo.building.domain.Building;
import soomsheo.Telo.member.MemberRepository;
import soomsheo.Telo.resident.ResidentRepository;
import soomsheo.Telo.member.MemberService;
import soomsheo.Telo.util.EncryptionUtil;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BuildingService {

    private final BuildingRepository buildingRepository;
    private final MemberService memberService;
    private final MemberRepository memberRepository;

    @Transactional
    public void saveBuilding(Building building, String realName, String phoneNumber) throws Exception {
        String landlordMemberID = building.getLandlord().getMemberID();
        Member landlord = memberService.findByMemberID(landlordMemberID);

        if (landlord != null && (landlord.getEncryptedPhoneNumber() == null || landlord.getEncryptedPhoneNumber().isEmpty())) {
            landlord.setEncryptedPhoneNumber(EncryptionUtil.encrypt(phoneNumber));
            landlord.setMemberRealName(realName);
            memberRepository.save(landlord);
        }
        buildingRepository.save(building);
    }

    public Optional<Building> findByBuildingID(UUID buildingID) {
        return buildingRepository.findByBuildingID(buildingID);
    }

    public List<Building> getAllBuildings() {
        return buildingRepository.findAll();
    }

    public List<Building> findByLandlordID(String landlordMemberID) {
        return buildingRepository.findByLandlord_MemberID(landlordMemberID);
    }

    @Transactional
    public Building updateNotice(UUID buildingId, String notice) {
        Building building = buildingRepository.findByBuildingID(buildingId).orElse(null);
        if (building != null) {
            building.setNotice(notice);
            return buildingRepository.save(building);
        }
        return null;
    }

    public Optional<Building> getBuildingById(UUID buildingID) {
        return buildingRepository.findByBuildingID(buildingID);
    }

    @Transactional
    public void incrementRentedHouseholds(UUID buildingID) {
        buildingRepository.findByBuildingID(buildingID).ifPresent(building -> {
            building.setNumberOfRentedHouseholds(building.getNumberOfRentedHouseholds() + 1);
            buildingRepository.save(building);
        });
    }

    public List<String> findMatchingBuildingAddresses(String partialAddress) {
        return buildingRepository.findByBuildingAddressContaining(partialAddress)
                .stream()
                .map(Building::getBuildingAddress)
                .toList();
    }
}
