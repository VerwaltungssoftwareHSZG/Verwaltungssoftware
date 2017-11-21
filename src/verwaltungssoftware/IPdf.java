/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package verwaltungssoftware;

import com.itextpdf.text.DocumentException;
import java.io.File;
import java.io.FileNotFoundException;
import java.sql.SQLException;

public interface IPdf {

    public void createDocument(String kunde, String angebot, File f) throws DocumentException, FileNotFoundException, SQLException;

    public void loadHeaderData(String kundennummer, File file) throws DocumentException, FileNotFoundException;
    
    public void loadTableData(String angebotsnummer) throws SQLException, DocumentException;
    
    public void loadFooterData();

}
