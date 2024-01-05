package OnboardingPlatform.complianceForms.controller;

import OnboardingPlatform.complianceForms.model.Customer;
import OnboardingPlatform.complianceForms.model.EconomicBeneficiary;
import OnboardingPlatform.complianceForms.model.SelfDisclosure;
import OnboardingPlatform.complianceForms.service.CustomerService;
import OnboardingPlatform.complianceForms.service.EconomicBeneficiaryService;
import OnboardingPlatform.complianceForms.service.SelfDisclosureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@org.springframework.stereotype.Controller
public class Controller {
    private final CustomerService customerService;
    private final EconomicBeneficiaryService economicBeneficiaryService;
    private final SelfDisclosureService selfDisclosureService;

    @Autowired
    public Controller(CustomerService customerService, EconomicBeneficiaryService economicBeneficiaryService, SelfDisclosureService selfDisclosureService) {
        this.customerService = customerService;
        this.economicBeneficiaryService = economicBeneficiaryService;
        this.selfDisclosureService = selfDisclosureService;
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
    public String getSelfDisclosureHtml(Model model) {
        Customer lastCreatedCustomer = customerService.getLastCreatedCustomer();
        SelfDisclosure lastCreatedSelfDisclosure = selfDisclosureService.getLastCreatedSelfDisclosure();

        if (lastCreatedCustomer != null) {
            int customerId = lastCreatedCustomer.getCustomerId();
            model.addAttribute("customerId", customerId);
        }
        if (lastCreatedSelfDisclosure != null) {
            int beneficiaryId = lastCreatedSelfDisclosure.getSelfDisclosureId();
            model.addAttribute("economicBeneficiaryId", beneficiaryId);
        }

        return "selfDisclosure";
    }




}
