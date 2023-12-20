package OnboardingPlatform.complianceForms.repository;

import OnboardingPlatform.complianceForms.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;


public interface CustomerRepository extends JpaRepository<Customer, Integer>  {

    // all logic for database connection is handled by dependencies from spring boot
    // logic can be found in application.properties file

    

}
