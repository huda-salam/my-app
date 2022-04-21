package com.bpkad;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JasperCompileManager;

import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;

public abstract class LaporanBase {
    protected Db db;
    protected String sql;
    protected JasperReport jasperReport;
    protected Map<String, Object> props = new HashMap<String, Object>();

    private String sqlFile, reportFile;

    protected LaporanBase(String sqlFile, String reportFile) {
        this.sqlFile = sqlFile;
        this.reportFile = reportFile;
        this.db = Db.getInstance();
    }   
    
    public void initialize() {
        InputStream stream = null;
        ClassLoader loader = LaporanBase.class.getClassLoader();

        try {
            stream = loader.getResourceAsStream(sqlFile);
            this.sql = new String(stream.readAllBytes());        
            stream.close();
            
            stream = loader.getResourceAsStream(reportFile);        
            System.out.println("Load .JRMXML file....");
            JasperDesign jasperDesign = JRXmlLoader.load(stream);
            stream.close();

            System.out.println("Compiling the .JRMXML file to .JASPER file....");
            this.jasperReport = JasperCompileManager.compileReport(jasperDesign); 
        }
        catch(IOException | JRException ex) {
            ex.printStackTrace();
        }
        
        this.props.put(JRParameter.REPORT_LOCALE, new Locale("id", "ID")); 
    }
}
