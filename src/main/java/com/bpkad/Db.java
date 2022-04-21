package com.bpkad;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class Db {
    private static final Db db = new Db();
    public static Db getInstance() {
        return db;
    }
    
    private final String connectionStringTemplate = "jdbc:jtds:sqlserver://%s/%s;user=%s;password=%s;"; //" jdbc:sqlserver://%s;username=%s;password=%s;databaseName=%s;encrypt=false;";
    private Connection con = null;
    private Db() {
        
    }    

    Connection getConnection() {
        if(con == null) {
            Properties props = new Properties();
            try {
                props.load(Db.class.getClassLoader().getResourceAsStream("db.properties"));
           
                String connectionString = String.format(connectionStringTemplate, props.get("server"), props.get("db"), props.get("user"), props.get("password"));
                System.out.println(connectionString);
                con = DriverManager.getConnection(connectionString);
            } catch (IOException | SQLException e) {
                e.printStackTrace();
            }
        }
        return con;
    }

    ResultSet executeQuery(String sql) {
        try  {
            Statement stmt = getConnection().createStatement();
            return stmt.executeQuery(sql);
        } catch (SQLException e) {            
            e.printStackTrace();
        }    
        
        return null;
    }

    PreparedStatement prepareStatement(String sql) {
        PreparedStatement stmt = null;
        try { 
            stmt = getConnection().prepareStatement(sql);            
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return stmt;
    }
}
