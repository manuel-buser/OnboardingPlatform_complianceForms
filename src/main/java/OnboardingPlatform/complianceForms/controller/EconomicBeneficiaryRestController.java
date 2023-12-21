package OnboardingPlatform.complianceForms.controller;

import OnboardingPlatform.complianceForms.model.Customer;
import OnboardingPlatform.complianceForms.model.EconomicBeneficiary;
import OnboardingPlatform.complianceForms.service.CustomerService;
import OnboardingPlatform.complianceForms.service.EconomicBeneficiaryService;
import OnboardingPlatform.complianceForms.service.EconomicBeneficiaryPDFGeneratorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
public class EconomicBeneficiaryRestController {
    private final EconomicBeneficiaryPDFGeneratorService economicBeneficiaryPdfGeneratorService;
    private final CustomerService customerService;
    private final EconomicBeneficiaryService economicBeneficiaryService;

    private final EconomicBeneficiaryService beneficiaryService;

    public EconomicBeneficiaryRestController(EconomicBeneficiaryPDFGeneratorService economicBeneficiaryPdfGeneratorService, CustomerService customerService,
                                             EconomicBeneficiaryService economicBeneficiaryService, EconomicBeneficiaryService beneficiaryService) {
        this.economicBeneficiaryPdfGeneratorService = economicBeneficiaryPdfGeneratorService;
        this.customerService = customerService;
        this.economicBeneficiaryService = economicBeneficiaryService;
        this.beneficiaryService = beneficiaryService;
    }

    @PostMapping("/addBeneficiary")
    public ResponseEntity<Integer> addEconomicBeneficiary(@RequestBody EconomicBeneficiary economicBeneficiary, @RequestParam("customerId") int customerId) {
        Customer customer = customerService.getCustomerById(customerId);
        economicBeneficiary.setCustomer(customer); // Set the retrieved customer

        int beneficiaryId = beneficiaryService.saveEconomicBeneficiary(economicBeneficiary);
        return ResponseEntity.ok(beneficiaryId);
    }

    @GetMapping("/getBeneficiary/{id}")
    public EconomicBeneficiary getBeneficiaryById(@PathVariable int id) {
        return beneficiaryService.getBeneficiaryById(id);
    }

    @DeleteMapping("/deleteBeneficiary/{id}")
    public String deleteBeneficiary(@PathVariable int id) {
        return beneficiaryService.deleteBeneficiaryById(id);
    }

    @GetMapping("/pdf/generate/economicBeneficiary")
    public void generatePDF(@RequestParam int customerId, @RequestParam int economicBeneficiaryId) throws IOException {
        String filePath = "/C:/Intelij_PracticalProject/PracticalProject_BackupDirectory/complianceForms/src/main/resources/PDFs";

        // Retrieve customer details from the database using the IDs
        Customer customer = customerService.getCustomerById(customerId);
        EconomicBeneficiary economicBeneficiary = economicBeneficiaryService.getBeneficiaryById(economicBeneficiaryId);

        // Use retrieved customer and economicBeneficiary in PDF generation
        this.economicBeneficiaryPdfGeneratorService.exportToFile(filePath, customer, economicBeneficiary);
    }
}
