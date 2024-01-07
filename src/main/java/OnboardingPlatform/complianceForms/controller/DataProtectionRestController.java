package OnboardingPlatform.complianceForms.controller;

import OnboardingPlatform.complianceForms.model.Customer;
import OnboardingPlatform.complianceForms.model.SelfDisclosure;
import OnboardingPlatform.complianceForms.service.CustomerService;
import OnboardingPlatform.complianceForms.service.DataProtectionPDFGeneratorService;
import OnboardingPlatform.complianceForms.service.SelfDisclosurePDFGeneratorService;
import OnboardingPlatform.complianceForms.service.SelfDisclosureService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;

@RestController
public class DataProtectionRestController {

    private final DataProtectionPDFGeneratorService dataProtectionPDFGeneratorService;
    private final CustomerService customerService;

    public DataProtectionRestController(DataProtectionPDFGeneratorService dataProtectionPDFGeneratorService, CustomerService customerService,
                                        SelfDisclosureService selfDisclosureService) {
        this.dataProtectionPDFGeneratorService = dataProtectionPDFGeneratorService;
        this.customerService = customerService;
    }

    @PostMapping("/pdf/generate/dataProtection")
    public void generatePDFDataProtection(@RequestBody Map<String, Object> formData) throws IOException {

        //get the classpath and point it to the PDFs folder
        String classpath = System.getProperty("user.dir");
        String filePath = classpath + "\\src\\main\\resources\\PDFs\\DataProtection\\";

        //if the path doesn't exist, create it
        File file = new File(filePath);
        if (!file.exists()) {
            file.getParentFile().mkdirs();
        }

        // Retrieve data from the request body for selfDisclosure
        int customerId = (int) formData.get("customerId");
        String encodedSignature = (String) formData.get("signatureImage");
        String currentPlace = (String) formData.get("currentPlace");

        // Retrieve the customer and selfDisclosure by using the customer ID
        Customer customer = customerService.getCustomerById(customerId);

        // URL decode the encoded signature
        String decodedSignature = java.net.URLDecoder.decode(encodedSignature, StandardCharsets.UTF_8);

        int indexOfComma = decodedSignature.indexOf(",");

        String cleanedSignature;
        if(indexOfComma != -1) {
            cleanedSignature = decodedSignature.substring(indexOfComma + 1);
        } else {
            cleanedSignature = decodedSignature;
        }

        // Decode Base64 data
        byte[] signatureBytes = Base64.getDecoder().decode(cleanedSignature);


        // Use retrieved customer, selfDisclosure, and signature in PDF generation
        this.dataProtectionPDFGeneratorService.exportToFileDataProtection(filePath, customer, signatureBytes, currentPlace);
    }


}
