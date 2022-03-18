package com.bpkad;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Locale;
import java.util.Map;
import java.util.HashMap;

import javax.swing.JFrame;

import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JRResultSetDataSource;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.swing.JRViewer;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        String connectionString = "jdbc:sqlserver://localhost;username=sa;password=123456;databaseName=tu_2021;encrypt=false;";
        ResultSet  rs = null; //, rs2 = null;
        try {
            Connection con  = DriverManager.getConnection(connectionString);
            Statement  stmt = con.createStatement();
            String     sql  = Queries.getQuery("sql_perda_1.1");// Files.readString("/Sql/ref_sub_unit.sql"); // "SELECT * FROM Ref_Sub_Unit ";
            //System.out.println(sql);
            rs   = stmt.executeQuery(sql);

            /*
            if(rs!=null) {
                while(rs.next()) {
                    System.out.println(rs.getString("Kd_Unit_Gab"));
                }
            } */

            Clipboard clipBoard = Toolkit.getDefaultToolkit().getSystemClipboard();
            StringSelection sel = new StringSelection(sql);
            clipBoard.setContents(sel, null);
            
            //rs2  = stmt.executeQuery(Queries.getQuery("sql_perda_1.2"));
            
        }
        catch (Exception ex) {
            System.out.println(ex);
        }
        
        String currentDir = Paths.get("").toAbsolutePath().toString();
        String fileNameJrxml = currentDir + "/Reports/rptLRA_Perda1.1.jrxml";
        String fileNamePdf = currentDir + "/Reports/tes.pdf";
 
        try {
            System.out.println("Loading the .JRMXML file ....");
            JasperDesign jasperDesign = JRXmlLoader.load(fileNameJrxml);
            System.out.println("Compiling the .JRMXML file to .JASPER file....");
            JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
            
            System.out.println("filling parameters to .JASPER file....");
            Map<String, Object> props = new HashMap<String, Object>();
            props.put(JRParameter.REPORT_LOCALE, new Locale("id", "ID")); 

            JasperPrint jprint = (JasperPrint) JasperFillManager.fillReport(jasperReport, null, new JRResultSetDataSource(rs));
            System.out.println("exporting the JASPER file to PDF file....");
            JasperExportManager.exportReportToPdfFile(jprint, fileNamePdf);
            System.out.println("Successfully completed the export");

            JFrame frame = new JFrame("Tes");
            JRViewer viewer = new JRViewer(jprint);

            frame.add(viewer);
            //frame.setSize(800, 600);
            frame.setExtendedState(frame.getExtendedState() | JFrame.MAXIMIZED_BOTH);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);
            System.console().readLine();

        } catch (Exception e) {
            System.out.print("Exception:" + e);
        }

        System.out.println( "He lorld!!!!" );        
    }
}
