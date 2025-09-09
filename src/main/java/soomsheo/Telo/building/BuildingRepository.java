package soomsheo.Telo.building;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import soomsheo.Telo.building.domain.Building;

import java.util.List;
import java.util.UUID;

public interface BuildingRepository extends JpaRepository<Building, UUID> {
    Building findByBuildingID(UUID buildingID);

    List<Building> findByLandlordID(String landlordID);

//    @Query("SELECT b.encryptedBuildingAddress FROM Building b")
//    List<String> findAllEncryptedAddresses();
    @Query("SELECT b.buildingAddress FROM Building b")
    List<String> findAllBuildingAddresses();
}