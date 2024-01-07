package OnboardingPlatform.complianceForms.service;

import OnboardingPlatform.complianceForms.model.Customer;
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
import java.util.Date;

@Service
public class InformationLetterPDFGeneratorService {

    public void exportToFileInformationLetter(String filePath) throws IOException {

        DateFormat dateFormatter = new SimpleDateFormat("yyyy-dd-MM_hh-mm-ss");
        String currentDateTime = dateFormatter.format(new Date());

        String fileName = "InformationLetter" + currentDateTime + ".pdf";
        String completeFilePath = filePath + "/" + fileName;

        Date currentDateForTheEnd= new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        String formattedDate = dateFormat.format(currentDateForTheEnd);

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
            headerTable1.setWidthPercentage(80);
            headerTable1.setWidths(new int[]{2, 1}); // Adjust the widths for left and right alignment

            // Add the title to the header
            Paragraph titleParagraph = new Paragraph("Kunden-Informationsschreiben \n" +
                    "EU-Steuertransparenzrichtlinie DAC6\n", fontTitle);
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

            // Introduction text
            String introductionText =
                    "Um grenzüberschreitende, potenziell aggressive Steueroptimierung aufzudecken, wurde in der EU eine neue" +
                    " Steuertransparenzrichtlinie (Richtlinie EU 2018/822 zur Änderung der Richtlinie EU 2011/16 bezüglich des" +
                    " verpflichtenden automatischen Informationsaustauschs im Bereich der Besteuerung über meldepflichtige" +
                    " grenzüberschreitende Gestaltungen), die allgemein unter der Abkürzung " +
                    "AC6 bekannt ist, eingeführt (vgl.: https://eur-lex.europa.eu/legal-content/EN/TXT/?uri=CELEX%3A32018L0822)." +
                    "iel der DAC6 ist es, Kenntnisse über grenzüberschreitende Steueroptimierungen zu erlangen um mögliche" +
                    " „Schlupflöcher“ in den nationalen Steuergesetzen zu schliessen und Daten von denjenigen Personen zu erhalten," +
                    " welche eine solche Steueroptimierung vornehmen, empfehlen oder zu deren Empfehlung beitragen. ";

            Paragraph IntroductionPara = new Paragraph(introductionText, fontContent);
            document.add(IntroductionPara);

            // 1. section
            Paragraph titleParagraph1 = new Paragraph("\n1.\t Was löst die Meldepflicht aus?\n", fontSubTitle);
            document.add(titleParagraph1);

            String text1 =
                    "Die DAC6 zielt auf eine zwingende Offenlegung von EU-grenzüberschreitenden Steuermodellen ab, sofern einer" +
                    " der Hauptvorteile der Gestaltung in einer geringeren steuerlichen Belastung (Main Benefit) " +
                    "liegt oder andere in der Richtlinie definierte Kriterien (sog. Hallmarks), also gewisse Ausprägungen " +
                    "oder Kennzeichen erfüllt sind. Grenzüberschreitender Natur ist die Gestaltung immer dann, wenn ein " +
                    "EU-Mitgliedstaat und mindestens ein anderer Staat (EU-Mitglied oder nicht) betroffen sind.";


            Paragraph text1Para = new Paragraph(text1, fontContent);
            document.add(text1Para);


            // 2. section
            Paragraph titleParagraph2 = new Paragraph("\n2.\t Was muss gemeldet werden?\n", fontSubTitle);
            document.add(titleParagraph2);

            String text2 =
                    "Es müssen 1) die Identifikation der beteiligten Steuerpflichtigen und Intermediäre, 2) Angaben zu den" +
                    " Kennzeichen, welche die Meldungspflicht ausgelöst haben, 3) eine Zusammenfassung der Vereinbarung," +
                    " 4) Angaben zu den relevanten inländischen Steuervorschriften und 5) der Wert der Vereinbarung gemeldet werden.";


            Paragraph text2Para = new Paragraph(text2, fontContent);
            document.add(text2Para);

            // 3. section
            Paragraph titleParagraph3 = new Paragraph("\n3.\tWer ist meldepflichtig?\n", fontSubTitle);
            document.add(titleParagraph3);

            String text3 =
                    "Die primäre Meldepflicht obliegt den EU-Intermediären, also allen Personen, die " +
                            "eine meldepflichtige grenzüberschreitende Steuergestaltung konzipieren, vermarkten, organisie" +
                            "ren oder zur Umsetzung bereitstellen oder die Umsetzung einer solchen Gestaltung verwalten" +
                            " sowie Personen, die im Hinblick auf diese Tätigkeiten Unterstützung oder Beratung leisten." +
                            " Ist kein EU-Intermediär an der Gestaltung beteiligt oder unterliegt der EU-Intermediär einer " +
                            "Verschwiegenheitsverpflichtung gemäß dem Recht des jeweiligen EU-Mitgliedstaates, ist der " +
                            "in der EU ansässige Steuerpflichtige subsidiär meldepflichtig.";

            Paragraph text3Para = new Paragraph(text3, fontContent);
            document.add(text3Para);

            // 4. section
            Paragraph titleParagraph4 = new Paragraph("\n4.\t Wann muss gemeldet werden?\n", fontSubTitle);
            document.add(titleParagraph4);

            String text4 =
                    "Die DAC6 ist am 25. Juni 2018 in Kraft getreten. Meldungspflichtige Gestaltungen, welche im Zeitraum" +
                            " vom 25. Juni 2018 bis zum 30. Juni 2020 vorgenommen wurden („Altfälle“) sind rückwirkend " +
                            "bis zum 28. Februar 2021 zu melden. Für ab dem 1. Juli 2020 umgesetzte Gestaltungen " +
                            "(„Neufälle“) beginnt eine 30-tätige Mitteilungsfrist am 1. Januar 2021. Einzelne " +
                            "Mitgliedstaaten, wie etwa Deutschland, Österreich und Finnland haben " +
                            "allerdings abweichende nationale Fristenregelungen vorgesehen.";

            Paragraph text4Para = new Paragraph(text4, fontContent);
            document.add(text4Para);

            // ending paragraph
            String endingText =
                    "Als in Liechtenstein domizilierter Intermediär trifft die CorPa Treuhand AG (grundsätzlich) keine " +
                            "Meldepflicht gemäss DAC6. Als unser Kunde sind Sie oder ein Sie beratender " +
                            "EU-Intermediär (vgl. oben, Ziff. 3) hingegen unter Berücksichtigung der lokalen " +
                            "DAC6-Umsetzungsgesetzgebung möglicherweise in Ihrem Wohnsitzstaat der Steuerverwaltung " +
                            "gegenüber meldepflichtig (ein zentrales EU-Meldeportal ist nicht vorgesehen). Wir empfehlen " +
                            "Ihnen deshalb dringendst, für weiterführende Informationen über die DAC6-Bestimmungen " +
                            "sowie die damit verbundenen Meldepflichten Ihren EU-Intermediär zu konsultieren oder " +
                            "einen solchen beizuziehen. Bitte beachten Sie, dass die Missachtung der entsprechenden" +
                            " Meldepflichten mit hohen Bussen geahndet werden kann.  " +
                            "\nDie Geschäftsleitung der CorPa Treuhand AG \n" ;

            Paragraph paraEndingText = new Paragraph(endingText, fontContent);
            document.add(paraEndingText);

            // End of the document

            Paragraph placeAndDate = new Paragraph("\nVaduz, den " + formattedDate, fontContent);
            document.add(placeAndDate);


        } catch (DocumentException e) {
            e.printStackTrace();
        } finally {
            if (document != null) {
                document.close();
            }
        }
    }
}


