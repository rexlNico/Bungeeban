package de.rexlNico.Ban.Api;

import java.sql.*;

public class MySQL
{
    private final String HOST;
    private final String DATABASE;
    private final String USER;
    private final String PASSWORD;
    private Connection con;
    
    public MySQL(final String host, final String database, final String user, final String password) {
        this.HOST = host;
        this.DATABASE = database;
        this.USER = user;
        this.PASSWORD = password;
        this.connect();
    }
    
    public void connect() {
        try {
            this.con = DriverManager.getConnection("jdbc:mysql://" + this.HOST + ":3306/" + this.DATABASE + "?autoReconnect=true", this.USER, this.PASSWORD);
            System.out.println("[MySQL] Die Verbindung zur MySQL wurde hergestellt!");
        }
        catch (SQLException e) {
            System.out.println("[MySQL] Die Verbindung zur MySQL ist fehlgeschlagen! Fehler: " + e.getMessage());
        }
    }
    
    public void close() {
        try {
            if (this.con != null) {
                this.con.close();
                System.out.println("[MySQL] Die Verbindung zur MySQL wurde Erfolgreich beendet!");
            }
        }
        catch (SQLException e) {
            System.out.println("[MySQL] Fehler beim beenden der Verbindung zur MySQL! Fehler: " + e.getMessage());
        }
    }
    
    public void update(final String qry) {
        Statement st = null;
        try {
            st = this.con.createStatement();
            st.executeUpdate(qry);
        }
        catch (Exception e) {
            this.connect();
            System.err.println(e);
        }
        closeStatement(st);
    }
    
    public ResultSet query(final String qry) {
        ResultSet rs = null;
        try {
            final Statement st = this.con.createStatement();
            rs = st.executeQuery(qry);
        }
        catch (SQLException e) {
            this.connect();
            System.err.println(e);
        }
        return rs;
    }
    
    public static void closeStatement(final Statement st) {
        if (st != null) {
            try {
                st.close();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    public static void closeResultset(final ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
