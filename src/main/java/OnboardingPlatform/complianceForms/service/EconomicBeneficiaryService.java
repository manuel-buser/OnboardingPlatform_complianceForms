package OnboardingPlatform.complianceForms.service;

import OnboardingPlatform.complianceForms.model.Customer;
import OnboardingPlatform.complianceForms.model.EconomicBeneficiary;
import OnboardingPlatform.complianceForms.repository.EconomicBeneficiaryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EconomicBeneficiaryService {

    private EconomicBeneficiary lastCreatedBeneficiary; // To store the last created beneficiary
    private final EconomicBeneficiaryRepository beneficiaryRepository;
    private final CustomerService customerService;

    // dependency injections
    @Autowired
    public EconomicBeneficiaryService(EconomicBeneficiaryRepository beneficiaryRepository, CustomerService customerService) {
        this.beneficiaryRepository = beneficiaryRepository;
        this.customerService = customerService;
    }

    public int saveEconomicBeneficiary(EconomicBeneficiary beneficiary) {
        Customer lastCreatedCustomer = customerService.getLastCreatedCustomer();

        beneficiary.setCustomer(lastCreatedCustomer); // Link the EconomicBeneficiary to the last created Customer

        lastCreatedBeneficiary = beneficiaryRepository.save(beneficiary); // Store the last created beneficiary

        return lastCreatedBeneficiary.getEconomicBeneficiaryId();
    }

    public EconomicBeneficiary getLastCreatedBeneficiary() {
        return lastCreatedBeneficiary;
    }

    public EconomicBeneficiary getBeneficiaryById(int id) {
        return beneficiaryRepository.findById(id).orElse(null); // Return object or null if ID not found
    }

    public String deleteBeneficiaryById(int id) {
        beneficiaryRepository.deleteById(id);
        return "Beneficiary deleted successfully " + id;
    }

}
