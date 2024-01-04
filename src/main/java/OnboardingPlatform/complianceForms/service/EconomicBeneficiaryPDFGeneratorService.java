package OnboardingPlatform.complianceForms.service;

import OnboardingPlatform.complianceForms.model.EconomicBeneficiary;
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import OnboardingPlatform.complianceForms.model.Customer;
import org.apache.tomcat.util.http.fileupload.ByteArrayOutputStream;
import org.springframework.stereotype.Service;

import java.awt.image.BufferedImage;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import com.lowagie.text.Image;

import javax.imageio.ImageIO;

@Service
public class EconomicBeneficiaryPDFGeneratorService {

    public void exportToFile(String filePath, Customer customer, EconomicBeneficiary economicBeneficiary, byte[] signatureBytes, String currentPlace) throws IOException {

        DateFormat dateFormatter = new SimpleDateFormat("yyyy-dd-MM_hh-mm-ss");
        String currentDateTime = dateFormatter.format(new Date());

        String fileName = "EconomicBeneficiary" + currentDateTime + ".pdf";
        String completeFilePath = filePath + "/" + fileName;

        Date currentDateForSignature = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        String formattedDate = dateFormat.format(currentDateForSignature);

        Document document = new Document(PageSize.A4);
        Font fontContent = FontFactory.getFont(FontFactory.HELVETICA);
        fontContent.setSize(12);

        // Create an instance of LogoReader
        LogoReader logoReader = new LogoReader();

            try {

                BufferedImage companyLogo = logoReader.readLogo();
                PdfWriter.getInstance(document, new FileOutputStream(completeFilePath));
                document.open();

                Font fontTitle = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
                fontTitle.setSize(18);

                Font fontSubTitle = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
                fontSubTitle.setSize(12);

                // Main Title
                // Convert the BufferedImage to iText's Image format
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                ImageIO.write(companyLogo, "jpg", byteArrayOutputStream);
                Image logoImage = Image.getInstance(byteArrayOutputStream.toByteArray());
                logoImage.scaleToFit(80, 80); // Adjust the size as needed

                // Create a table for the header
                PdfPTable headerTable1 = new PdfPTable(2);
                headerTable1.setWidthPercentage(100);
                headerTable1.setWidths(new int[]{3, 1}); // Adjust the widths for left and right alignment

                // Add the title to the header
                Paragraph titleParagraph = new Paragraph("Erklärung des Vertragspartners zur Feststellung der Identität der wirtschaftlich berechtigten Person(en)", fontTitle);
                PdfPCell titleCell = new PdfPCell(titleParagraph);
                titleCell.setBorder(Rectangle.NO_BORDER);
                titleCell.setHorizontalAlignment(Element.ALIGN_LEFT); // Align title to the left
                headerTable1.addCell(titleCell);

                // Add the logo to the header
                PdfPCell logoCell = new PdfPCell(logoImage, true);
                logoCell.setBorder(Rectangle.NO_BORDER);
                logoCell.setHorizontalAlignment(Element.ALIGN_RIGHT); // Align logo to the right
                headerTable1.addCell(logoCell);

                // Add the header table to the document
                document.add(headerTable1);

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

                // Create the customer details table
                PdfPTable customerDetailsTable = new PdfPTable(2);
                // Set properties for the customer details table
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
                Paragraph subtitle2 = new Paragraph("2. Indizien bezüglich Qualifizierung als US-Person", fontSubTitle);
                document.add(subtitle2);
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

                Paragraph subtitle3 = new Paragraph("3. Rechtliche Hinweise", fontSubTitle);
                document.add(subtitle3);

                // Add the additional text before the signature
                String additionalText =
                        "Eine vorsätzlich oder fahrlässig falsch erteilte Erklärung ist strafbar. Selbiges gilt, wenn " +
                                "Änderungen der Gegebenheiten nicht mitgeteilt werden oder über Änderungen der Gegebenheiten " +
                                "falsche Angaben gemacht werden.\n\n" +
                                "Ich, der unterzeichnende Vertragspartner bestätige die Richtigkeit und Vollständigkeit " +
                                "sämtlicher von mir gemachten Angaben. Ich verpflichte mich, allfällige Änderungen unaufgefordert " +
                                "und unverzüglich CorPa Treuhand AG schriftlich mitzuteilen.\n\n" +
                                "Ich nehme darüber hinaus zur Kenntnis und bin damit einverstanden, dass im Falle einer " +
                                "Konto-/Depoteröffnung bei einer Bank der Inhalt dieser Erklärung der Bank gegenüber bekannt zu geben ist.";

                // Create a paragraph for the additional text
                Paragraph additionalParagraph = new Paragraph(additionalText, fontContent);
                document.add(additionalParagraph);

                Paragraph placeAndDate = new Paragraph("\n\nOrt und Datum: " + currentPlace + ", " + formattedDate, fontContent);
                document.add(placeAndDate);

                // Generate the signature image from the decoded byte array
                Image signature = Image.getInstance(signatureBytes);
                signature.scaleToFit(150, 50); // Adjust the size as needed

                Paragraph SignaturePara = new Paragraph("Unterschrift des Vertragspartners: ", fontContent);
                document.add(SignaturePara);
                document.add(signature); // picture can not be added to the paragraph, so we do it separately here directly to doc

                Paragraph FirstAndLastName = new Paragraph("Name des Vertragspartners: " + customer.getFirstName() + " " + customer.getLastName(), fontContent);
                document.add(FirstAndLastName);


            } catch (DocumentException e) {
                e.printStackTrace();
            } finally {
                if (document != null) {
                    document.close();
                }
            }
        }


    private void addCell(PdfPTable table, String label, String value) {
        Font boldFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
        PdfPCell labelCell = new PdfPCell(new Phrase(label, boldFont));
        PdfPCell valueCell = new PdfPCell(new Phrase(value));

        // Set cell properties
        labelCell.setHorizontalAlignment(Element.ALIGN_LEFT);
        labelCell.setPadding(5);

        valueCell.setHorizontalAlignment(Element.ALIGN_LEFT);
        valueCell.setPadding(5);

        // Add cells to the table
        table.addCell(labelCell);
        table.addCell(valueCell);
    }
}

