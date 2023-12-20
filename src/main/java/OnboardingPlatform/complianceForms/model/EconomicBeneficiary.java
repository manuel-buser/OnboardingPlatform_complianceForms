package OnboardingPlatform.complianceForms.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data // instead of manually creating getters and setters - Lombok dependency
@AllArgsConstructor // all of them included in constructor
@NoArgsConstructor // no arguments
@Entity
@Table(name = "EconomicBeneficiary_TBL")
public class EconomicBeneficiary {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int economicBeneficiaryId;

    @OneToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    // Identified Person fields
    private boolean identicalToContractPartner;
    private boolean identicalToWealthContributor;
    private boolean memberOfLeadershipBody;
    private boolean protectorOrSimilar;
    private boolean beneficiaryOrPotential;
    private boolean holdsMoreThan25PercentShares;
    private boolean exercisesControlOverManagement;

    // US Person Qualification fields
    private boolean usCitizen;
    private boolean bornInUSTerritories;
    private boolean hasCertificateOfLossOfNationality;
    private boolean hasGreenCard;
    private boolean hasUSImmigrationServiceCard;
    private boolean hasUSResidenceForTax;
    private boolean isUSResidentForOtherReasons;
}

