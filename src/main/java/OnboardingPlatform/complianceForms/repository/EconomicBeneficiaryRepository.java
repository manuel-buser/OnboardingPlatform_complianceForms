package OnboardingPlatform.complianceForms.repository;

import OnboardingPlatform.complianceForms.model.EconomicBeneficiary;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EconomicBeneficiaryRepository extends JpaRepository<EconomicBeneficiary, Integer> {
    // Custom methods specific to EconomicBeneficiaryEntity...
}
