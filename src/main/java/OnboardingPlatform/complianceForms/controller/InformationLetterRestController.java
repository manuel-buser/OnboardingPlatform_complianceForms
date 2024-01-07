package OnboardingPlatform.complianceForms.controller;

import OnboardingPlatform.complianceForms.model.Customer;
import OnboardingPlatform.complianceForms.service.CustomerService;
import OnboardingPlatform.complianceForms.service.InformationLetterPDFGeneratorService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;

@RestController
public class InformationLetterRestController {

    private final InformationLetterPDFGeneratorService informationLetterPDFGeneratorService;
    public InformationLetterRestController(InformationLetterPDFGeneratorService informationLetterPDFGeneratorService) {
        this.informationLetterPDFGeneratorService = informationLetterPDFGeneratorService;
    }

    @PostMapping("/pdf/generate/informationLetter")
    public void generatePDFDataProtection() throws IOException {
        String classpath = System.getProperty("user.dir");
        String filePath = classpath + "\\src\\main\\resources\\PDFs\\InformationLetter\\";

        File file = new File(filePath);
        if (!file.exists()) {
            file.getParentFile().mkdirs();
        }

        this.informationLetterPDFGeneratorService.exportToFileInformationLetter(filePath);
    }


}
