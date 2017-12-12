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
import com.verwaltungssoftware.objects.Angebot;
import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class PdfCreator {

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

    public void createDocument(String kunde, String angebot, String datum, String hinweis, int zahlungsziel, int skontoZeit, double skontoBetrag, File f) throws DocumentException, FileNotFoundException, SQLException {
        if (document.isOpen() == false) {
            document = new Document();
        }
        try {
            File file = f;
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(file)); //irrelevant ob \\ oder /
            //PdfWriter writer = PdfCreator.getInstance(document, new FileOutputStream(System.getProperty("user.home") + "/Desktop/blabla.pdf"));
            document.open();

            loadHeaderData(kunde, angebot, datum, hinweis, file);
            loadTableData(angebot, zahlungsziel, skontoZeit, skontoBetrag);

            document.close();
            writer.close();
        } catch (DocumentException | FileNotFoundException | SQLException e2) {
            throw e2;
        }
    }

    public void loadHeaderData(String kundennummer, String angebotsnummer, String datum, String hinweis, File file) throws SQLException, DocumentException, FileNotFoundException {
        try {
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(file));

            document.open();

            //company/Überschrift
            Font chapterFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16);
            PdfPTable company = new PdfPTable(1);
            company.setHorizontalAlignment(Element.ALIGN_CENTER);
            company.setWidthPercentage(100);
            company.setSpacingAfter(11f);
            Chunk chunk1 = new Chunk(user.getCompany(), chapterFont);
            Paragraph paragraph1 = new Paragraph(chunk1);
            PdfPCell companyC1 = new PdfPCell(paragraph1);
            Font infoFont = FontFactory.getFont(FontFactory.HELVETICA, 7);
            PdfPCell companyC2 = new PdfPCell(new Paragraph("Tel. " + user.getaTel() + ",   " + "Fax: " + user.getaFax(), infoFont));
            PdfPCell companyC3 = new PdfPCell(new Paragraph("Geschäftsführer : " + user.getPreName() + " " + user.getLastName() + ",    " + user.getaAmt() + " " + user.getaHrb(), infoFont));

            companyC1.setBorderColor(BaseColor.WHITE);
            companyC2.setBorderColor(BaseColor.WHITE);
            companyC3.setBorderColor(BaseColor.WHITE);
            company.addCell(companyC1);
            company.addCell(companyC2);
            company.addCell(companyC3);
            document.add(company);

            //AdressTable
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
            //Datum richtig formattieren
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd.MM.yyyy");
            LocalDate angebotDatum = null;
            sql.loadDataAngebot();
            for(Angebot a: sql.getDataAngebot()){
                if(a.getAngebotsnummer().equals(angebotsnummer)){
                    angebotDatum = LocalDate.parse(a.getDatum(), dtf);
                }
            }
            pDatum.add(angebotDatum.format(dtf)); //Meldung kann ignoriert werden, da definitiv ein Datum in der oberen Schleife zugewiesen wird
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

            //Hinweis für Kunden
            Paragraph pHinweis = new Paragraph(hinweis, boxInfoFont);
            pHinweis.setAlignment(Element.ALIGN_LEFT);
            pHinweis.setIndentationRight(200); //damit nach gewisser Länge auch ein Zeilenumbruch stattfindet
            pHinweis.setSpacingAfter(11f);
            document.add(pHinweis);

        } catch (SQLException | DocumentException | FileNotFoundException e2) {
            throw e2;
        }
    }

    public void loadTableData(String angebotsnummer, int zahlungsziel, int skontoZeit, double skontoBetrag) throws SQLException, DocumentException {
        try {
            //Haupttabelle
            float[] est = {1.5f, 3, 5, 3, 4, 2, 4};
            PdfPTable table = new PdfPTable(est);
            table.setWidthPercentage(100);
            table.setSpacingAfter(11f);
            table.getDefaultCell().setBackgroundColor(BaseColor.LIGHT_GRAY);
            table.getDefaultCell().setBorderColor(BaseColor.WHITE);
            table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
            table.getDefaultCell().setPadding(5);
            //HeaderSpalten der Haupttabelle
            table.addCell("Pos.");
            table.addCell("Art.Nr.");
            table.addCell("Bezeichnung");
            table.addCell("Menge");
            table.addCell("Einzelpreis");
            table.addCell("%");
            table.addCell("Gesamtpreis");

            table.setHeaderRows(1);
            table.getDefaultCell().setBackgroundColor(BaseColor.WHITE);
            table.getDefaultCell().setBorderColor(BaseColor.WHITE);
            table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
            table.getDefaultCell().setPadding(5);

            //AlternativTabelle
            float[] estAlt = {1.5f, 3, 5, 3, 4, 2, 4};
            PdfPTable tableAlt = new PdfPTable(estAlt);
            tableAlt.setWidthPercentage(100);
            tableAlt.setSpacingBefore(5f);
            tableAlt.setSpacingAfter(11f);
            tableAlt.getDefaultCell().setBackgroundColor(BaseColor.WHITE);
            tableAlt.getDefaultCell().setBorderColor(BaseColor.WHITE);
            tableAlt.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
            tableAlt.getDefaultCell().setPadding(5);
            //

            sql.loadArtikelFromAngebot(angebotsnummer);
            int count = 1; //Nummerierung/Position des Artikels im Angebot/Rechnung
            double verkaufspreis = 0; //Einzelpreis
            double menge = 0;
            double mwstEinzeln = 0;
            double mwstGesamt = 0;
            double gesamtpreis = 0; //Verkaufspreis * Menge pro Artikel/Zeile
            double alternativpreis = 0;
            double endpreisNetto = 0;
            double endpreisBrutto = 0;
            for (Artikel a : sql.getDataArtikelInAngebot()) {
                /*TODO: Rabatt nicht als Dezimalstelle darstellen sondern als ganze
                Zahl (20% etc.) darstellen und auch in der Datenbank so speichern, dann einfach nicht mal Rabattmenge
                sondern durch 100 mal Rabattmenge || genauso bei der Mehrwertsteuer auch noch
                 */
                if (a.getAlternative().equals("0")) {
                    table.addCell("#" + count);
                    table.addCell(a.getArtikelnummer());
                    table.addCell(a.getBezeichnung() + "\n" + a.getZusatztext());
                    table.addCell(a.getMenge());
                    table.addCell(a.getVerkaufspreis());
                    table.addCell(a.getRabattmenge());
                    verkaufspreis = Double.parseDouble(a.getVerkaufspreis());
                    menge = Double.parseDouble(a.getMenge());
                    //wenn kein Rabatt besteht
                    if (a.getRabattmenge() == null) {
                        gesamtpreis = verkaufspreis * menge;
                    } else { //wenn Rabatt besteht
                        gesamtpreis = verkaufspreis * menge * Double.parseDouble(a.getRabattmenge());
                    }
                    table.addCell(String.valueOf(gesamtpreis));
                    count++;
                } else if (a.getAlternative().equals("1")) { //wenn Artikel alternativ ist
                    gesamtpreis = 0;
                    tableAlt.addCell(" ");
                    tableAlt.addCell(a.getArtikelnummer());
                    tableAlt.addCell(a.getBezeichnung() + "\n" + a.getZusatztext());
                    tableAlt.addCell(a.getMenge());
                    tableAlt.addCell(a.getVerkaufspreis());
                    tableAlt.addCell(a.getRabattmenge());
                    verkaufspreis = Double.parseDouble(a.getVerkaufspreis());
                    menge = Double.parseDouble(a.getMenge());
                    if (a.getRabattmenge() == null) {
                        alternativpreis = verkaufspreis * menge;
                    } else { //wenn Rabatt besteht
                        alternativpreis = verkaufspreis * menge * Double.parseDouble(a.getRabattmenge());
                    }
                    tableAlt.addCell(String.valueOf(alternativpreis));
                }
                mwstEinzeln = gesamtpreis * Double.parseDouble(a.getMwst());
                mwstGesamt += mwstEinzeln;
                endpreisNetto += gesamtpreis;
            }
            endpreisBrutto = endpreisNetto + mwstGesamt;

            Font fontAlt = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 8);
            Paragraph pAlt = new Paragraph("Alternativ bieten wir an: ", fontAlt);
            document.add(table);
            document.add(pAlt);
            document.add(tableAlt);

            Font fontEnde = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 8);
            Font fontEndeWert = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10);
            float[] est2 = {2, 2, 2};
            PdfPTable tableEnd = new PdfPTable(est2);
            tableEnd.setWidthPercentage(100);
            tableEnd.setSpacingAfter(5f);
            PdfPCell cNetto = new PdfPCell(new Paragraph("Nettobetrag", fontEnde));
            PdfPCell cMehrwert = new PdfPCell(new Paragraph("Mehrwertsteuer (MwSt. 19%)", fontEnde));
            PdfPCell cBrutto = new PdfPCell(new Paragraph("Bruttobetrag", fontEnde));
            cNetto.setUseVariableBorders(true);
            cNetto.setBorderColorTop(BaseColor.WHITE);
            cNetto.setBorderColorBottom(BaseColor.WHITE);
            cNetto.setBorderColorRight(BaseColor.WHITE);
            cNetto.setBorderColorLeft(BaseColor.WHITE);
            cNetto.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cMehrwert.setUseVariableBorders(true);
            cMehrwert.setBorderColorTop(BaseColor.WHITE);
            cMehrwert.setBorderColorBottom(BaseColor.WHITE);
            cMehrwert.setBorderColorLeft(BaseColor.WHITE);
            cMehrwert.setBorderColorRight(BaseColor.WHITE);
            cMehrwert.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cBrutto.setUseVariableBorders(true);
            cBrutto.setBorderColorTop(BaseColor.WHITE);
            cBrutto.setBorderColorBottom(BaseColor.WHITE);
            cBrutto.setBorderColorLeft(BaseColor.WHITE);
            cBrutto.setBorderColorRight(BaseColor.WHITE);
            cBrutto.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tableEnd.addCell(cNetto);
            tableEnd.addCell(cMehrwert);
            tableEnd.addCell(cBrutto);
            PdfPCell cNettoWert = new PdfPCell(new Paragraph(String.valueOf(endpreisNetto) + " €", fontEndeWert));
            PdfPCell cMehrwertWert = new PdfPCell(new Paragraph(String.valueOf(round(mwstGesamt, 2)) + " €", fontEndeWert));
            PdfPCell cBruttoWert = new PdfPCell(new Paragraph(String.valueOf(endpreisBrutto) + " €", fontEndeWert));
            cNettoWert.setUseVariableBorders(true);
            cNettoWert.setBackgroundColor(BaseColor.LIGHT_GRAY);
            cNettoWert.setBorderWidth(0.5f);
            cNettoWert.setBorderColor(BaseColor.LIGHT_GRAY);
            cNettoWert.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cMehrwertWert.setUseVariableBorders(true);
            cMehrwertWert.setBackgroundColor(BaseColor.LIGHT_GRAY);
            cMehrwertWert.setBorderWidth(0.5f);
            cMehrwertWert.setBorderColor(BaseColor.LIGHT_GRAY);
            cMehrwertWert.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cBruttoWert.setUseVariableBorders(true);
            cBruttoWert.setBackgroundColor(BaseColor.LIGHT_GRAY);
            cBruttoWert.setBorderWidth(0.5f);
            cBruttoWert.setBorderColor(BaseColor.LIGHT_GRAY);
            cBruttoWert.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tableEnd.addCell(cNettoWert);
            tableEnd.addCell(cMehrwertWert);
            tableEnd.addCell(cBruttoWert);

            document.add(tableEnd);
            loadFooterData(12, 7, 2, endpreisBrutto, angebotsnummer);
        } catch (SQLException | DocumentException exc) {
            throw exc;
        }
    }

    public void loadFooterData(int zahlungsziel, int skontoZeit, double skontoBetrag, double endpreisBrutto, String angebotsnummer) throws DocumentException, SQLException {
        try {
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd.MM.yyyy");
            LocalDate angebotDatum;
            LocalDate skontoDatum = null;
            LocalDate zielDatum = null;
            sql.loadDataAngebot();
            for(Angebot a: sql.getDataAngebot()){
                if(a.getAngebotsnummer().equals(angebotsnummer)){
                    angebotDatum = LocalDate.parse(a.getDatum(), dtf);
                    skontoDatum = angebotDatum.plusDays(skontoZeit);
                    zielDatum = angebotDatum.plusDays(zahlungsziel);
                }
            }
            Font payHeadlineFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 9);
            PdfPTable termsOfPayment = new PdfPTable(1);
            termsOfPayment.setHorizontalAlignment(Element.ALIGN_CENTER);
            termsOfPayment.setWidthPercentage(100);
            termsOfPayment.setSpacingAfter(5f);
            Chunk chunk1 = new Chunk("Zahlungsbedingungen: ", payHeadlineFont);
            Paragraph paragraph1 = new Paragraph(chunk1);
            PdfPCell payC1 = new PdfPCell(paragraph1);
            Font payFont = FontFactory.getFont(FontFactory.HELVETICA, 7);
            PdfPCell payC2 = new PdfPCell(new Paragraph("Zahlungsziel " + zahlungsziel + " Netto, " + skontoZeit + " Tage " + skontoBetrag + "% Skonto.", payFont));
            PdfPCell payC3 = new PdfPCell(new Paragraph("Die Ware bleibt unser Eigentum bis zur vollständigen Bezahlung.\nDienstleistungen sind nicht skontierfähig.", payFont));
            PdfPCell payC4 = new PdfPCell(new Paragraph("entspricht:            " + round(endpreisBrutto*(1-(skontoBetrag/100)), 2) + " € bis: " + skontoDatum.format(dtf) + ",         " + endpreisBrutto + " € bis: " + zielDatum.format(dtf), payFont));

            payC1.setBorderColor(BaseColor.WHITE);
            payC2.setBorderColor(BaseColor.WHITE);
            payC3.setBorderColor(BaseColor.WHITE);
            payC4.setBorderColor(BaseColor.WHITE);
            termsOfPayment.addCell(payC1);
            termsOfPayment.addCell(payC2);
            termsOfPayment.addCell(payC3);
            termsOfPayment.addCell(payC4);
            document.add(termsOfPayment);
            
            Font zahlungInfoFont = FontFactory.getFont(FontFactory.HELVETICA, 8);
            Paragraph pHinweis = new Paragraph("Wir halten den Preis wenn nicht anders vereinbart 4 Kalenderwochen aufrecht, danach bitte erneut anfragen.", zahlungInfoFont);
            pHinweis.setAlignment(Element.ALIGN_LEFT);
            pHinweis.setSpacingAfter(5f);
            document.add(pHinweis);
            
        } catch (SQLException | DocumentException exc) {
            throw exc;
        }
    }

    private double round(double value, int places) {
        try {
            if (places < 0) {
                throw new IllegalArgumentException();
            }
        } catch (IllegalArgumentException iae) {
            System.out.println(iae.getMessage());
        }
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}
