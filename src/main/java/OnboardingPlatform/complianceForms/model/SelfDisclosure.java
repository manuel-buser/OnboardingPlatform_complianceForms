package OnboardingPlatform.complianceForms.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "selfDisclosure_tbl")
public class SelfDisclosure {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int selfDisclosureId;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    // US Person Qualification fields
    private boolean usCitizen;
    private boolean bornInUSTerritories;
    private boolean hasCertificateOfLossOfNationality;
    private boolean hasGreenCard;
    private boolean hasUSImmigrationServiceCard;
    private boolean hasUSResidenceForTax;
    private boolean isUSResidentForOtherReasons;

    // Tax Residency and PEP fields

    private boolean euTaxResidency;

    private boolean ukTaxResidency;

    private boolean politicallyExposedPerson;

    private boolean relatedToPep;

    private String pepAssociatedPosition;

    private String relatedPersonDetails;

}
