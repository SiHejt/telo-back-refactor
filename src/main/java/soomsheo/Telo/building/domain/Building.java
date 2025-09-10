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
        name = "building",
        indexes = {
                @Index(name = "idx_building_landlord_member_id", columnList = "landlord_member_id"),
        }
)
public class Building {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "BINARY(16)")
    private UUID buildingID;

    private String buildingName;
    private String buildingAddress;
    private int numberOfHouseholds;
    private int numberOfRentedHouseholds;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "landlord_member_id", nullable = false)
    private Member landlord;

    private String notice;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "building_images", joinColumns = @JoinColumn(name = "building_id"))
    @Column(name = "image_url")
    private List<String> imageURL;

    public Building(String buildingName, String buildingAddress, int numberOfHouseholds, int numberOfRentedHouseholds, List<String> imageURL, Member landlord, String notice) {
        this.buildingName = buildingName;
        this.buildingAddress = buildingAddress;
        this.numberOfHouseholds = numberOfHouseholds;
        this.numberOfRentedHouseholds = numberOfRentedHouseholds;
        this.landlord = landlord;
        this.imageURL = imageURL;
        this.notice = notice;
    }
}