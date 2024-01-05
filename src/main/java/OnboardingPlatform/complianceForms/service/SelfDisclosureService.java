package OnboardingPlatform.complianceForms.service;

import OnboardingPlatform.complianceForms.model.Customer;
import OnboardingPlatform.complianceForms.model.SelfDisclosure;
import OnboardingPlatform.complianceForms.repository.SelfDisclosureRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SelfDisclosureService {

    private SelfDisclosure lastCreatedSelfDisclosure; // To store the last created selfDisclosure
    private final SelfDisclosureRepository selfDisclosureRepository;
    private final CustomerService customerService;

    @Autowired
    public SelfDisclosureService(SelfDisclosureRepository selfDisclosureRepository, CustomerService customerService) {
        this.selfDisclosureRepository = selfDisclosureRepository;
        this.customerService = customerService;
    }

    public int saveSelfDisclosure(SelfDisclosure selfDisclosure) {
        Customer lastCreatedCustomer = customerService.getLastCreatedCustomer();

        selfDisclosure.setCustomer(lastCreatedCustomer); // Link the selfDisclosure to the last created Customer

        lastCreatedSelfDisclosure = selfDisclosureRepository.save(selfDisclosure); // Store the last created selfDisclosure

        return lastCreatedSelfDisclosure.getSelfDisclosureId();
    }

    public SelfDisclosure getLastCreatedSelfDisclosure() {
        return lastCreatedSelfDisclosure;
    }

    public SelfDisclosure getSelfDisclosureById(int id) {
        return selfDisclosureRepository.findById(id).orElse(null); // Return object or null if ID not found
    }

    public String deleteSelfDisclosureById(int id) {
        selfDisclosureRepository.deleteById(id);
        return "selfDisclosure deleted successfully " + id;
    }

    public SelfDisclosure getSelfDisclosureByCustomerId(int customerId) {
        return selfDisclosureRepository.findByCustomerCustomerId(customerId);
    }
}
