package OnboardingPlatform.complianceForms.service;

import OnboardingPlatform.complianceForms.model.EconomicBeneficiary;
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import OnboardingPlatform.complianceForms.model.Customer;
import org.springframework.stereotype.Service;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import com.lowagie.text.Image;

@Service
public class EconomicBeneficiaryPDFGeneratorService {

    public void exportToFile(String filePath, Customer customer, EconomicBeneficiary economicBeneficiary, byte[] signatureBytes) throws IOException {

        DateFormat dateFormatter = new SimpleDateFormat("yyyy-dd-MM_hh-mm-ss");
        String currentDateTime = dateFormatter.format(new Date());

        String fileName = "EconomicBeneficiary" + currentDateTime + ".pdf";
        String completeFilePath = filePath + "/" + fileName;

        Document document = new Document(PageSize.A4);
        Font fontContent = FontFactory.getFont(FontFactory.HELVETICA);
        fontContent.setSize(12);

        try {
            PdfWriter.getInstance(document, new FileOutputStream(completeFilePath));
            document.open();

            Font fontTitle = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
            fontTitle.setSize(18);

            Font fontSubTitle = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
            fontSubTitle.setSize(14);

            // Main Title
            Paragraph mainTitle = new Paragraph("Erklärung des Vertragspartners zur Feststellung der Identität der wirtschaftlich berechtigten Person(en)", fontTitle);
            document.add(mainTitle);



            // Subtitle 1 content
            String[] checkboxLabelsSubtitle1 = new String[]{
                    // Wirtschaftlich berechtigter Questions
                    "Die nachstehende Person ist mit dem Vertragspartner identisch;",
                    "Die nachstehende Person ist mit dem Vermögenseinbringer identisch;",
                    "Die nachstehende Person ist Mitglied des leitenden Organs (Stiftungs-, Verwaltungsrat, Treunehmer);",
                    "Die nachstehende Person ist Protektor, Kurator oder mit ähnlichen Kompetenzen ausgestattete Person;",
                    "Die nachstehende Person ist Begünstigte, oder der Kreis von Personen, die als Begünstigte in Frage " +
                            "kommen des obigen Rechtsträgers;",
                    "Die nachstehende Person hält Anteile/Stimmrechte von >25% oder ist mit >25% am Gewinn beteiligt;",
                    "Die nachstehende Person übt auf andere Weise die Kontrolle über die Geschäftsführung oder letztlich " +
                            "direkt/indirekt die Kontrolle über das Vermögen des obigen Rechtsträgers aus.",

            };

            boolean[] checkboxValuesSubtitle1 = new boolean[]{
                    // Wirtschaftlich berechtigter Questions
                    // getter method for boolean: is instead of get
                    economicBeneficiary.isIdenticalToContractPartner(),
                    economicBeneficiary.isIdenticalToWealthContributor(),
                    economicBeneficiary.isMemberOfLeadershipBody(),
                    economicBeneficiary.isProtectorOrSimilar(),
                    economicBeneficiary.isBeneficiaryOrPotential(),
                    economicBeneficiary.isHoldsMoreThan25PercentShares(),
                    economicBeneficiary.isExercisesControlOverManagement()
            };


            String[] checkboxLabelsSubtitle2 = new String[]{
                    // New checkbox labels for USPersonQualificationModel
                    "a) Sind Sie im Besitz der US-Staatsbürgerschaft?",
                    "b) Sind Sie in den USA (oder in einem der US Territorien) geboren?",
                    "c) Sofern die Frage b) angekreutzt wurde: Sind Sie im Besitz eines Dokumentes, welches die " +
                            "definitive Aufgabe der US-Staatsbürgerschaft beweist („Certificate of Loss of Nationality“)" +
                            " und verfügen Sie über einen nicht-amerikanischen Pass oder einen anderen behördlich " +
                            "ausgestellten Ausweis, der Ihre Staatsbürgerschaft eines anderen Staates als der USA bestätigt?",
                    "d) Sind sie im Besitz einer gültigen amerikanischen „Green Card“?",
                    "e) Sind Sie im Besitz einer von der US-Amerikanischen Einwanderungs- und Einbürgerungs-behörde " +
                            "(US Citizenship and Immigration Services, USCIS) gültig ausgestellten Ausländer-Registrierungs-Karte" +
                            " für permanent niederlassungsberechtigte Personen?",
                    "f) Haben Sie aus US-steuerlicher Sicht Wohnsitz in den USA?",
                    "g) Sind Sie aus einem anderen Grund in den USA unbeschränkt steuerpflichtig?",

            };

            boolean[] checkboxValuesSubtitle2 = new boolean[]{

                    // us person questions
                    economicBeneficiary.isUsCitizen(),
                    economicBeneficiary.isBornInUSTerritories(),
                    economicBeneficiary.isHasCertificateOfLossOfNationality(),
                    economicBeneficiary.isHasGreenCard(),
                    economicBeneficiary.isHasUSImmigrationServiceCard(),
                    economicBeneficiary.isHasUSResidenceForTax(),
                    economicBeneficiary.isUSResidentForOtherReasons()
            };


            // Subtitle 1
            Paragraph subtitle1 = new Paragraph("1. Feststellung der letztlich wirtschaftlich berechtigten Person (kurz WB) des Rechtsträgers", fontSubTitle);
            document.add(subtitle1);
            for (int i = 0; i < checkboxLabelsSubtitle1.length; i++) {
                Chunk checkbox = checkboxValuesSubtitle1[i] ? new Chunk("[X] ", fontContent) : new Chunk("[ ] ", fontContent);
                Paragraph paragraph = new Paragraph(checkbox);
                paragraph.add(new Phrase(checkboxLabelsSubtitle1[i], fontContent));
                document.add(paragraph);
            }

            // Subtitle 2
            Paragraph subtitle2 = new Paragraph("2. Angaben zu Ihrer Person", fontSubTitle);
            document.add(subtitle2);
            PdfPTable customerDetailsTable = new PdfPTable(2); // 2 columns
            customerDetailsTable.setWidthPercentage(100);
            customerDetailsTable.setSpacingBefore(10f);
            customerDetailsTable.setSpacingAfter(10f);

            // Adding cells to the table
            addCell(customerDetailsTable, "Name:", customer.getLastName() + " " + customer.getFirstName());
            addCell(customerDetailsTable, "Geburtsdatum:", customer.getBirthDate());
            addCell(customerDetailsTable, "Nationalität:", customer.getNationality());
            addCell(customerDetailsTable, "Wohnadresse:", customer.getStreetName() + " " + customer.getStreetNumber());
            addCell(customerDetailsTable, "PLZ/Ort:", customer.getPlzNumber());
            addCell(customerDetailsTable, "Wohnsitzstaat:", customer.getCountry());
            addCell(customerDetailsTable, "Wohnsitz steuerlich:", customer.getTaxCountry());
            addCell(customerDetailsTable, "Steueridentifikations-Nr./TIN:", customer.getTaxIdentificationNumber());

            // Adding the table to the document
            document.add(customerDetailsTable);

            // Subtitle 2
            Paragraph subtitle3 = new Paragraph("3. Indizien bezüglich Qualifizierung als US-Person", fontSubTitle);
            document.add(subtitle3);
            Paragraph text1 = new Paragraph("Ist die oben, unter Ziff. 1 genannte Person ein US-Staatsbürger?" +
                    " Auf die genannte Person treffen folgende Merkmale zu (bitte Fragen durch Ankreuzen beantworten):");
            document.add(text1);
            for (int i = 0; i < checkboxLabelsSubtitle2.length; i++) {
                Chunk checkbox = checkboxValuesSubtitle2[i] ? new Chunk("[X] ", fontContent) : new Chunk("[ ] ", fontContent);
                Paragraph paragraph = new Paragraph(checkbox);
                paragraph.add(new Phrase(checkboxLabelsSubtitle2[i], fontContent));
                document.add(paragraph);
            }
            Paragraph text2 = new Paragraph("Ist eine oder sind mehrere Fragen (mit Ausnahme von c) angekreuzt worden, wird die " +
                    "unterzeichnende Person gebeten, zusätzlich das Formular W-9 auszufüllen und zu unterzeichnen. ");
            document.add(text2);

            // Generate the signature image from the decoded byte array
            Image signature = Image.getInstance(signatureBytes);
            signature.scaleToFit(150, 50); // Adjust the size as needed
            document.add(signature); // Add the signature image to the PDF document



        } catch (DocumentException e) {
            e.printStackTrace();
        } finally {
            if (document != null) {
                document.close();
            }
        }


    }

    private void addCell(PdfPTable table, String label, String value) {
        PdfPCell labelCell = new PdfPCell(new Phrase(label));
        PdfPCell valueCell = new PdfPCell(new Phrase(value));
        table.addCell(labelCell);
        table.addCell(valueCell);
    }

}

