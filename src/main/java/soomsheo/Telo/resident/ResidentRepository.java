package soomsheo.Telo.resident;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import soomsheo.Telo.building.domain.Building;
import soomsheo.Telo.building.domain.Resident;

import java.util.List;
import java.util.UUID;

public interface ResidentRepository extends JpaRepository<Resident, UUID> {

    // N+1 문제 해결: JOIN FETCH로 Resident 조회 시 연관된 tenant(Member)를 함께 조회
    @Query("SELECT r FROM Resident r JOIN FETCH r.tenant WHERE r.building.buildingID = :buildingID")
    List<Resident> findAllWithTenantByBuildingID(@Param("buildingID") UUID buildingID);

    // N+1 문제 해결: JOIN FETCH로 Resident 조회 시 연관된 building을 함께 조회
    @Query("SELECT r FROM Resident r JOIN FETCH r.building WHERE r.tenant.memberID = :tenantID")
    List<Resident> findAllWithBuildingByTenantMemberID(@Param("tenantID") String tenantID);





    Resident findByResidentID(UUID residentID);
    List<Resident> findByTenantMemberID(String tenantID);

    @Query("SELECT r.building FROM Resident r WHERE r.tenant.memberID = :tenantID AND r.building.landlordID = :landlordID")
    List<Building> findBuildingsByTenantIdAndLandlordId(@Param("tenantID") String tenantID, @Param("landlordID") String landlordID);

    @Query("SELECT r FROM Resident r WHERE r.tenant.memberID = :tenantID AND r.building.landlordID = :landlordID")
    List<Resident> findResidentsByTenantIdAndLandlordId(@Param("tenantID") String tenantID, @Param("landlordID") String landlordID);
}