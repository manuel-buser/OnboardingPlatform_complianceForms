package OnboardingPlatform.complianceForms.controller;

import OnboardingPlatform.complianceForms.MailService.GMailer;
import OnboardingPlatform.complianceForms.model.Customer;
import OnboardingPlatform.complianceForms.model.EconomicBeneficiary;
import OnboardingPlatform.complianceForms.model.SelfDisclosure;
import OnboardingPlatform.complianceForms.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@org.springframework.stereotype.Controller
public class Controller {
    private final CustomerService customerService;
    private final EconomicBeneficiaryService economicBeneficiaryService;
    private final SelfDisclosureService selfDisclosureService;
    private final DataProtectionPDFGeneratorService dataProtectionPDFGeneratorService;

    private final GMailer gMailer;

    @Autowired
    public Controller(CustomerService customerService, EconomicBeneficiaryService economicBeneficiaryService,
                      SelfDisclosureService selfDisclosureService, DataProtectionPDFGeneratorService dataProtectionPDFGeneratorService,
                      GMailer gMailer) {
        this.customerService = customerService;
        this.economicBeneficiaryService = economicBeneficiaryService;
        this.selfDisclosureService = selfDisclosureService;
        this.dataProtectionPDFGeneratorService = dataProtectionPDFGeneratorService;
        this.gMailer = gMailer;
    }

    @GetMapping("/")
    public String getPersonalDataHTML(Model model) {
        model.addAttribute("customer", new Customer());
        return "personalData";
    }

    @GetMapping("/dataProtection")
    public String getDataProtectionHTML(Model model) {
        Customer lastCreatedCustomer = customerService.getLastCreatedCustomer();

        if (lastCreatedCustomer != null) {
            int customerId = lastCreatedCustomer.getCustomerId();
            model.addAttribute("customerId", customerId);
        }

        return "dataProtection";
    }

    @GetMapping("/informationLetter")
    public String getInformationLetterHTML() {
        return "informationLetter";
    }

    @GetMapping("/economicBeneficiary")
    public String getEconomicBeneficiaryHTML(Model model) {
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
    public String getSelfDisclosureHTML(Model model) {
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

    @GetMapping("/endingPage")
    public String getEndingPageHTML() throws Exception { //throws MessagingException {

        gMailer.sendMail("Subject", "Message");


        return "endingPage";
    }




}



