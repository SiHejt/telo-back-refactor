package soomsheo.Telo.building.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import soomsheo.Telo.member.domain.Member;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(
        name = "resident",
        indexes = {
                @Index(name = "idx_resident_tenant_id", columnList = "tenant_id"),
                @Index(name = "idx_resident_building_id", columnList = "building_id"),}
)
public class Resident {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "BINARY(16)")
    private UUID residentID;

    private String apartmentNumber;
    private String rentType;
    private String monthlyRentAmount;
    private String monthlyRentPaymentDate;
    private String deposit;
    private String contractExpirationDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tenant_id", nullable = false)
    private Member tenant;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "building_id", nullable = false)
    private Building building;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "resident_contract_images", joinColumns = @JoinColumn(name = "resident_id"))
    @Column(name = "contract_image_url")
    private List<String> contractImageURL;

    public Resident(Member tenant, String apartmentNumber, String rentType, String monthlyRentAmount,
                    String monthlyRentPaymentDate, String deposit, String contractExpirationDate,Building building, List<String> contractImageURL) throws Exception {
        this.apartmentNumber = apartmentNumber;
        this.rentType = rentType;
        this.monthlyRentAmount = monthlyRentAmount;
        this.monthlyRentPaymentDate = monthlyRentPaymentDate;
        this.deposit = deposit;
        this.contractExpirationDate = contractExpirationDate;
        this.building = building;
        this.tenant = tenant;
        this.contractImageURL = contractImageURL;
    }

}
