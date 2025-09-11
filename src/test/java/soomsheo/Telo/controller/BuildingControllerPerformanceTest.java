package soomsheo.Telo.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import soomsheo.Telo.building.BuildingController;
import soomsheo.Telo.building.domain.Building;
import soomsheo.Telo.building.BuildingRepository;
import soomsheo.Telo.building.domain.Resident;
import soomsheo.Telo.building.dto.BuildingResponse; // BuildingResponse DTO 임포트
import soomsheo.Telo.member.domain.Member;
import soomsheo.Telo.member.MemberRepository;
import soomsheo.Telo.resident.ResidentRepository;
import soomsheo.Telo.resident.ResidentService;
import soomsheo.Telo.resident.dto.ResidentRegisterDTO;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class BuildingControllerPerformanceTest {

    @Autowired private ResidentService residentService;
    @Autowired private ResidentRepository residentRepository;
    @Autowired private BuildingRepository buildingRepository;
    @Autowired private MemberRepository memberRepository;
    @Autowired private BuildingController buildingController; // BuildingController 주입

    private UUID testBuildingID; // 기존 테스트용 빌딩 ID
    private String testLandlordID; // 새로운 테스트용 지주 ID

    @BeforeEach
    void setup() throws Exception {
        // --- 기존 residentService.getAllResidents 성능 테스트를 위한 데이터 ---
        // 1. Member (landlord) 객체 생성 및 setter로 값 설정
        Member landlordForResidentsTest = new Member();
        landlordForResidentsTest.setMemberID(UUID.randomUUID().toString()); // String 타입 ID 직접 할당
        landlordForResidentsTest.setMemberRealName("테스트지주-레지던트");
        landlordForResidentsTest.setMemberType("landlord");
        Member savedLandlordForResidentsTest = memberRepository.save(landlordForResidentsTest);

        // 2. Building 객체 생성 (생성자 사용 - 모든 필드를 인자로 받는 생성자가 있다고 가정)
        Building buildingForResidentsTest = new Building(
                "테스트 빌딩-레지던트",
                "테스트 주소-레지던트",
                10000,
                0,
                Collections.emptyList(),
                savedLandlordForResidentsTest, // 영속 상태의 Member 객체 전달
                "공지사항"
        );
        buildingRepository.save(buildingForResidentsTest);
        testBuildingID = buildingForResidentsTest.getBuildingID();

        // 3. Resident (tenant)와 Resident 객체 생성 및 저장 (10,000개 데이터 생성)
        for (int i = 0; i < 10000; i++) {
            Member tenant = new Member();
            tenant.setMemberID(UUID.randomUUID().toString());
            tenant.setMemberRealName("테스트세입자" + i);
            tenant.setMemberType("tenant");
            Member savedTenant = memberRepository.save(tenant);

            Resident resident = new Resident( // Resident 생성자 사용 (모든 필드를 인자로 받는 생성자 가정)
                    savedTenant, // 영속 상태의 Member 객체 전달
                    String.valueOf(100 + i),
                    "월세", "500000", "10", "10000000", "2025-12-31",
                    buildingForResidentsTest, // 영속 상태의 Building 객체 전달
                    Collections.emptyList()
            );
            residentRepository.save(resident);
        }

        // --- 새로운 getBuildingsByLandlordID 성능 테스트를 위한 데이터 ---
        // 1. 새로운 지주 (landlord) 생성
        Member landlordForBuildingsTest = new Member();
        landlordForBuildingsTest.setMemberID(UUID.randomUUID().toString());
        landlordForBuildingsTest.setMemberRealName("새테스트지주");
        landlordForBuildingsTest.setMemberType("landlord");
        Member savedLandlordForBuildingsTest = memberRepository.save(landlordForBuildingsTest);
        testLandlordID = savedLandlordForBuildingsTest.getMemberID(); // 이 지주의 ID를 저장

        // 2. 이 지주에게 10,000개의 빌딩 할당 및 저장
        for (int i = 0; i < 10000; i++) {
            Building building = new Building( // Building 생성자 사용
                    "새 빌딩 " + i,
                    "새 주소 " + i,
                    10,
                    0,
                    Collections.emptyList(),
                    savedLandlordForBuildingsTest, // 위에서 저장한 지주 할당
                    "새 공지사항"
            );
            buildingRepository.save(building);
        }

        // 모든 변경사항을 DB에 즉시 반영
        memberRepository.flush();
        buildingRepository.flush();
        residentRepository.flush();
    }

    @Test
    void 대용량_데이터에서_주소로_건물정보_조회_성능_측정() {
        long startTime = System.currentTimeMillis();
        List<ResidentRegisterDTO> residents = residentService.getAllResidents(testBuildingID);
        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;

        System.out.println("getAllResidents took: " + duration + " ms");
        assertThat(residents).hasSize(10000);
        assertThat(duration).isLessThan(500); // 500ms 이내로 응답하는지 검증 (임의의 기준, 필요에 따라 조정)
    }

    @Test
    void 대용량_데이터에서_지주ID로_빌딩목록_조회_성능_측정() {
        long startTime = System.currentTimeMillis();

        ResponseEntity<List<BuildingResponse>> responseEntity = buildingController.getBuildingsByLandlordID(testLandlordID);

        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;

        System.out.println("getBuildingsByLandlordID took: " + duration + " ms");

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isNotNull();
        assertThat(responseEntity.getBody()).hasSize(10000);
        assertThat(duration).isLessThan(500);
    }
}