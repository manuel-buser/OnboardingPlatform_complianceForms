package OnboardingPlatform.complianceForms.controller;

import OnboardingPlatform.complianceForms.model.Customer;
import OnboardingPlatform.complianceForms.model.EconomicBeneficiary;
import OnboardingPlatform.complianceForms.service.CustomerService;
import OnboardingPlatform.complianceForms.service.EconomicBeneficiaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class EconomicBeneficiaryRestController {
    private final EconomicBeneficiaryService beneficiaryService; // Assuming you have EconomicBeneficiaryService
    private final CustomerService customerService;

    @Autowired
    public EconomicBeneficiaryRestController(EconomicBeneficiaryService beneficiaryService, CustomerService customerService) {
        this.beneficiaryService = beneficiaryService;
        this.customerService = customerService;

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
}
