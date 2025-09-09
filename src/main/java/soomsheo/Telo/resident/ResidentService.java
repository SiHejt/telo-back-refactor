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
import java.util.UUID;

@Service
@RequiredArgsConstructor // final 필드에 대한 생성자 자동 생성
@Transactional(readOnly = true) // 기본적으로 모든 메소드는 읽기 전용 트랜잭션으로 설정
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

    public Resident findByResidentID(UUID residentID) {
        return residentRepository.findByResidentID(residentID);
    }

    public List<ResidentRegisterDTO> getAllResidents(UUID buildingID) {
        // N+1 문제를 해결한 새로운 메소드 사용
        List<Resident> residents = residentRepository.findAllWithTenantByBuildingID(buildingID);
        // Java Stream API를 사용하여 DTO로 변환
        return residents.stream()
                .map(ResidentRegisterDTO::fromEntity)
                .toList();
    }

    public List<ResidentDTO> getResidentsByMemberID(String memberID) {
        // N+1 문제를 해결한 새로운 메소드 사용
        List<Resident> residents = residentRepository.findAllWithBuildingByTenantMemberID(memberID);
        // Java Stream API를 사용하여 DTO로 변환
        return residents.stream()
                .map(ResidentDTO::fromEntity)
                .toList();
    }

    public List<Building> getBuildingsByTenantIDAndLandlordID (String tenantID, String landlordID) {
        return residentRepository.findBuildingsByTenantIdAndLandlordId(tenantID, landlordID);
    }

    public List<Resident> getResidentsByTenantIdAndLandlordId (String tenantID, String landlordID) {
        return residentRepository.findResidentsByTenantIdAndLandlordId(tenantID, landlordID);
    }
}
