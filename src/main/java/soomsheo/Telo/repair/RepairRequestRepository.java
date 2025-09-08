package soomsheo.Telo.repair;

import org.springframework.data.jpa.repository.JpaRepository;
import soomsheo.Telo.repair.domain.RepairRequest;

import java.util.List;

public interface RepairRequestRepository extends JpaRepository<RepairRequest, String> {
    List<RepairRequest> findByLandlordIDOrTenantID(String landlordID, String tenantID);
}
