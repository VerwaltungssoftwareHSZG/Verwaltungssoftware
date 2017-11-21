package verwaltungssoftware;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import java.io.FileOutputStream;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.sql.SQLException;

public class PdfCreator implements IPdf{

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
    public void createDocument(String kunde, String angebot, File f) throws DocumentException, FileNotFoundException, SQLException{
        try {
            File file = f;
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(file)); //irrelevant ob \\ oder /
            //PdfWriter writer = PdfCreator.getInstance(document, new FileOutputStream(System.getProperty("user.home") + "/Desktop/blabla.pdf"));
            document.open();

            loadHeaderData(kunde, file);
            loadTableData(angebot);

            document.close();
            writer.close();
        } catch (DocumentException | FileNotFoundException | SQLException e2) {
            throw e2;
        }
    }

    @Override
    public void loadHeaderData(String kundennummer, File file) throws DocumentException, FileNotFoundException{
        try {
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(file));

            document.open();

            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA, 6);
            PdfPTable adresse = new PdfPTable(1);
            adresse.setHorizontalAlignment(Element.ALIGN_LEFT);
            adresse.setWidthPercentage(40);
            Chunk d = new Chunk(user.getCompany() + " · " + user.getaStreet() + " · " + user.getaPlz() + " " + user.getaOrt());
            d.setUnderline(0.1f, -2f);
            d.setFont(titleFont);
            Paragraph p = new Paragraph(d);
            PdfPCell absenderAdresse = new PdfPCell(new Paragraph(p));

            absenderAdresse.setBorderColor(BaseColor.WHITE);
            absenderAdresse.setHorizontalAlignment(Element.ALIGN_LEFT);
            adresse.addCell(absenderAdresse);
            Font adresseFont = FontFactory.getFont(FontFactory.HELVETICA, 9);
            PdfPCell kundeAdresseName;
            PdfPCell kundeAdresseStrasse;
            PdfPCell kundeAdressePlz;
            //suche nach dem Kunden für Adresszeile
            for (Kunde k : gui.getDataKunde()) {
                if (k.getKundennummer().equals(kundennummer)) {
                    kundeAdresseName = new PdfPCell(new Paragraph(k.getVorname() + " " + k.getName(), adresseFont));
                    kundeAdresseStrasse = new PdfPCell(new Paragraph(k.getStraße() + " " + k.getHausnummer(), adresseFont));
                    kundeAdressePlz = new PdfPCell(new Paragraph(k.getPlz() + " " + k.getOrt(), adresseFont));
                    kundeAdresseName.setBorderColor(BaseColor.WHITE);
                    kundeAdresseStrasse.setBorderColor(BaseColor.WHITE);
                    kundeAdressePlz.setBorderColor(BaseColor.WHITE);
                    adresse.addCell(kundeAdresseName);
                    adresse.addCell(kundeAdresseStrasse);
                    adresse.addCell(kundeAdressePlz);
                }
            }
            document.add(adresse);

        } catch (DocumentException | FileNotFoundException e2) {
            throw e2;
        }
    }

    @Override
    public void loadTableData(String angebotsnummer) throws SQLException, DocumentException{
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
            for (Artikel a : gui.getDataArtikelInAngebot()) {
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
    
    public void loadFooterData(){
        
    }
}
