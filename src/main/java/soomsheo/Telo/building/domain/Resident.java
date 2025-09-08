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
public class Resident {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID residentID;

    private String apartmentNumber;
    private String rentType;
    private String monthlyRentAmount;
    private String monthlyRentPaymentDate;
    private String deposit;
    private String contractExpirationDate;

    @ManyToOne
    @JoinColumn(name = "tenant_id", nullable = false)
    private Member tenant;

    @ManyToOne
    @JoinColumn(name = "building_id", nullable = false)
    private Building building;

    @ElementCollection
    @CollectionTable(name = "resident_contract_images", joinColumns = @JoinColumn(name = "resident_id"))
    @Column(name = "contract_image_url")
    private List<String> contractImageURL;

    public Resident(Member tenant, String apartmentNumber, String rentType, String monthlyRentAmount,
                    String monthlyRentPaymentDate, String deposit, String contractExpirationDate,Building building, List<String> contractImageURL) throws Exception {
        this.residentID = UUID.randomUUID();
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
