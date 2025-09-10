package soomsheo.Telo.building;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import soomsheo.Telo.building.domain.Building;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BuildingRepository extends JpaRepository<Building, UUID> {
    @EntityGraph(attributePaths = {"landlord", "imageURL"})
    Optional<Building> findByBuildingID(UUID buildingID);

    @EntityGraph(attributePaths = {"landlord", "imageURL"})
    List<Building> findByLandlord_MemberID(String memberID);

    @Query("SELECT b.buildingAddress FROM Building b")
    List<String> findAllBuildingAddresses();

    @Query("SELECT b FROM Building b WHERE b.buildingAddress LIKE %:partialAddress%")
    List<Building> findByBuildingAddressContaining(@Param("partialAddress") String partialAddress);

    Optional<Building> findByBuildingAddress(String buildingAddress);
}