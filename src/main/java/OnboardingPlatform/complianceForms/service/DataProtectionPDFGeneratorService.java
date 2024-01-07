package OnboardingPlatform.complianceForms.service;

import OnboardingPlatform.complianceForms.model.Customer;
import OnboardingPlatform.complianceForms.model.EconomicBeneficiary;
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import org.apache.tomcat.util.http.fileupload.ByteArrayOutputStream;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

@Service
public class DataProtectionPDFGeneratorService {

    public void exportToFileDataProtection(String filePath, Customer customer, byte[] signatureBytes, String currentPlace) throws IOException {

        DateFormat dateFormatter = new SimpleDateFormat("yyyy-dd-MM_hh-mm-ss");
        String currentDateTime = dateFormatter.format(new Date());

        String fileName = "DataProtection" + currentDateTime + ".pdf";
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
            Paragraph titleParagraph = new Paragraph("Information nach Art. 13 EU-Datenschutz-Grundverordnung (DSGVO)", fontTitle);
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

            // Add the additional text before the signature
            String introductionText =
                    "Vertrauen ist wichtig, besonders wenn es um Ihre Daten geht. Aus diesem Grund erachten wir es als " +
                            "unsere Pflicht, nur die Daten zu erheben, welche unbedingt erforderlich sind sowie die Daten" +
                            " mit der gebotenen Sorgfalt zu verwalten und vor Missbräuchen zu schützen. \n" +
                            "Die CorPa Treuhand AG und ihr nahestehende Gesellschaften halten sich strikt an die " +
                            "datenschutzrecht-lichen Vorschriften bei der Erhebung und Verarbeitung Ihrer Daten.\n" +
                            "Nachstehend finden Sie Informationen zur Verarbeitung Ihrer Daten und den Ihnen zukommenden Rechten: \n";


            // Create a paragraph for the additional text
            Paragraph IntroductionPara = new Paragraph(introductionText, fontContent);
            document.add(IntroductionPara);

            Paragraph titleParagraph1 = new Paragraph("\n1.\t Name und Kontaktdaten des Verantwortlichen\n", fontSubTitle);
            document.add(titleParagraph1);

            // subsection 1 - contact details
            String contactDetails =
                            "• CorPa Treuhand AG\n" +
                            "• Meierhofstrasse 2\n" +
                            "• 9490 Vaduz\n" +
                            "• Liechtenstein\n" +
                            "• office@corpatrust.com\n" +
                            "• +423 239 01 01\n";

            Paragraph contactDetailsPara = new Paragraph(contactDetails, fontContent);
            document.add(contactDetailsPara);


            Paragraph titleParagraph2 = new Paragraph("\n2.\t Kategorien der erhobenen personenbezogenen Daten, Zwecke und Rechtsgrundlagen der Verarbeitung\n", fontSubTitle);
            document.add(titleParagraph2);

            // Sub-subsection 2.1
            Paragraph subSubtitle2_1 = new Paragraph("2.1 Die CorPa Treuhand AG und ihr nahestehende Gesellschaften erheben insbesondere die folgenden Daten:\n", fontContent);
            document.add(subSubtitle2_1);

            // List of data categories
            String[] dataCategories = {
                    "• Klienten- und Adressdaten;",
                    "• Legitimationsdaten;",
                    "• Sorgfaltspflichtdaten;",
                    "• Mandatsinformationen;",
                    "• Buchführungsdaten;",
                    "• Rechtsträgerdaten;",
                    "• Steuermeldedaten;",
                    "• Korrespondenz."
            };

            for (String category : dataCategories) {
                Paragraph categoryPara = new Paragraph(category, fontContent);
                document.add(categoryPara);
            }

            // Content for data purposes and legal basis
            String dataPurposes =
                    "\n2.2 Die Erhebung dieser Daten erfolgt,\n" +
                            "• um Sie als unseren Kunden identifizieren zu können;\n" +
                            "• um das uns übertragene Mandat erfüllen zu können;\n" +
                            "• zur Erfüllung der gesetzlichen Buchführungspflichten;\n" +
                            "• zur Korrespondenz mit Ihnen;\n" +
                            "• zur Rechnungsstellung.\n\n" +
                            "2.3. Ihre Daten werden auf Grundlage von Art. 6 Abs. 1 Bst. b (vertragliche Beziehung) " +
                            "und c (Erfüllung einer rechtlichen Verpflichtung) DSGVO verarbeitet.";

            Paragraph dataPurposesPara = new Paragraph(dataPurposes, fontContent);
            document.add(dataPurposesPara);

            Paragraph titleParagraph3 = new Paragraph("\n3.\t Herkunft der Daten\n", fontSubTitle);
            document.add(titleParagraph3);

            // Content for data origin
            String dataOrigin =
                            "3.1. Ihre Daten erheben wir direkt (Besprechungen und Korrespondenz) " +
                            "oder über interne Hinter-grund- und Sorgfaltspflichtabklärungen.\n" +
                            "3.2. Allenfalls können Daten durch Dritte Dienstleister erhoben werden. " +
                            "Dritte Dienstleister können insbesondere sein:\n" +
                            "• Banken;\n" +
                            "• Vermögensverwalter;\n" +
                            "• Revisoren.";

            Paragraph dataOriginPara = new Paragraph(dataOrigin, fontContent);
            document.add(dataOriginPara);

            // Content for recipients of the collected personal data
            Paragraph titleParagraph4 = new Paragraph("\n\n4.\t Empfänger der erhobenen personenbezogenen Daten\n", fontSubTitle);
            document.add(titleParagraph4);

            String dataRecipients =
                            "4.1. Ihre Daten werden von uns ausschließlich zur Erfüllung unserer vertraglichen, " +
                            "gesetzlichen und aufsichtsrechtlichen Pflichten zu den unter Ziff. 2.2. aufgeführten " +
                            "Zwecken verarbeitet.\n" +
                            "4.2. Hierzu können insbesondere folgende externe Dienstleister Daten erhalten:\n" +
                            "• Banken;\n" +
                            "• Vermögensverwalter;\n" +
                            "• Versicherungen;\n" +
                            "• Rechtsanwälte;\n" +
                            "• Revisoren.\n" +
                            "\n4.3. Sind von uns gesetzliche oder aufsichtsrechtliche Pflichten zu erfüllen, können " +
                            "insbesondere folgende Stellen Daten erhalten:\n" +
                            "• Amtsstellen (z. B. Aufsichtsbehörden, Gerichte);\n" +
                            "• Steuerbehörden (u.a. auch im Rahmen des AIA und der FATCA).";

            Paragraph dataRecipientsPara = new Paragraph(dataRecipients, fontContent);
            document.add(dataRecipientsPara);

            // Content for transfer of personal data to third countries a

            Paragraph titleParagraph5 = new Paragraph("\n5.\t Transfer von erhobenen personenbezogenen Daten in Drittstaaten\n", fontSubTitle);
            document.add(titleParagraph5);

            String dataTransferAndStorage =
                            "Zur Erfüllung vertraglicher und rechtlicher Verpflichtungen können Ihre Daten " +
                            "direkt oder indirekt an dritte Dienstleister in Drittstaaten transferiert werden.\n";

            Paragraph dataTransferAndStoragePara = new Paragraph(dataTransferAndStorage, fontContent);
            document.add(dataTransferAndStoragePara);

            // Content for transfer of personal data to third countries and data storage duration

            Paragraph titleParagraph6 = new Paragraph("\n6. \t Dauer der Speicherung der erhobenen personenbezogenen Daten\n", fontSubTitle);
            document.add(titleParagraph6);

            String duration =
                            "Ihre Daten werden während der Geschäftsbeziehung im Rahmen der gesetzlichen " +
                            "Bestimmungen verarbeitet und gespeichert. Nach Beendigung der Geschäftsbeziehung " +
                            "werden Ihre Daten so lange gespeichert, wie dies unter Beachtung der gesetzlichen " +
                            "Aufbewahrungsfristen erforderlich ist.";
            Paragraph durationPara = new Paragraph(duration, fontContent);
            document.add(durationPara);



            // Content for data subject rights and customer acknowledgment

            Paragraph titleParagraph7 = new Paragraph("\n7. \t Betroffenenrechte\n", fontSubTitle);
            document.add(titleParagraph7);

            String dataSubjectRights =
                            "Nach der EU-Datenschutz-Grundverordnung stehen Ihnen folgende Rechte zu:\n" +
                            "• Auskunft zu erhalten über die verarbeiteten personenbezogenen Daten bzw. deren Kategorien, " +
                            "die Verarbeitungszwecke, die Kategorien von Empfängern, gegenüber denen Ihre Daten offengelegt " +
                            "wurden oder werden, die Absicht, Daten an ein Drittland oder eine internationale Organisation zu " +
                            "übermitteln einschließlich dafür geeigneter Garantien, die geplante Speicherdauer, das Bestehen " +
                            "eines Rechts auf Berichtigung, Löschung, Einschränkung der Verarbeitung oder Widerspruch, das " +
                            "Bestehen eines Beschwerderechts, die Herkunft Ihrer Daten, sofern diese nicht bei uns erhoben " +
                            "wurden, sowie über das Bestehen einer automatisierten Entscheidungsfindung einschließlich " +
                            "Profiling und ggf. aussagekräftigen Informationen zu deren Einzelheiten;\n" +
                            "• die Berichtigung, Ergänzung oder das Löschen Ihrer personenbezogenen Daten, die falsch sind " +
                            "oder nicht rechtskonform verarbeitet werden, zu verlangen;\n" +
                            "• von uns zu verlangen, die Verarbeitung Ihrer personenbezogenen Daten einzuschränken;\n" +
                            "• unter bestimmten Umständen der Verarbeitung Ihrer personenbezogenen Daten zu widersprechen;\n" +
                            "• Ihre personenbezogenen Daten, die Sie uns bereitgestellt haben, in einem strukturierten, " +
                            "gängigen und maschinenlesbaren Format zu erhalten oder die Übermittlung an einen anderen " +
                            "Verantwortlichen zu verlangen;\n" +
                            "• bei der zuständigen Datenschutzaufsichtsbehörde Beschwerde zu erheben.\n" +
                            "Empfangsbestätigung durch den Kunden";

            Paragraph dataSubjectRightsPara = new Paragraph(dataSubjectRights, fontContent);
            document.add(dataSubjectRightsPara);

            // End of the document

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
}


