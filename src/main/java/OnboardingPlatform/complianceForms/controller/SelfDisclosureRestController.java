package OnboardingPlatform.complianceForms.controller;

import OnboardingPlatform.complianceForms.model.Customer;
import OnboardingPlatform.complianceForms.model.SelfDisclosure;
import OnboardingPlatform.complianceForms.service.SelfDisclosurePDFGeneratorService;
import OnboardingPlatform.complianceForms.service.SelfDisclosureService;
import OnboardingPlatform.complianceForms.service.CustomerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;

@RestController
@RequestMapping("/selfDisclosure")
public class SelfDisclosureRestController {

    private final SelfDisclosurePDFGeneratorService selfDisclosurePdfGeneratorService;
    private final CustomerService customerService;
    private final SelfDisclosureService selfDisclosureService;

    public SelfDisclosureRestController(SelfDisclosurePDFGeneratorService selfDisclosurePdfGeneratorService, CustomerService customerService,
                                        SelfDisclosureService selfDisclosureService) {
        this.selfDisclosurePdfGeneratorService = selfDisclosurePdfGeneratorService;
        this.customerService = customerService;
        this.selfDisclosureService = selfDisclosureService;
    }

    @PostMapping("/add")
    public ResponseEntity<Integer> addSelfDisclosure(@RequestBody SelfDisclosure selfDisclosure, @RequestParam("customerId") int customerId) {
        Customer customer = customerService.getCustomerById(customerId);
        selfDisclosure.setCustomer(customer); // Set the retrieved customer

        int selfDisclosureId = selfDisclosureService.saveSelfDisclosure(selfDisclosure);
        return ResponseEntity.ok(selfDisclosureId);
    }

    @GetMapping("/get/{id}")
    public SelfDisclosure getSelfDisclosureById(@PathVariable int id) {
        return selfDisclosureService.getSelfDisclosureById(id);
    }

    @DeleteMapping("/delete/{id}")
    public String deleteSelfDisclosure(@PathVariable int id) {
        return selfDisclosureService.deleteSelfDisclosureById(id);
    }

    @PostMapping("/pdf/generate")
    public void generatePDF(@RequestBody Map<String, Object> formData) throws IOException {

        //get the classpath and point it to the PDFs folder
        String classpath = System.getProperty("user.dir");
        String filePath = classpath + "\\src\\main\\resources\\PDFs\\SelfDisclosure\\";

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
        SelfDisclosure selfDisclosure = selfDisclosureService.getSelfDisclosureByCustomerId(customerId);

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


        // Use retrieved customer, selfDisclosure, and signature in PDF generation
        this.selfDisclosurePdfGeneratorService.exportToFileSelf(filePath, customer, selfDisclosure, signatureBytes, currentPlace);
    }
}
