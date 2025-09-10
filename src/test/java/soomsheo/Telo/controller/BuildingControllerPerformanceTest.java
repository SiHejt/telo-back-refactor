package soomsheo.Telo.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.support.TransactionTemplate; // TransactionTemplate import
import soomsheo.Telo.building.BuildingRepository;
import soomsheo.Telo.building.domain.Building;
import soomsheo.Telo.member.MemberRepository;
import soomsheo.Telo.member.domain.Member;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
// 클래스 레벨의 @Transactional 제거!
public class BuildingControllerPerformanceTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BuildingRepository buildingRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private TransactionTemplate transactionTemplate; // 트랜잭션을 수동으로 제어하기 위해 주입

    @Test
    @DisplayName("대용량 데이터에서 주소로 건물 정보 조회 성능 측정")
    void measureBuildingSearchByAddress_Performance() throws Exception {
        // =================================================================================
        // GIVEN: 별도의 트랜잭션에서 테스트 데이터를 준비하고 완전히 커밋합니다.
        // =================================================================================
        final String LANDLORD_MEMBER_ID = "perf-test-landlord-123";
        final int DATA_SIZE = 10000;
        final String TARGET_ADDRESS = "테스트 주소 " + (DATA_SIZE - 1);

        transactionTemplate.execute(status -> {
            System.out.println("데이터 준비 트랜잭션을 시작합니다...");
            Member landlord = new Member();
            landlord.setMemberID(LANDLORD_MEMBER_ID);
            landlord.setMemberRealName("성능테스트임대인");
            landlord.setMemberNickName("임대인대표닉네임");
            landlord.setMemberType("landlord");
            landlord.setProvider("google");
            landlord.setProfile("https://example.com/profiles/default.jpg");
            landlord.setToken("firebasetoken_for_perf_test_12345");
            landlord.setEncryptedPhoneNumber("AES_ENCRYPTED_PHONE_NUMBER_HERE");

            List<Building> buildings = new ArrayList<>();
            for (int i = 0; i < DATA_SIZE; i++) {
                String address = "테스트 주소 " + i;
                Building building = new Building(
                        "테스트 빌딩 " + i, address, 20, 15,
                        List.of("https://example.com/images/building_default.jpg"),
                        landlord, "공지사항 " + i + "입니다."
                );
                buildings.add(building);
            }
            buildingRepository.saveAll(buildings);
            System.out.println(DATA_SIZE + "개의 건물 데이터 준비 및 커밋 완료.");
            return null;
        });

        // =================================================================================
        // WHEN & THEN: 데이터가 완전히 준비된 상태에서 API 성능을 측정합니다.
        // =================================================================================
        System.out.println("성능 측정을 시작합니다. 찾을 주소: " + TARGET_ADDRESS);
        long startTime = System.currentTimeMillis();

        mockMvc.perform(get("/api/buildings/address-compare")
                        .param("buildingAddress", TARGET_ADDRESS)
                        .characterEncoding("UTF-8"))
                .andExpect(status().isOk())
                .andDo(print());

        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        System.out.println("조회 소요 시간: " + duration + "ms");
    }
}