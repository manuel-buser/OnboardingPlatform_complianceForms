package OnboardingPlatform.complianceForms.repository;

import OnboardingPlatform.complianceForms.model.SelfDisclosure;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SelfDisclosureRepository extends JpaRepository<SelfDisclosure, Integer> {
    SelfDisclosure findByCustomerCustomerId(int customerId);
}