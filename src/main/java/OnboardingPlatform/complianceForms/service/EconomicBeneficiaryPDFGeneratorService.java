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

    public void exportToFileEco(String filePath, Customer customer, EconomicBeneficiary economicBeneficiary, byte[] signatureBytes, String currentPlace) throws IOException {

        DateFormat dateFormatter = new SimpleDateFormat("yyyy-dd-MM_hh-mm-ss");
        String currentDateTime = dateFormatter.format(new Date());

        String fileName = "EconomicBeneficiary" + currentDateTime + ".pdf";
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


                // Subtitle 1
                Paragraph subtitle1 = new Paragraph("1. Feststellung der letztlich wirtschaftlich berechtigten Person (kurz WB) des Rechtsträgers", fontSubTitle);
                document.add(subtitle1);

                // Table for checkbox values in Subtitle 1
                PdfPTable checkboxTable = new PdfPTable(2);
                checkboxTable.setWidthPercentage(100);
                checkboxTable.getDefaultCell().setBorder(Rectangle.NO_BORDER);

                // Set widths for the columns
                float[] columnWidths = {0.2f, 3.5f}; // Adjusted the left column width to be smaller
                checkboxTable.setWidths(columnWidths);

                for (int i = 0; i < checkboxLabelsSubtitle1.length; i++) {
                    Chunk checkbox = checkboxValuesSubtitle1[i] ? new Chunk("[X] ", fontContent) : new Chunk("[ ] ", fontContent);
                    PdfPCell checkboxCell = new PdfPCell(new Phrase(checkbox));
                    checkboxCell.setBorder(Rectangle.NO_BORDER);
                    checkboxCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    checkboxCell.setHorizontalAlignment(Element.ALIGN_LEFT); // Align checkbox value left-centred

                    PdfPCell labelCell = new PdfPCell(new Phrase(checkboxLabelsSubtitle1[i], FontFactory.getFont(FontFactory.HELVETICA, 14)));
                    labelCell.setBorder(Rectangle.NO_BORDER);
                    labelCell.setVerticalAlignment(Element.ALIGN_MIDDLE);

                    checkboxTable.addCell(checkboxCell);
                    checkboxTable.addCell(labelCell);
                }

                document.add(checkboxTable);

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


                Paragraph subtitle3 = new Paragraph("3. Rechtliche Hinweise", fontSubTitle);
                document.add(subtitle3);

                // Add the additional text before the signature
                String additionalText =
                        "Eine vorsätzlich oder fahrlässig falsch erteilte Erklärung ist strafbar. Selbiges gilt, wenn " +
                                "Änderungen der Gegebenheiten nicht mitgeteilt werden oder über Änderungen der Gegebenheiten " +
                                "falsche Angaben gemacht werden.\n" +
                                "Ich, der unterzeichnende Vertragspartner bestätige die Richtigkeit und Vollständigkeit " +
                                "sämtlicher von mir gemachten Angaben. Ich verpflichte mich, allfällige Änderungen unaufgefordert " +
                                "und unverzüglich CorPa Treuhand AG schriftlich mitzuteilen.\n" +
                                "Ich nehme darüber hinaus zur Kenntnis und bin damit einverstanden, dass im Falle einer " +
                                "Konto-/Depoteröffnung bei einer Bank der Inhalt dieser Erklärung der Bank gegenüber bekannt zu geben ist.";

                // Create a paragraph for the additional text
                Paragraph additionalParagraph = new Paragraph(additionalText, fontContent);
                document.add(additionalParagraph);

                Paragraph placeAndDate = new Paragraph("\nOrt und Datum: " + currentPlace + ", " + formattedDate, fontContent);
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

