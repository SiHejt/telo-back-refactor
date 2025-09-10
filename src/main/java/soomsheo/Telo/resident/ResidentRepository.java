package soomsheo.Telo.resident;

import org.springframework.data.jpa.repository.EntityGraph; // EntityGraph 임포트 추가 (N+1 문제 방지용)
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import soomsheo.Telo.building.domain.Building;
import soomsheo.Telo.building.domain.Resident;

import java.util.List;
import java.util.Optional; // findByResidentID를 Optional로 변경하기 위해 임포트
import java.util.UUID;

public interface ResidentRepository extends JpaRepository<Resident, UUID> {

    @EntityGraph(attributePaths = {"tenant"})
    List<Resident> findByBuilding_BuildingID(UUID buildingID); // <- 이 부분이 변경되었습니다.

    @EntityGraph(attributePaths = {"building"})
    List<Resident> findAllWithBuildingByTenantMemberID(@Param("tenantID") String tenantID);

    @EntityGraph(attributePaths = {"tenant", "building"})
    Optional<Resident> findByResidentID(UUID residentID);

    @EntityGraph(attributePaths = {"tenant", "building"})
    List<Resident> findByTenant_MemberID(String tenantID);

    @Query("SELECT r.building FROM Resident r WHERE r.tenant.memberID = :tenantMemberID AND r.building.landlord.memberID = :landlordMemberID")
    List<Building> findBuildingsByTenantMemberIDAndLandlordMemberID(
            @Param("tenantMemberID") String tenantMemberID,
            @Param("landlordMemberID") String landlordMemberID
    );

    @Query("SELECT r FROM Resident r JOIN FETCH r.tenant JOIN FETCH r.building b JOIN FETCH b.landlord WHERE r.tenant.memberID = :tenantMemberID AND r.building.landlord.memberID = :landlordMemberID")
    List<Resident> findResidentsByTenantMemberIDAndLandlordMemberID(
            @Param("tenantMemberID") String tenantMemberID,
            @Param("landlordMemberID") String landlordMemberID
    );
}