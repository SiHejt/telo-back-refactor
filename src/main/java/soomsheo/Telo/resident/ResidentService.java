package soomsheo.Telo.resident;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import soomsheo.Telo.member.domain.Member;
import soomsheo.Telo.building.domain.Building;
import soomsheo.Telo.building.domain.Resident;
import soomsheo.Telo.resident.dto.ResidentDTO;
import soomsheo.Telo.resident.dto.ResidentRegisterDTO;
import soomsheo.Telo.building.BuildingRepository;
import soomsheo.Telo.member.MemberRepository;
import soomsheo.Telo.util.EncryptionUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ResidentService {
    private final ResidentRepository residentRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public void saveResident(Resident resident, String realName, String phoneNumber) throws Exception {
        Member tenant = resident.getTenant();
        if(tenant.getEncryptedPhoneNumber() == null || tenant.getEncryptedPhoneNumber().isEmpty()) {
            tenant.setEncryptedPhoneNumber(EncryptionUtil.encrypt(phoneNumber));
            tenant.setMemberRealName(realName);
            memberRepository.save(tenant);
        };
        residentRepository.save(resident);
    }

    public Optional<Resident> findByResidentID(UUID residentID) {
        return residentRepository.findByResidentID(residentID);
    }

    public List<ResidentRegisterDTO> getAllResidents(UUID buildingID) {
        List<Resident> residents = residentRepository.findByBuilding_BuildingID(buildingID);
        return residents.stream()
                .map(ResidentRegisterDTO::fromEntity)
                .toList();
    }

    public List<ResidentDTO> getResidentsByMemberID(String memberID) {
        List<Resident> residents = residentRepository.findAllWithBuildingByTenantMemberID(memberID);
        return residents.stream()
                .map(ResidentDTO::fromEntity)
                .toList();
    }

    public List<Building> getBuildingsByTenantIDAndLandlordID(String tenantID, String landlordID) {
        return residentRepository.findBuildingsByTenantMemberIDAndLandlordMemberID(tenantID, landlordID);
    }


    public List<Resident> getResidentsByTenantIdAndLandlordId(String tenantID, String landlordID) {
        return residentRepository.findResidentsByTenantMemberIDAndLandlordMemberID(tenantID, landlordID);
    }
}
