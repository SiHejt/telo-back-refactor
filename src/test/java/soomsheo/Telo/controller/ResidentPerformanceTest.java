package soomsheo.Telo.controller; // 자신의 패키지 경로에 맞게 확인하세요

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import soomsheo.Telo.member.domain.Member;
import soomsheo.Telo.building.domain.Building;
import soomsheo.Telo.building.domain.Resident;
import soomsheo.Telo.building.BuildingRepository;
import soomsheo.Telo.member.MemberRepository;
import soomsheo.Telo.resident.ResidentRepository;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ResidentPerformanceTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BuildingRepository buildingRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ResidentRepository residentRepository;

//    @Test
//    @Transactional
//    void 건물별_세입자_목록_조회시_N플러스1_문제_측정() throws Exception {
//        // --- 1. 데이터 준비 ---
//        Building testBuilding = new Building("테스트 빌딩", "암호화된 주소", 50, 0, List.of("img.jpg"), "landlord123", "");
//        // Use the returned instance from the save method
//        testBuilding = buildingRepository.save(testBuilding);
//
//        int residentCount = 10000;
//        List<Member> tenants = new ArrayList<>();
//        for (int i = 0; i < residentCount; i++) {
//            Member tenant = new Member();
//            tenant.setMemberID("tenant" + i);
//            tenant.setMemberRealName("세입자" + i);
//            tenant.setMemberType("TENANT");
//            tenant.setPhoneNumber("010-1234-" + String.format("%04d", i));
//            tenants.add(tenant);
//        }
//        memberRepository.saveAll(tenants);
//
//        List<Resident> residents = new ArrayList<>();
//        for (Member tenant : tenants) {
//            // Now using the JPA-managed testBuilding instance
//            Resident resident = new Resident(tenant, (101 + residents.size()) + "호", "월세", "0", "0", "0", "2026-09-06", testBuilding, List.of("contract.jpg"));
//            residents.add(resident);
//        }
//        // This line (64) should now work without errors
//        residentRepository.saveAll(residents);
//        System.out.println("데이터 준비 완료: 건물 1개, 세입자 " + residentCount + "명");
//
//
//        // --- 2. 성능 측정 ---
//        System.out.println("성능 측정을 시작합니다...");
//        long startTime = System.currentTimeMillis();
//
//        mockMvc.perform(get("/api/residents/landlord/resident-list/" + testBuilding.getBuildingID()))
//                .andExpect(status().isOk())
//                .andDo(print());
//
//        long endTime = System.currentTimeMillis();
//        long duration = endTime - startTime;
//        System.out.println("N+1 문제 발생 시 조회 소요 시간: " + duration + "ms");
//    }
}