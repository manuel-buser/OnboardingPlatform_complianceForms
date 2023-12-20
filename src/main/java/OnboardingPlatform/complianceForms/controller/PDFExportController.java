package OnboardingPlatform.complianceForms.controller;

import OnboardingPlatform.complianceForms.model.Customer;
import OnboardingPlatform.complianceForms.model.EconomicBeneficiary;
import OnboardingPlatform.complianceForms.service.CustomerService;
import OnboardingPlatform.complianceForms.service.EconomicBeneficiaryService;
import OnboardingPlatform.complianceForms.service.PDFGeneratorService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;


@Controller
public class PDFExportController {
        private final PDFGeneratorService pdfGeneratorService;
        private final CustomerService customerService;
        private final EconomicBeneficiaryService economicBeneficiaryService;

        public PDFExportController(PDFGeneratorService pdfGeneratorService, CustomerService customerService, EconomicBeneficiaryService economicBeneficiaryService) {
            this.pdfGeneratorService = pdfGeneratorService;
            this.customerService = customerService;
            this.economicBeneficiaryService = economicBeneficiaryService;
        }


    @GetMapping("/pdf/generate/economicBeneficiary")
    public void generatePDF(@RequestParam int customerId, @RequestParam int economicBeneficiaryId) throws IOException {
        String filePath = "/C:/Intelij_PracticalProject/PracticalProject_BackupDirectory/complianceForms/src/main/resources/PDFs";

        // Retrieve customer details from the database using the IDs
        Customer customer = customerService.getCustomerById(customerId);
        EconomicBeneficiary economicBeneficiary = economicBeneficiaryService.getBeneficiaryById(economicBeneficiaryId);

        // Use retrieved customer and economicBeneficiary in PDF generation
        this.pdfGeneratorService.exportToFile(filePath, customer, economicBeneficiary);
    }


    @GetMapping("/getCustomerById")
    @ResponseBody
    public ResponseEntity<Customer> getCustomerById(@RequestParam int customerId) {
        Customer customer = customerService.getCustomerById(customerId);
        if (customer != null) {
            return ResponseEntity.ok(customer);
        } else {
            return ResponseEntity.notFound().build();
        }
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

        return "economicBeneficiary"; // Return the name of your Thymeleaf template file
    }

}
