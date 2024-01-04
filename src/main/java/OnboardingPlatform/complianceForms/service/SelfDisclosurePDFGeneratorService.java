package OnboardingPlatform.complianceForms.service;


import OnboardingPlatform.complianceForms.model.EconomicBeneficiary;
import OnboardingPlatform.complianceForms.model.SelfDisclosure;
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
public class SelfDisclosurePDFGeneratorService {

    public void exportToFileSelf(String filePath, Customer customer, SelfDisclosure selfDisclosure, byte[] signatureBytes, String currentPlace) throws IOException {

        DateFormat dateFormatter = new SimpleDateFormat("yyyy-dd-MM_hh-mm-ss");
        String currentDateTime = dateFormatter.format(new Date());

        String fileName = "SelfDisclosure" + currentDateTime + ".pdf";
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
                    selfDisclosure.isUsCitizen(),
                    selfDisclosure.isBornInUSTerritories(),
                    selfDisclosure.isHasCertificateOfLossOfNationality(),
                    selfDisclosure.isHasGreenCard(),
                    selfDisclosure.isHasUSImmigrationServiceCard(),
                    selfDisclosure.isHasUSResidenceForTax(),
                    selfDisclosure.isUSResidentForOtherReasons()
            };


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


            Paragraph subtitle3 = new Paragraph("3. Steuerliche Ansässigkeit in einem EU-Mitgliedstaat", fontSubTitle);
            document.add(subtitle3);

            // EU Tax Residency
            Chunk euTaxResidencyChunk = selfDisclosure.isEuTaxResidency() ? new Chunk("Ja", fontContent) : new Chunk("Nein", fontContent);
            Paragraph euTaxResidencyParagraph = new Paragraph("Haben Sie eine steuerliche Ansässigkeit in einem EU-Mitgliedstaat? ");
            euTaxResidencyParagraph.add(euTaxResidencyChunk);
            document.add(euTaxResidencyParagraph);

            if (selfDisclosure.isEuTaxResidency()) {
                Paragraph euTaxResidencyConfirmation = new Paragraph("Falls Sie die Frage mit „Ja“ beantwortet haben, bestätigen Sie hiermit, dass Sie über die EU-Steuer- transparenzrichtlinie 2018/822/EU DAC6 unterrichtet wurden und Ihnen das Kunden-Informations- schreiben EU-Steuertransparenzrichtlinie DAC6 der CorPa Treuhand AG ausgehändigt wurde.");
                document.add(euTaxResidencyConfirmation);
            }

            Paragraph subtitle4 = new Paragraph("4. Steuerliche Ansässigkeit in UK", fontSubTitle);
            document.add(subtitle4);

            // UK Tax Residency
            Chunk ukTaxResidencyChunk = selfDisclosure.isUkTaxResidency() ? new Chunk("Ja", fontContent) : new Chunk("Nein", fontContent);
            Paragraph ukTaxResidencyParagraph = new Paragraph("Haben Sie eine steuerliche Ansässigkeit in UK? ");
            ukTaxResidencyParagraph.add(ukTaxResidencyChunk);
            document.add(ukTaxResidencyParagraph);

            if (selfDisclosure.isUkTaxResidency()) {
                Paragraph ukTaxResidencyConfirmation = new Paragraph("Falls Sie die Frage mit „Ja“ beantwortet haben, bestätigen Sie hiermit, dass Sie über die Richtlinie der Liechtensteinischen Treuhandkammer zur Vorgehensweise bei der Gründung und Verwaltung von Rechtsträgern für UK Personen unterrichtet wurden.");
                document.add(ukTaxResidencyConfirmation);
            }

            Paragraph subtitle5 = new Paragraph("5. Politisch exponierte Person (PEP)", fontSubTitle);
            document.add(subtitle5);

            // PEP questions
            Chunk pepChunk = selfDisclosure.isPoliticallyExposedPerson() ? new Chunk("Ja", fontContent) : new Chunk("Nein", fontContent);
            Paragraph pepParagraph = new Paragraph("Sind Sie eine politisch exponierte Person (PEP)? ");
            pepParagraph.add(pepChunk);
            document.add(pepParagraph);

            if (selfDisclosure.isPoliticallyExposedPerson()) {
                Paragraph pepPosition = new Paragraph("Falls ja, in welchem/r Amt/Funktion? " + selfDisclosure.getPepAssociatedPosition());
                document.add(pepPosition);

                Paragraph relatedToPep = new Paragraph("Haben Sie ein Naheverhältnis (unmittelbares Familienmitglied, enge Geschäftsbeziehung, sozial oder politisch enge Verbundenheit) zu einer politisch exponierten Person?");
                Chunk relatedToPepChunk = selfDisclosure.isRelatedToPep() ? new Chunk("Ja", fontContent) : new Chunk("Nein", fontContent);
                relatedToPep.add(relatedToPepChunk);
                document.add(relatedToPep);

                if (selfDisclosure.isRelatedToPep()) {
                    Paragraph relatedPersonDetails = new Paragraph("Falls im Naheverhältnis, Art/Name/Vorname der nahestehenden Person, Amt und Funktion? " + selfDisclosure.getRelatedPersonDetails());
                    document.add(relatedPersonDetails);
                }
            }


            Paragraph subtitle6 = new Paragraph("6. Rechtliche Hinweise und Steuerkonformitätserklärung", fontSubTitle);
            document.add(subtitle6);

            // Add the additional text before the signature
            String additionalText =
                            "Eine vorsätzlich oder fahrlässig falsch erteilte Selbstauskunft ist strafbar. " +
                            "Selbiges gilt, wenn Änderungen der Gegebenheiten nicht mitgeteilt werden oder " +
                            "über Änderungen der Gegebenheiten falsche Angaben gemacht werden.\n\n" +
                            "Mit meiner Unterschrift bestätige ich, dass alle von mir gemachten Angaben richtig " +
                            "und vollständig sind. Ich verpflichte mich, allfällige Änderungen unaufgefordert und " +
                            "unverzüglich CorPa Treuhand AG schriftlich mitzuteilen.\n\n" +
                            "Ich bestätige zudem, dass die im Zusammenhang mit dem Mandat eingebrachten/geschenkten/" +
                            "übertragenen Vermögenswerte und/oder die daraus erzielten Einkünfte und Gewinne bei " +
                            "allen zuständigen Steuerbehörden, insbesondere in meinem steuerlichen Domizilland, " +
                            "deklariert worden sind und weiterhin von mir deklariert werden. \n\n" +
                            "Ich habe keine Steuerhinterziehung, Geldwäsche oder andere Strafdelikte begangen und " +
                            "bin diesbezüglich nicht in einem Gerichts- oder Verwaltungsverfahren verurteilt worden. " +
                            "Die im Zusammenhang mit dem Mandat eingebrachten/geschenkten/übertragenen " +
                            "Vermögenswerte und/oder die daraus erzielten Einkünfte und Gewinne sind keine " +
                            "Erlöse aus Steuerkriminalität oder sonstigen kriminellen Machenschaften.";


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

