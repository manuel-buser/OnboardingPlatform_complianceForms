package OnboardingPlatform.complianceForms.repository;

import OnboardingPlatform.complianceForms.model.EconomicBeneficiary;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EconomicBeneficiaryRepository extends JpaRepository<EconomicBeneficiary, Integer> {
    EconomicBeneficiary findByCustomerCustomerId(int customerId);
}
