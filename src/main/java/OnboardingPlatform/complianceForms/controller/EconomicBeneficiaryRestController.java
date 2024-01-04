package OnboardingPlatform.complianceForms.controller;

import OnboardingPlatform.complianceForms.model.Customer;
import OnboardingPlatform.complianceForms.model.EconomicBeneficiary;
import OnboardingPlatform.complianceForms.service.CustomerService;
import OnboardingPlatform.complianceForms.service.EconomicBeneficiaryService;
import OnboardingPlatform.complianceForms.service.EconomicBeneficiaryPDFGeneratorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;

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

    @PostMapping("/pdf/generate/economicBeneficiary")
    public void generatePDF(@RequestBody Map<String, Object> formData) throws IOException {

        //get the classpath and point it to the PDFs folder
        String classpath = System.getProperty("user.dir");
        String filePath = classpath + "\\src\\main\\resources\\PDFs\\";

        //if the path doesn't exist, create it
        File file = new File(filePath);
        if (!file.exists()) {
            file.getParentFile().mkdirs();
        }

        // Retrieve data from the request body
        int customerId = (int) formData.get("customerId");
        String encodedSignature = (String) formData.get("signatureImage");
        String currentPlace = (String) formData.get("currentPlace");

        // Retrieve the customer and beneficiary by using the customer ID
        Customer customer = customerService.getCustomerById(customerId);
        EconomicBeneficiary economicBeneficiary = economicBeneficiaryService.getBeneficiaryByCustomerId(customerId);

        // URL decode the encoded signature
        String decodedSignature = java.net.URLDecoder.decode(encodedSignature, StandardCharsets.UTF_8);

        // here the decoded signature is stored like this:
        // data:image/png;base64,iVBORw0KGgoAAAANSUhE....
        // and we have to take away everything in front of the first , (comma)
        // otherwise we get an illegal base64 char error

        int indexOfComma = decodedSignature.indexOf(",");

        String cleanedSignature;
        if(indexOfComma != -1) {
            cleanedSignature = decodedSignature.substring(indexOfComma + 1);
        } else {
            cleanedSignature = decodedSignature;
        }

        // Decode Base64 data
        byte[] signatureBytes = Base64.getDecoder().decode(cleanedSignature);


        // Use retrieved customer, economicBeneficiary, and signature in PDF generation
        this.economicBeneficiaryPdfGeneratorService.exportToFile(filePath, customer, economicBeneficiary, signatureBytes, currentPlace);
    }
}
