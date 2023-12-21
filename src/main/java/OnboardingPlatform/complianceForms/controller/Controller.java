package OnboardingPlatform.complianceForms.controller;

import OnboardingPlatform.complianceForms.model.Customer;
import OnboardingPlatform.complianceForms.model.EconomicBeneficiary;
import OnboardingPlatform.complianceForms.service.CustomerService;
import OnboardingPlatform.complianceForms.service.EconomicBeneficiaryService;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@org.springframework.stereotype.Controller
public class Controller {
    private final CustomerService customerService;
    private final EconomicBeneficiaryService economicBeneficiaryService;

    public Controller(CustomerService customerService, EconomicBeneficiaryService economicBeneficiaryService) {
        this.customerService = customerService;
        this.economicBeneficiaryService = economicBeneficiaryService;
    }

    @GetMapping("/")
    public String getAddCustomerPage(Model model) {
        model.addAttribute("customer", new Customer());
        return "personalData";
    }

    @GetMapping("/economicBeneficiary")
    public String renderEconomicBeneficiaryPage(Model model) {
        Customer lastCreatedCustomer = customerService.getLastCreatedCustomer();
        EconomicBeneficiary lastCreatedBeneficiary = economicBeneficiaryService.getLastCreatedBeneficiary();

        if (lastCreatedCustomer != null) {
            int customerId = lastCreatedCustomer.getCustomerId();
            model.addAttribute("customerId", customerId);
        }

        if (lastCreatedBeneficiary != null) {
            int beneficiaryId = lastCreatedBeneficiary.getEconomicBeneficiaryId();
            model.addAttribute("economicBeneficiaryId", beneficiaryId);
        }

        return "economicBeneficiary";
    }

    @GetMapping("/selfDisclosure")
    public String getSelfDisclosureHtml() {
        return "selfDisclosure";
    }




}
