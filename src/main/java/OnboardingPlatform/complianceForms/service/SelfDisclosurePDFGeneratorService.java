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
        fontContent.setSize(10);

        // Create an instance of LogoReader
        LogoReader logoReader = new LogoReader();

        try {

            BufferedImage companyLogo = logoReader.readLogo();
            PdfWriter.getInstance(document, new FileOutputStream(completeFilePath));
            document.open();

            Font fontTitle = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
            fontTitle.setSize(14);

            Font fontSubTitle = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
            fontSubTitle.setSize(11);

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
            Paragraph titleParagraph = new Paragraph("Selbstauskunft", fontTitle);
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
            addCell(customerDetailsTable, "Nachname, Vorname:", customer.getLastName() + " " + customer.getFirstName());
            addCell(customerDetailsTable, "Geburtsdatum:", customer.getBirthDate());
            addCell(customerDetailsTable, "Nationalität:", customer.getNationality());
            addCell(customerDetailsTable, "Wohnadresse:", customer.getStreetName() + " " + customer.getStreetNumber());
            addCell(customerDetailsTable, "PLZ/Ort:", customer.getPlzNumber());
            addCell(customerDetailsTable, "Wohnsitzstaat:", customer.getCountry());
            addCell(customerDetailsTable, "Land der unbeschränkten Steuerpflicht:", customer.getTaxCountry());
            addCell(customerDetailsTable, "Steueridentifikations-Nr./TIN:", customer.getTaxIdentificationNumber());
            addCell(customerDetailsTable, "Telefonnummer:", customer.getPhoneNumber());
            addCell(customerDetailsTable, "E-Mail Adresse:", customer.getEmailAddress());
            addCell(customerDetailsTable, "Beruf:", customer.getJobTitle());

            // Adding the table to the document
            document.add(customerDetailsTable);

            // Subtitle 2
            Paragraph subtitle2 = new Paragraph("2. Indizien bezüglich Qualifizierung als US-Person", fontSubTitle);
            document.add(subtitle2);

            // Table for checkbox values in Subtitle 2
            PdfPTable checkboxTableSubtitle2 = new PdfPTable(2);
            checkboxTableSubtitle2.setWidthPercentage(100);
            checkboxTableSubtitle2.getDefaultCell().setBorder(Rectangle.NO_BORDER);

            // Set widths for the columns
            float[] columnWidthsSubtitle2 = {0.2f, 3.5f}; // Adjusted the left column width to be smaller
            checkboxTableSubtitle2.setWidths(columnWidthsSubtitle2);

            for (int i = 0; i < checkboxLabelsSubtitle2.length; i++) {
                Chunk checkbox = checkboxValuesSubtitle2[i] ? new Chunk("[X] ", fontContent) : new Chunk("[ ] ", fontContent);
                PdfPCell checkboxCell = new PdfPCell(new Phrase(checkbox));
                checkboxCell.setBorder(Rectangle.NO_BORDER);
                checkboxCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                checkboxCell.setHorizontalAlignment(Element.ALIGN_LEFT); // Align checkbox value left-centred

                PdfPCell labelCell = new PdfPCell(new Phrase(checkboxLabelsSubtitle2[i], FontFactory.getFont(FontFactory.HELVETICA, 12)));
                labelCell.setBorder(Rectangle.NO_BORDER);
                labelCell.setVerticalAlignment(Element.ALIGN_MIDDLE);

                checkboxTableSubtitle2.addCell(checkboxCell);
                checkboxTableSubtitle2.addCell(labelCell);
            }

            document.add(checkboxTableSubtitle2);


            Paragraph text2 = new Paragraph("\nIst eine oder sind mehrere Fragen (mit Ausnahme von c) angekreuzt worden, wird die " +
                    "unterzeichnende Person gebeten, zusätzlich das Formular W-9 auszufüllen und zu unterzeichnen. ");
            document.add(text2);


            // Subtitle 3
            Paragraph subtitle3 = new Paragraph("\n3. Steuerliche Ansässigkeit in einem EU-Mitgliedstaat", fontSubTitle);
            document.add(subtitle3);

            // Table for checkbox values in Subtitle 3
            PdfPTable checkboxTableSubtitle3 = new PdfPTable(2);
            checkboxTableSubtitle3.setWidthPercentage(100);
            checkboxTableSubtitle3.getDefaultCell().setBorder(Rectangle.NO_BORDER);

            // Set widths for the columns
            float[] columnWidthsSubtitle3 = {0.2f, 3.5f}; // Adjusted the left column width to be smaller
            checkboxTableSubtitle3.setWidths(columnWidthsSubtitle3);

            // EU Tax Residency with checkboxes
            boolean euTaxResidencyValue = selfDisclosure.isEuTaxResidency();
            String euTaxResidencyLabel = "Haben Sie eine steuerliche Ansässigkeit in einem EU-Mitgliedstaat?\n ";
            addCheckboxRow(checkboxTableSubtitle3, euTaxResidencyLabel, euTaxResidencyValue);

            Paragraph euTaxResidencyConfirmation = new Paragraph("Falls Sie die Frage mit „Ja“ beantwortet haben, " +
                    "bestätigen Sie hiermit, dass Sie über die EU-Steuer- transparenzrichtlinie 2018/822/EU DAC6 unterrichtet" +
                    " wurden und Ihnen das Kunden-Informations- schreiben EU-Steuertransparenzrichtlinie DAC6 der CorPa " +
                    "Treuhand AG ausgehändigt wurde.\n\n\n");

            document.add(checkboxTableSubtitle3);
            document.add(euTaxResidencyConfirmation);

            // Subtitle 4
            Paragraph subtitle4 = new Paragraph("\n4. Steuerliche Ansässigkeit in UK\n", fontSubTitle);
            document.add(subtitle4);

            // Table for checkbox values in Subtitle 4
            PdfPTable checkboxTableSubtitle4 = new PdfPTable(2);
            checkboxTableSubtitle4.setWidthPercentage(100);
            checkboxTableSubtitle4.getDefaultCell().setBorder(Rectangle.NO_BORDER);

            // Set widths for the columns
            float[] columnWidthsSubtitle4 = {0.2f, 3.5f}; // Adjusted the left column width to be smaller
            checkboxTableSubtitle4.setWidths(columnWidthsSubtitle4);

            // UK Tax Residency with checkboxes
            boolean ukTaxResidencyValue = selfDisclosure.isUkTaxResidency();
            String ukTaxResidencyLabel = "Haben Sie eine steuerliche Ansässigkeit in UK? \n ";
            addCheckboxRow(checkboxTableSubtitle4, ukTaxResidencyLabel, ukTaxResidencyValue);

            if (ukTaxResidencyValue) {
                Paragraph ukTaxResidencyConfirmation = new Paragraph("Falls Sie die Frage mit „Ja“ beantwortet haben, bestätigen Sie" +
                        " hiermit, dass Sie über die Richtlinie der Liechtensteinischen Treuhandkammer zur Vorgehensweise bei der Gründung " +
                        "und Verwaltung von Rechtsträgern für UK Personen unterrichtet wurden.");
                document.add(ukTaxResidencyConfirmation);
            }

            Paragraph ukTaxResidencyConfirmation = new Paragraph("Falls Sie die Frage mit „Ja“ beantwortet haben, bestätigen Sie hiermit, dass Sie über die Richtlinie \n" +
                    "der Liechtensteinischen Treuhandkammer zur Vorgehensweise bei der Gründung und Verwaltung \n" +
                    "von Rechtsträgern für UK Personen unterrichtet wurden.\n");


            document.add(checkboxTableSubtitle4);
            document.add(ukTaxResidencyConfirmation);

            // Subtitle 5
            Paragraph subtitle5 = new Paragraph("\n5. Politisch exponierte Person (PEP)\n", fontSubTitle);
            document.add(subtitle5);

            // Table for checkbox values in Subtitle 5
            PdfPTable checkboxTableSubtitle5 = new PdfPTable(2);
            checkboxTableSubtitle5.setWidthPercentage(100);
            checkboxTableSubtitle5.getDefaultCell().setBorder(Rectangle.NO_BORDER);

            // Set widths for the columns
            float[] columnWidthsSubtitle5 = {0.2f, 3.5f}; // Adjusted the left column width to be smaller
            checkboxTableSubtitle5.setWidths(columnWidthsSubtitle5);

            // PEP questions with checkboxes
            boolean pepValue = selfDisclosure.isPoliticallyExposedPerson();
            String pepLabel = "Sind Sie eine politisch exponierte Person (PEP)? \n ";
            addCheckboxRow(checkboxTableSubtitle5, pepLabel, pepValue);

            if (pepValue) {

                Paragraph relatedToPep = new Paragraph("Haben Sie ein Naheverhältnis (unmittelbares Familienmitglied, enge Geschäftsbeziehung, sozial oder politisch enge Verbundenheit) zu einer politisch exponierten Person?");
                boolean relatedToPepValue = selfDisclosure.isRelatedToPep();
                Paragraph relatedToPepParagraph = new Paragraph(relatedToPep);
                addCheckboxRow2(checkboxTableSubtitle5, relatedToPepParagraph, relatedToPepValue);

            }
            document.add(checkboxTableSubtitle5);
            Paragraph pepPosition = new Paragraph("Falls ja, in welchem/r Amt/Funktion? \n " + "Amt/Funktion: " + selfDisclosure.getPepAssociatedPosition());
            document.add(pepPosition);
            Paragraph relatedPersonDetails = new Paragraph("Falls im Naheverhältnis, Art/Name/Vorname der nahestehenden Person, Amt und Funktion? \n " + "Name/Vorname, Amt/Funktion: " + selfDisclosure.getRelatedPersonDetails());
            document.add(relatedPersonDetails);


            Paragraph subtitle6 = new Paragraph("\n6. Rechtliche Hinweise und Steuerkonformitätserklärung\n", fontSubTitle);
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
        Font contentFont = FontFactory.getFont(FontFactory.HELVETICA); // New content font

        PdfPCell labelCell = new PdfPCell(new Phrase(label, boldFont));
        PdfPCell valueCell = new PdfPCell(new Phrase(value, contentFont)); // Updated to use contentFont

        // Set cell properties
        labelCell.setHorizontalAlignment(Element.ALIGN_LEFT);
        labelCell.setPadding(5);

        valueCell.setHorizontalAlignment(Element.ALIGN_LEFT);
        valueCell.setPadding(5);

        // Add cells to the table
        table.addCell(labelCell);
        table.addCell(valueCell);
    }

    // Function to add checkbox rows to the table
    private void addCheckboxRow(PdfPTable table, String label, boolean value) {
        Font fontContent = FontFactory.getFont(FontFactory.HELVETICA);
        fontContent.setSize(12);

        Chunk checkbox = value ? new Chunk("[X] ", fontContent) : new Chunk("[ ] ", fontContent);

        PdfPCell checkboxCell = new PdfPCell(new Phrase(checkbox));
        checkboxCell.setBorder(Rectangle.NO_BORDER);
        checkboxCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        checkboxCell.setHorizontalAlignment(Element.ALIGN_MIDDLE);

        PdfPCell labelCell = new PdfPCell(new Phrase(label, FontFactory.getFont(FontFactory.HELVETICA, 12)));
        labelCell.setBorder(Rectangle.NO_BORDER);
        labelCell.setVerticalAlignment(Element.ALIGN_MIDDLE);

        table.addCell(checkboxCell);
        table.addCell(labelCell);
    }

    private void addCheckboxRow2(PdfPTable table, Paragraph label, boolean value) {
        Font fontContent = FontFactory.getFont(FontFactory.HELVETICA);
        fontContent.setSize(12);

        Chunk checkbox = value ? new Chunk("[X] ", fontContent) : new Chunk("[ ] ", fontContent);

        PdfPCell checkboxCell = new PdfPCell(new Phrase(checkbox));
        checkboxCell.setBorder(Rectangle.NO_BORDER);
        checkboxCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        checkboxCell.setHorizontalAlignment(Element.ALIGN_MIDDLE);

        PdfPCell labelCell = new PdfPCell(label);
        labelCell.setBorder(Rectangle.NO_BORDER);
        labelCell.setVerticalAlignment(Element.ALIGN_MIDDLE);

        table.addCell(checkboxCell);
        table.addCell(labelCell);
    }

}

