package com.bpkad;


import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRResultSetDataSource;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;

public class LaporanPerda1_3 extends LaporanBase{

    public LaporanPerda1_3() {
        super("sql/pertanggungjawaban/perda1.3.sql", "reports/pertanggungjawaban/rpt_perda_1.3.jrxml");
    }

    public void export(ResultSet rs, String path) throws JRException {
        
        System.out.println("filling parameters to .JASPER file....");  
        JasperPrint jprint = (JasperPrint) JasperFillManager.fillReport(jasperReport, props, new JRResultSetDataSource(rs));
        System.out.println("exporting the JASPER file to PDF file....");
        JasperExportManager.exportReportToPdfFile(jprint, path);
        System.out.println("Successfully completed the export");
    }

    public void exportPerSkpd(String folder) throws JRException, SQLException {
        ResultSet rs = db.executeQuery("SELECT top 2 kd_urusan, kd_bidang, kd_unit, kd_sub, nm_sub_unit FROM ref_sub_unit");
        ResultSet rsRpt = null;
        PreparedStatement stmt = db.prepareStatement(this.sql);
        String fileName;

        if(rs!= null) {
            while(rs.next()) {
                stmt.setByte(1, rs.getByte(1));
                stmt.setByte(2, rs.getByte(2));
                stmt.setByte(3, rs.getByte(3));
                stmt.setShort(4, rs.getShort(4));
                
                fileName = String.format("%d.%d.%02d.%02d %s.pdf", rs.getByte(1), rs.getByte(2), rs.getByte(3), rs.getShort(4), rs.getString(5));
                rsRpt = stmt.executeQuery();

                export(rsRpt, folder + "/" + fileName);
            }
            rs.close();
        }
    }
    
}
