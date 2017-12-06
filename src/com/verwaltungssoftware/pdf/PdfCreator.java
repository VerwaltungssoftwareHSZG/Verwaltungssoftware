package com.verwaltungssoftware.pdf;

import com.verwaltungssoftware.database.SqlConnector;
import com.verwaltungssoftware.objects.User;
import com.verwaltungssoftware.objects.Kunde;
import com.verwaltungssoftware.objects.Artikel;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import java.io.FileOutputStream;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.VerticalPositionMark;
import com.verwaltungssoftware.main.Gui;
import java.io.File;
import java.io.FileNotFoundException;
import java.sql.SQLException;

public class PdfCreator implements IPdf {

    private Document document;
    private User user;
    private SqlConnector sql;
    private Gui gui;

    public PdfCreator(User u, Gui g, SqlConnector s) {
        document = new Document();
        user = u;
        sql = s;
        gui = g;
    }

    @Override
    public void createDocument(String kunde, String angebot, String datum, File f) throws DocumentException, FileNotFoundException, SQLException {
        if(document.isOpen() == false){
            document = new Document();
        }
        try {
            File file = f;
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(file)); //irrelevant ob \\ oder /
            //PdfWriter writer = PdfCreator.getInstance(document, new FileOutputStream(System.getProperty("user.home") + "/Desktop/blabla.pdf"));
            document.open();

            loadHeaderData(kunde, angebot, datum, file);
            loadTableData(angebot);

            document.close();
            writer.close();
        } catch (DocumentException | FileNotFoundException | SQLException e2) {
            throw e2;
        }
    }

    @Override
    public void loadHeaderData(String kundennummer, String angebotsnummer, String datum, File file) throws DocumentException, FileNotFoundException {
        try {
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(file));

            document.open();

            Font chapterFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16);
            PdfPTable company = new PdfPTable(1);
            company.setHorizontalAlignment(Element.ALIGN_CENTER);
            company.setWidthPercentage(100);
            Chunk chunk1 = new Chunk(user.getCompany(), chapterFont);
            Paragraph paragraph1 = new Paragraph(chunk1);
            PdfPCell companyC1 = new PdfPCell(paragraph1);
            Font infoFont = FontFactory.getFont(FontFactory.HELVETICA, 7);
            PdfPCell companyC2 = new PdfPCell(new Paragraph("Tel. " + user.getaTel() + ",   " + "Fax: " + user.getaFax(), infoFont));
            PdfPCell companyC3 = new PdfPCell(new Paragraph("Geschäftsführer : " + user.getPreName() + " " + user.getLastName() + ",    " + user.getaAmt() + " " + user.getaHrb(), infoFont));
            PdfPCell fill1 = new PdfPCell(new Paragraph(" "));

            companyC1.setBorderColor(BaseColor.WHITE);
            companyC2.setBorderColor(BaseColor.WHITE);
            companyC3.setBorderColor(BaseColor.WHITE);
            fill1.setBorderColor(BaseColor.WHITE);
            company.addCell(companyC1);
            company.addCell(companyC2);
            company.addCell(companyC3);
            company.addCell(fill1);
            document.add(company);

            //AdresssTable
            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA, 6);
            PdfPTable adresseTable = new PdfPTable(1);
            adresseTable.setHorizontalAlignment(Element.ALIGN_LEFT);
            adresseTable.setWidthPercentage(40);
            Chunk d = new Chunk(user.getCompany() + " · " + user.getaStreet() + " · " + user.getaPlz() + " " + user.getaOrt());
            d.setUnderline(0.1f, -2f);
            d.setFont(titleFont);
            Paragraph p = new Paragraph(d);
            PdfPCell absenderAdresse = new PdfPCell(p);
            absenderAdresse.setBorderColor(BaseColor.WHITE);
            absenderAdresse.setHorizontalAlignment(Element.ALIGN_LEFT);
            adresseTable.addCell(absenderAdresse);
            Font adresseFont = FontFactory.getFont(FontFactory.HELVETICA, 9);
            PdfPCell kundeAdresseName;
            PdfPCell kundeAdresseStrasse;
            PdfPCell kundeAdressePlz;

            //suche nach dem Kunden für Adresszeile
            for (Kunde k : sql.getDataKunde()) {
                if (k.getKundennummer().equals(kundennummer)) {
                    kundeAdresseName = new PdfPCell(new Paragraph(k.getVorname() + " " + k.getName(), adresseFont));
                    kundeAdresseStrasse = new PdfPCell(new Paragraph(k.getStraße() + " " + k.getHausnummer(), adresseFont));
                    kundeAdressePlz = new PdfPCell(new Paragraph(k.getPlz() + " " + k.getOrt(), adresseFont));
                    kundeAdresseName.setBorderColor(BaseColor.WHITE);
                    kundeAdresseStrasse.setBorderColor(BaseColor.WHITE);
                    kundeAdressePlz.setBorderColor(BaseColor.WHITE);
                    adresseTable.addCell(kundeAdresseName);
                    adresseTable.addCell(kundeAdresseStrasse);
                    adresseTable.addCell(kundeAdressePlz);
                }
            }

            PdfPCell filler3 = new PdfPCell(new Paragraph(" "));
            filler3.setBorderColor(BaseColor.WHITE);
            //adresseTable.addCell(filler3);

            //AngebotTable
            Font angebotFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10);
            Font boxInfoFont = FontFactory.getFont(FontFactory.HELVETICA, 8);
            PdfPTable angebotTable = new PdfPTable(1);
            PdfPCell cA = new PdfPCell(new Paragraph("Angebot", angebotFont));
            cA.setHorizontalAlignment(Element.ALIGN_CENTER);
            cA.setUseVariableBorders(true);
            angebotTable.addCell(cA);
            
            PdfPCell cANummer = new PdfPCell();
            Chunk chAnummer = new Chunk(new VerticalPositionMark());
            Phrase pAnummer = new Phrase();
            pAnummer.setFont(boxInfoFont);
            pAnummer.add("Angebotsnummer:");
            pAnummer.add(chAnummer);
            pAnummer.add(angebotsnummer);
            cANummer.setPhrase(pAnummer);
            cANummer.setUseVariableBorders(true);
            cANummer.setBorderColorBottom(BaseColor.WHITE);
            angebotTable.addCell(cANummer);
            
            PdfPCell cADatum = new PdfPCell();
            Chunk chDatum = new Chunk(new VerticalPositionMark());
            Phrase pDatum = new Phrase();
            pDatum.setFont(boxInfoFont);
            pDatum.add("Datum:");
            pDatum.add(chDatum);
            pDatum.add(datum);
            cADatum.setPhrase(pDatum);
            cADatum.setUseVariableBorders(true);
            cADatum.setBorderColorTop(BaseColor.WHITE);
            cADatum.setBorderColorBottom(BaseColor.WHITE);
            angebotTable.addCell(cADatum);
            
            PdfPCell cAKunde = new PdfPCell();
            Chunk chKunde = new Chunk(new VerticalPositionMark());
            Phrase pKunde = new Phrase();
            pKunde.setFont(boxInfoFont);
            pKunde.add("Ihre Kundennummer:");
            pKunde.add(chKunde);
            pKunde.add(kundennummer);
            cAKunde.setPhrase(pKunde);
            cAKunde.setUseVariableBorders(true);
            cAKunde.setBorderColorTop(BaseColor.WHITE);
            angebotTable.addCell(cAKunde);

            //outerTable
            float[] outerTableWidth = {1, 1};
            PdfPTable outerTable = new PdfPTable(outerTableWidth);
            outerTable.setWidthPercentage(100);
            outerTable.getDefaultCell().setBorderColor(BaseColor.WHITE);
            outerTable.setSpacingAfter(11f);
            outerTable.addCell(adresseTable);
            outerTable.addCell(angebotTable);

            document.add(outerTable);

        } catch (DocumentException | FileNotFoundException e2) {
            throw e2;
        }
    }

    @Override
    public void loadTableData(String angebotsnummer) throws SQLException, DocumentException {
        try {
            float[] est = {1.5f, 3, 5, 3, 4, 2, 4};
            PdfPTable table = new PdfPTable(est);
            table.setWidthPercentage(100);
            //table.setSpacingBefore(11f);
            //table.setSpacingAfter(11f);

            /*dfPCell c0 = new PdfPCell(new Phrase("blubl"));
                c0.setColspan(7);
                c0.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(c0);*/
            table.getDefaultCell().setBackgroundColor(BaseColor.LIGHT_GRAY);
            table.getDefaultCell().setBorderColor(BaseColor.WHITE);
            table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
            table.getDefaultCell().setPadding(5);
            for (int i = 0; i < 1; i++) {
                table.addCell("Pos.");
                table.addCell("Art.Nr.");
                table.addCell("Bezeichnung");
                table.addCell("Menge");
                table.addCell("Einzelpreis");
                table.addCell("%");
                table.addCell("Gesamtpreis");
            }
            table.setHeaderRows(1);
            table.getDefaultCell().setBackgroundColor(BaseColor.WHITE);
            table.getDefaultCell().setBorderColor(BaseColor.WHITE);
            table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
            table.getDefaultCell().setPadding(5);
            sql.loadArtikelFromAngebot(angebotsnummer);
            int count = 1;
            double verkaufspreis;
            double menge;
            double gesamtpreis;
            double endpreis = 0;
            for (Artikel a : sql.getDataArtikelInAngebot()) {
                table.addCell("#" + count);
                table.addCell(a.getArtikelnummer());
                table.addCell(a.getBezeichnung() + "\n" + a.getZusatztext());
                table.addCell(a.getMenge());
                table.addCell(a.getVerkaufspreis());
                table.addCell(a.getRabatt());
                verkaufspreis = Double.parseDouble(a.getVerkaufspreis());
                menge = Double.parseDouble(a.getMenge());
                gesamtpreis = verkaufspreis * menge;
                endpreis += gesamtpreis;
                table.addCell(String.valueOf(gesamtpreis));
                count++;
            }

            //Paragraph test = new Paragraph(String.valueOf(endpreis));
            //document.add(test);
            document.add(table);
        } catch (SQLException | DocumentException exc) {
            throw exc;
        }
    }

    @Override
    public void loadFooterData() {

    }
}
