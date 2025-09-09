package soomsheo.Telo.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import soomsheo.Telo.building.domain.Building;
import soomsheo.Telo.building.BuildingRepository;
import soomsheo.Telo.member.domain.Member;
import soomsheo.Telo.member.MemberRepository;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional // ✅ 1. 테스트용 '샌드박스'를 만들고, 끝나면 모든 변경사항을 자동으로 롤백합니다.
public class BuildingControllerPerformanceTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BuildingRepository buildingRepository;

    @Autowired
    private MemberRepository memberRepository; // Member 저장을 위해 주입

    @Test
    void 주소로_건물정보_조회_성능_측정() throws Exception {
        // --- 1. 데이터 준비 (Arrange) ---
        // ✅ 2. 테스트에 필요한 모든 데이터를 이 안에서 직접 생성합니다.

        // 상수 정의
        final String LANDLORD_ID = "landlord123";
        final int DATA_SIZE = 10000;
        final String TARGET_ADDRESS = "테스트 주소 " + (DATA_SIZE - 1);

        // 테스트용 Member(임대인) 생성 및 저장
        System.out.println("테스트용 임대인 데이터를 생성합니다...");
        Member testMember = new Member();
        testMember.setMemberID(LANDLORD_ID);
        testMember.setMemberRealName("테스트임대인");
        testMember.setMemberNickName("임대인닉네임");
        testMember.setMemberType("landlord");
        testMember.setProvider("google");
        testMember.setProfile("default_profile.jpg");
        testMember.setToken("test-device-token");
        // setPhoneNumber는 Exception을 던지므로 try-catch로 감싸줍니다.
        try {
            testMember.setPhoneNumber("010-1234-5678");
        } catch (Exception e) {
            // 테스트 실패 처리
            throw new RuntimeException("테스트 멤버 전화번호 설정 실패", e);
        }
        memberRepository.save(testMember);
        System.out.println("임대인 데이터 준비 완료.");

        // 테스트용 Building(건물) 10,000개 생성 및 저장
        System.out.println("테스트용 건물 데이터를 생성합니다...");
        List<Building> buildings = new ArrayList<>();
        for (int i = 0; i < DATA_SIZE; i++) {
            String address = "테스트 주소 " + i;
            Building building = new Building(
                    "테스트 빌딩 " + i,
                    address,
                    20,
                    15,
                    List.of("default_image.jpg"),
                    LANDLORD_ID, // 위에서 만든 임대인 ID와 연결
                    ""
            );
            buildings.add(building);
        }
        buildingRepository.saveAll(buildings);
        System.out.println(DATA_SIZE + "개의 건물 데이터 준비 완료.");


        // --- 2. 실행 (Act) & 검증 (Assert) ---
        System.out.println("성능 측정을 시작합니다. 찾을 주소: " + TARGET_ADDRESS);
        long startTime = System.currentTimeMillis();

        mockMvc.perform(get("/api/buildings/address-compare")
                        .param("buildingAddress", TARGET_ADDRESS)
                        .characterEncoding("UTF-8")) // ✅ 3. 한글 파라미터 인코딩 문제 방지
                .andExpect(status().isOk()) // 이제 정상적으로 200 OK 응답을 기대할 수 있습니다.
                .andDo(print());

        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        System.out.println("조회 소요 시간: " + duration + "ms");
    }
}