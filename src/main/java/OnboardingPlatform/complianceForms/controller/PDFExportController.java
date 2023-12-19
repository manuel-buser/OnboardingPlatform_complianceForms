package OnboardingPlatform.complianceForms.controller;

import OnboardingPlatform.complianceForms.model.Customer;
import OnboardingPlatform.complianceForms.model.IdentifiedPersonModel;
import OnboardingPlatform.complianceForms.model.USPersonQualificationModel;
import OnboardingPlatform.complianceForms.service.CustomerService;
import OnboardingPlatform.complianceForms.service.PDFGeneratorService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

@Controller
public class PDFExportController {

    private final PDFGeneratorService pdfGeneratorService;
    private final CustomerService customerService;

    public PDFExportController(PDFGeneratorService pdfGeneratorService, CustomerService customerService) {
        this.pdfGeneratorService = pdfGeneratorService;
        this.customerService = customerService;
    }

    @GetMapping("/pdf/generate")
    public void generatePDF(HttpServletResponse response,
                            @RequestParam Map<String, String> params1, Map<String, String> params2, int customerId) throws IOException {
        response.setContentType("application/pdf");
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-dd-MM:hh:mm:ss");
        String currentDateTime = dateFormatter.format(new Date());

        String headerKey = "Content-Disposition";
        String headerValue = "inline; filename=pdf_" + currentDateTime + ".pdf";
        response.setHeader(headerKey, headerValue);

        // Retrieve customer details from the database using the customerId
        Customer customer = customerService.getCustomerById(customerId);

        // Assuming you have default values for IdentifiedPersonModel and USPersonQualificationModel
        IdentifiedPersonModel identifiedPersonModel = mapParamsToIdentifiedPersonModel(params1);
        USPersonQualificationModel usPersonQualificationModel = mapParamsToUSPersonQualificationModel(params2);


        // Use retrieved customer, identifiedPersonModel, and usPersonQualificationModel in PDF generation
        this.pdfGeneratorService.export(response, identifiedPersonModel, customer, usPersonQualificationModel);
    }


    private IdentifiedPersonModel mapParamsToIdentifiedPersonModel(Map<String, String> params1) {
        IdentifiedPersonModel identifiedPersonModel = new IdentifiedPersonModel();

        // Map checkbox values to IdentifiedPersonModel fields
        identifiedPersonModel.setIdenticalToContractPartner(params1.containsKey("identicalToContractPartner"));
        identifiedPersonModel.setIdenticalToWealthContributor(params1.containsKey("identicalToWealthContributor"));
        identifiedPersonModel.setMemberOfLeadershipBody(params1.containsKey("memberOfLeadershipBody"));
        identifiedPersonModel.setProtectorOrSimilar(params1.containsKey("protectorOrSimilar"));
        identifiedPersonModel.setBeneficiaryOrPotential(params1.containsKey("beneficiaryOrPotential"));
        identifiedPersonModel.setHoldsMoreThan25PercentShares(params1.containsKey("holdsMoreThan25PercentShares"));
        identifiedPersonModel.setExercisesControlOverManagement(params1.containsKey("exercisesControlOverManagement"));

        return identifiedPersonModel;
    }

    private USPersonQualificationModel mapParamsToUSPersonQualificationModel(Map<String, String> params2) {
        USPersonQualificationModel usPersonQualificationModel = new USPersonQualificationModel();

        // Map checkbox values to USPersonQualificationModel fields
        usPersonQualificationModel.setUSCitizen(params2.containsKey("usCitizen"));
        usPersonQualificationModel.setBornInUSTerritories(params2.containsKey("bornInUSTerritories"));
        usPersonQualificationModel.setHasCertificateOfLossOfNationality(params2.containsKey("hasCertificateOfLossOfNationality"));
        usPersonQualificationModel.setHasGreenCard(params2.containsKey("hasGreenCard"));
        usPersonQualificationModel.setHasUSImmigrationServiceCard(params2.containsKey("hasUSImmigrationServiceCard"));
        usPersonQualificationModel.setHasUSResidenceForTax(params2.containsKey("hasUSResidenceForTax"));
        usPersonQualificationModel.setUSResidentForOtherReasons(params2.containsKey("isUSResidentForOtherReasons"));

        return usPersonQualificationModel;
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

    @GetMapping("/complianceForm1")
    public String renderSecondPage(Model model) {
        Customer lastCreatedCustomer = customerService.getLastCreatedCustomer();

        if (lastCreatedCustomer != null) {
            int customerId = lastCreatedCustomer.getCustomerId();
            model.addAttribute("customerId", customerId);
        }

        return "complianceForm1";
    }

}
