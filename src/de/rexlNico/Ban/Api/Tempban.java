package de.rexlNico.Ban.Api;

import java.sql.*;
import java.text.*;
import java.util.*;
import java.util.Date;

import de.rexlNico.Ban.Main.main;

public class Tempban
{
    public static boolean playerExists(final String uuid) {
        ResultSet rs = null;
        try {
            rs = main.mysql.query("SELECT * FROM Tempban WHERE UUID= '" + uuid + "'");
            if (rs.next()) {
                final String s = rs.getString("UUID");
                MySQL.closeResultset(rs);
                return s != null;
            }
            return false;
        }
        catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public static void unban(final String Spielername, final Integer banned) {
        main.mysql.update("UPDATE Tempban SET BANNED= '" + banned + "' WHERE UUID= '" + Spielername + "';");
        main.mysql.update("DELETE FROM HistoryBan WHERE UUID= '" + Spielername + "'");
    }
    
    public static Integer getTempBanned(final String name) {
        Integer i = 0;
        ResultSet rs = null;
        try {
            if (!playerExists(name)) {
                MySQL.closeResultset(rs);
                return 2;
            }
            try {
                rs = main.mysql.query("SELECT * FROM Tempban WHERE UUID= '" + name + "'");
                if (!rs.next() || Integer.valueOf(rs.getInt("BANNED")) == null) {
                    i = rs.getInt("BANNED");
                }
            }
            catch (SQLException e) {
                e.printStackTrace();
            }
        }
        catch (Exception e2) {
            e2.printStackTrace();
            main.mysql.connect();
        }
        try {
            i = rs.getInt("BANNED");
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        MySQL.closeResultset(rs);
        return i;
    }
    
    public static Long getEnd2(final String Spielername) {
        ResultSet rs = null;
        if (playerExists(Spielername)) {
            rs = main.mysql.query("SELECT * FROM HistoryBan WHERE UUID= '" + Spielername + "'");
            try {
                if (rs.next()) {
                    final long i = rs.getLong("Length");
                    MySQL.closeResultset(rs);
                    return i;
                }
            }
            catch (SQLException ex) {}
        }
        return null;
    }
    
    public static Long getEnd(final String Spielername) {
        ResultSet rs = null;
        if (playerExists(Spielername)) {
            rs = main.mysql.query("SELECT * FROM Tempban WHERE UUID= '" + Spielername + "'");
            try {
                if (rs.next()) {
                    final long i = rs.getLong("End");
                    MySQL.closeResultset(rs);
                    return i;
                }
            }
            catch (SQLException ex) {}
        }
        return null;
    }
    
    public static String getReason(final String Spielername) {
        ResultSet rs = null;
        if (playerExists(Spielername)) {
            rs = main.mysql.query("SELECT * FROM Tempban WHERE UUID= '" + Spielername + "'");
            try {
                if (rs.next()) {
                    final String t = rs.getString("Reason");
                    MySQL.closeResultset(rs);
                    return t;
                }
            }
            catch (SQLException ex) {}
        }
        return null;
    }
    
    public static Integer getPoints(final String name) {
        Integer i = 0;
        ResultSet rs = null;
        try {
            if (!playerExists(name)) {
                MySQL.closeResultset(rs);
                return 0;
            }
            try {
                rs = main.mysql.query("SELECT * FROM Tempban WHERE UUID= '" + name + "'");
                if (!rs.next() || Integer.valueOf(rs.getInt("POINTS")) == null) {
                    i = rs.getInt("POINTS");
                }
            }
            catch (SQLException e) {
                e.printStackTrace();
            }
        }
        catch (Exception e2) {
            e2.printStackTrace();
            main.mysql.connect();
        }
        try {
            i = rs.getInt("POINTS");
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        MySQL.closeResultset(rs);
        return i;
    }
    
    public static void setTempban(final String Spielername, final String Grund, final long length, final String ip, final Integer points) {
        final long c = System.currentTimeMillis();
        final long millis = length * 1000L;
        final long ende = c + millis;
        final Date date = Calendar.getInstance().getTime();
        final SimpleDateFormat dateFormatter = new SimpleDateFormat("dd.MM.yyyy");
        final String dateString = dateFormatter.format(date);
        final SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        final String uhrzeit = sdf.format(new Date());
        main.mysql.update("INSERT INTO HistoryBan(UUID,Datum,Uhrzeit,Grund,Length) VALUES ('" + Spielername + "','" + dateString + "', '" + uhrzeit + "','" + Grund + "','" + length + "');");
        main.mysql.update("INSERT INTO Tempban(UUID,End,Reason,VON,POINTS,BANNED) VALUES ('" + Spielername + "','" + ende + "','" + Grund.toUpperCase() + "','" + ip + "','" + points + "', '1')");
    }
    
    public static void setTempban2(final String Spielername, final String Grund, final long length, final String ip, final Integer points, final Integer banned) {
        final long c = System.currentTimeMillis();
        final long millis = length * 1000L;
        final long ende = c + millis;
        final Date date = Calendar.getInstance().getTime();
        final SimpleDateFormat dateFormatter = new SimpleDateFormat("dd.MM.yyyy");
        final String dateString = dateFormatter.format(date);
        final SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        final String uhrzeit = sdf.format(new Date());
        main.mysql.update("INSERT INTO HistoryBan(UUID,Datum,Uhrzeit,Grund,Length) VALUES ('" + Spielername + "','" + dateString + "', '" + uhrzeit + "','" + Grund + "','" + length + "');");
        main.mysql.update("UPDATE Tempban SET UUID= '" + Spielername + "' WHERE UUID= '" + Spielername + "';");
        main.mysql.update("UPDATE Tempban SET POINTS= '" + points + "' WHERE UUID= '" + Spielername + "';");
        main.mysql.update("UPDATE Tempban SET BANNED= '" + banned + "' WHERE UUID= '" + Spielername + "';");
        main.mysql.update("UPDATE Tempban SET IP= '" + ip + "' WHERE UUID= '" + Spielername + "';");
        main.mysql.update("UPDATE Tempban SET Reason= '" + Grund + "' WHERE UUID= '" + Spielername + "';");
        main.mysql.update("UPDATE Tempban SET End= '" + ende + "' WHERE UUID= '" + Spielername + "';");
    }
    
    public static String getTime(final String uuid) {
        if (getEnd2(uuid) == -1L) {
            return "PERMANENT";
        }
        final long c = System.currentTimeMillis();
        final long ende = getEnd(uuid);
        long diff = ende - c;
        int sekunden = 0;
        int minuten = 0;
        int stunden = 0;
        int tage = 0;
        while (diff > 1000L) {
            diff -= 1000L;
            ++sekunden;
        }
        while (sekunden > 60) {
            sekunden -= 60;
            ++minuten;
        }
        while (minuten > 60) {
            minuten -= 60;
            ++stunden;
        }
        while (stunden > 24) {
            stunden -= 24;
            ++tage;
        }
        if (tage <= 0 & stunden <= 0 & minuten <= 0 & sekunden <= 0) {
            unban(uuid, 2);
            return null;
        }
        if (tage == 0 & stunden == 0 & minuten == 0 & sekunden == 1) {
            return String.valueOf(sekunden) + " Sekunde";
        }
        if (tage == 0 & stunden == 0 & minuten == 0 & sekunden != 1) {
            return String.valueOf(sekunden) + " Sekunden";
        }
        if (tage == 0 & stunden == 0 & minuten == 1 & sekunden != 1) {
            return String.valueOf(minuten) + " Minute " + sekunden + " Sekunden";
        }
        if (tage == 0 & stunden == 0 & minuten == 1 & sekunden == 1) {
            return String.valueOf(minuten) + " Minute " + sekunden + " Sekunde";
        }
        if (tage == 0 & stunden == 0 & minuten != 1 & sekunden != 1) {
            return String.valueOf(minuten) + " Minuten " + sekunden + " Sekunden";
        }
        if (tage == 0 & stunden == 0 & minuten != 1 & sekunden == 1) {
            return String.valueOf(minuten) + " Minuten " + sekunden + " Sekunde";
        }
        if (tage == 0 & stunden == 1 & minuten != 1 & sekunden != 1) {
            return String.valueOf(stunden) + " Stunde " + minuten + " Minuten " + sekunden + " Sekunden";
        }
        if (tage == 0 & stunden == 1 & minuten != 1 & sekunden == 1) {
            return String.valueOf(stunden) + " Stunde " + minuten + " Minuten " + sekunden + " Sekunde";
        }
        if (tage == 0 & stunden == 1 & minuten == 1 & sekunden != 1) {
            return String.valueOf(stunden) + " Stunde " + minuten + " Minute " + sekunden + " Sekunden";
        }
        if (tage == 0 & stunden == 1 & minuten == 1 & sekunden == 1) {
            return String.valueOf(stunden) + " Stunde " + minuten + " Minute " + sekunden + " Sekunde";
        }
        if (tage == 0 & stunden != 1 & minuten != 1 & sekunden != 1) {
            return String.valueOf(stunden) + " Stunden " + minuten + " Minuten " + sekunden + " Sekunden";
        }
        if (tage == 0 & stunden != 1 & minuten != 1 & sekunden == 1) {
            return String.valueOf(stunden) + " Stunden " + minuten + " Minuten " + sekunden + " Sekunde";
        }
        if (tage == 0 & stunden != 1 & minuten == 1 & sekunden != 1) {
            return String.valueOf(stunden) + " Stunden " + minuten + " Minute " + sekunden + " Sekunden";
        }
        if (tage == 0 & stunden != 1 & minuten == 1 & sekunden == 1) {
            return String.valueOf(stunden) + " Stunden " + minuten + " Minute " + sekunden + " Sekunde";
        }
        if (tage == 1 & stunden != 1 & minuten != 1 & sekunden != 1) {
            return String.valueOf(tage) + " Tag " + stunden + " Stunden " + minuten + " Minuten " + sekunden + " Sekunden";
        }
        if (tage == 1 & stunden != 1 & minuten != 1 & sekunden == 1) {
            return String.valueOf(tage) + " Tag " + stunden + " Stunden " + minuten + " Minuten " + sekunden + " Sekunde";
        }
        if (tage == 1 & stunden != 1 & minuten == 1 & sekunden != 1) {
            return String.valueOf(tage) + " Tag " + stunden + " Stunden " + minuten + " Minute " + sekunden + " Sekunden";
        }
        if (tage == 1 & stunden != 1 & minuten == 1 & sekunden == 1) {
            return String.valueOf(tage) + " Tag " + stunden + " Stunden " + minuten + " Minute " + sekunden + " Sekunde";
        }
        if (tage == 1 & stunden == 1 & minuten != 1 & sekunden != 1) {
            return String.valueOf(tage) + " Tag " + stunden + " Stunde " + minuten + " Minuten " + sekunden + " Sekunden";
        }
        if (tage == 1 & stunden == 1 & minuten != 1 & sekunden == 1) {
            return String.valueOf(tage) + " Tag " + stunden + " Stunde " + minuten + " Minuten " + sekunden + " Sekunde";
        }
        if (tage == 1 & stunden == 1 & minuten == 1 & sekunden != 1) {
            return String.valueOf(tage) + " Tag " + stunden + " Stunde " + minuten + " Minute " + sekunden + " Sekunden";
        }
        if (tage == 1 & stunden == 1 & minuten == 1 & sekunden == 1) {
            return String.valueOf(tage) + " Tag " + stunden + " Stunde " + minuten + " Minute " + sekunden + " Sekunde";
        }
        if (tage != 1 & stunden != 1 & minuten != 1 & sekunden != 1) {
            return String.valueOf(tage) + " Tage " + stunden + " Stunden " + minuten + " Minuten " + sekunden + " Sekunden";
        }
        if (tage != 1 & stunden != 1 & minuten != 1 & sekunden == 1) {
            return String.valueOf(tage) + " Tage " + stunden + " Stunden " + minuten + " Minuten " + sekunden + " Sekunde";
        }
        if (tage != 1 & stunden != 1 & minuten == 1 & sekunden != 1) {
            return String.valueOf(tage) + " Tage " + stunden + " Stunden " + minuten + " Minute " + sekunden + " Sekunden";
        }
        if (tage != 1 & stunden != 1 & minuten == 1 & sekunden == 1) {
            return String.valueOf(tage) + " Tage " + stunden + " Stunden " + minuten + " Minute " + sekunden + " Sekunde";
        }
        if (tage != 1 & stunden == 1 & minuten != 1 & sekunden != 1) {
            return String.valueOf(tage) + " Tage " + stunden + " Stunde " + minuten + " Minuten " + sekunden + " Sekunden";
        }
        if (tage != 1 & stunden == 1 & minuten != 1 & sekunden == 1) {
            return String.valueOf(tage) + " Tage " + stunden + " Stunde " + minuten + " Minuten " + sekunden + " Sekunde";
        }
        if (tage != 1 & stunden == 1 & minuten == 1 & sekunden != 1) {
            return String.valueOf(tage) + " Tage " + stunden + " Stunde " + minuten + " Minute " + sekunden + " Sekunden";
        }
        if (tage != 1 & stunden == 1 & minuten == 1 & sekunden == 1) {
            return String.valueOf(tage) + " Tage " + stunden + " Stunde " + minuten + " Minute " + sekunden + " Sekunde";
        }
        if (tage == 1 & stunden != 1 & minuten != 1 & sekunden != 1) {
            return String.valueOf(tage) + " Tag " + stunden + " Stunden " + minuten + " Minuten " + sekunden + " Sekunden";
        }
        if (tage == 1 & stunden != 1 & minuten != 1 & sekunden == 1) {
            return String.valueOf(tage) + " Tag " + stunden + " Stunden " + minuten + " Minuten " + sekunden + " Sekunde";
        }
        if (tage == 1 & stunden != 1 & minuten == 1 & sekunden != 1) {
            return String.valueOf(tage) + " Tag " + stunden + " Stunden " + minuten + " Minute " + sekunden + " Sekunden";
        }
        if (tage == 1 & stunden != 1 & minuten == 1 & sekunden == 1) {
            return String.valueOf(tage) + " Tag " + stunden + " Stunden " + minuten + " Minute " + sekunden + " Sekunde";
        }
        if (tage == 1 & stunden == 1 & minuten != 1 & sekunden != 1) {
            return String.valueOf(tage) + " Tag " + stunden + " Stunde " + minuten + " Minuten " + sekunden + " Sekunden";
        }
        if (tage == 1 & stunden == 1 & minuten != 1 & sekunden == 1) {
            return String.valueOf(tage) + " Tag " + stunden + " Stunde " + minuten + " Minuten " + sekunden + " Sekunde";
        }
        if (tage == 1 & stunden == 1 & minuten == 1 & sekunden != 1) {
            return String.valueOf(tage) + " Tag " + stunden + " Stunde " + minuten + " Minute " + sekunden + " Sekunden";
        }
        if (tage == 1 & stunden == 1 & minuten == 1 & sekunden == 1) {
            return String.valueOf(tage) + " Tag " + stunden + " Stunde " + minuten + " Minute " + sekunden + " Sekunde";
        }
        if (tage != 1 & stunden != 1 & minuten != 1 & sekunden != 1) {
            return String.valueOf(tage) + " Tage " + stunden + " Stunden " + minuten + " Minuten " + sekunden + " Sekunden";
        }
        if (tage != 1 & stunden != 1 & minuten != 1 & sekunden == 1) {
            return String.valueOf(tage) + " Tage " + stunden + " Stunden " + minuten + " Minuten " + sekunden + " Sekunde";
        }
        if (tage != 1 & stunden != 1 & minuten == 1 & sekunden != 1) {
            return String.valueOf(tage) + " Tage " + stunden + " Stunden " + minuten + " Minute " + sekunden + " Sekunden";
        }
        if (tage != 1 & stunden != 1 & minuten == 1 & sekunden == 1) {
            return String.valueOf(tage) + " Tage " + stunden + " Stunden " + minuten + " Minute " + sekunden + " Sekunde";
        }
        if (tage != 1 & stunden == 1 & minuten != 1 & sekunden != 1) {
            return String.valueOf(tage) + " Tage " + stunden + " Stunde " + minuten + " Minuten " + sekunden + " Sekunden";
        }
        if (tage != 1 & stunden == 1 & minuten != 1 & sekunden == 1) {
            return String.valueOf(tage) + " Tage " + stunden + " Stunde " + minuten + " Minuten " + sekunden + " Sekunde";
        }
        if (tage != 1 & stunden == 1 & minuten == 1 & sekunden != 1) {
            return String.valueOf(tage) + " Tage " + stunden + " Stunde " + minuten + " Minute " + sekunden + " Sekunden";
        }
        if (tage != 1 & stunden == 1 & minuten == 1 & sekunden == 1) {
            return String.valueOf(tage) + " Tage " + stunden + " Stunde " + minuten + " Minute " + sekunden + " Sekunde";
        }
        if (tage == 1 & stunden != 1 & minuten != 1 & sekunden != 1) {
            return String.valueOf(tage) + " Tag " + stunden + " Stunden " + minuten + " Minuten " + sekunden + " Sekunden";
        }
        if (tage == 1 & stunden != 1 & minuten != 1 & sekunden == 1) {
            return String.valueOf(tage) + " Tag " + stunden + " Stunden " + minuten + " Minuten " + sekunden + " Sekunde";
        }
        if (tage == 1 & stunden != 1 & minuten == 1 & sekunden != 1) {
            return String.valueOf(tage) + " Tag " + stunden + " Stunden " + minuten + " Minute " + sekunden + " Sekunden";
        }
        if (tage == 1 & stunden != 1 & minuten == 1 & sekunden == 1) {
            return String.valueOf(tage) + " Tag " + stunden + " Stunden " + minuten + " Minute " + sekunden + " Sekunde";
        }
        if (tage == 1 & stunden == 1 & minuten != 1 & sekunden != 1) {
            return String.valueOf(tage) + " Tag " + stunden + " Stunde " + minuten + " Minuten " + sekunden + " Sekunden";
        }
        if (tage == 1 & stunden == 1 & minuten != 1 & sekunden == 1) {
            return String.valueOf(tage) + " Tag " + stunden + " Stunde " + minuten + " Minuten " + sekunden + " Sekunde";
        }
        if (tage == 1 & stunden == 1 & minuten == 1 & sekunden != 1) {
            return String.valueOf(tage) + " Tag " + stunden + " Stunde " + minuten + " Minute " + sekunden + " Sekunden";
        }
        if (tage == 1 & stunden == 1 & minuten == 1 & sekunden == 1) {
            return String.valueOf(tage) + " Tag " + stunden + " Stunde " + minuten + " Minute " + sekunden + " Sekunde";
        }
        if (tage != 1 & stunden != 1 & minuten != 1 & sekunden != 1) {
            return String.valueOf(tage) + " Tage " + stunden + " Stunden " + minuten + " Minuten " + sekunden + " Sekunden";
        }
        if (tage != 1 & stunden != 1 & minuten != 1 & sekunden == 1) {
            return String.valueOf(tage) + " Tage " + stunden + " Stunden " + minuten + " Minuten " + sekunden + " Sekunde";
        }
        if (tage != 1 & stunden != 1 & minuten == 1 & sekunden != 1) {
            return String.valueOf(tage) + " Tage " + stunden + " Stunden " + minuten + " Minute " + sekunden + " Sekunden";
        }
        if (tage != 1 & stunden != 1 & minuten == 1 & sekunden == 1) {
            return String.valueOf(tage) + " Tage " + stunden + " Stunden " + minuten + " Minute " + sekunden + " Sekunde";
        }
        if (tage != 1 & stunden == 1 & minuten != 1 & sekunden != 1) {
            return String.valueOf(tage) + " Tage " + stunden + " Stunde " + minuten + " Minuten " + sekunden + " Sekunden";
        }
        if (tage != 1 & stunden == 1 & minuten != 1 & sekunden == 1) {
            return String.valueOf(tage) + " Tage " + stunden + " Stunde " + minuten + " Minuten " + sekunden + " Sekunde";
        }
        if (tage != 1 & stunden == 1 & minuten == 1 & sekunden != 1) {
            return String.valueOf(tage) + " Tage " + stunden + " Stunde " + minuten + " Minute " + sekunden + " Sekunden";
        }
        if (tage != 1 & stunden == 1 & minuten == 1 & sekunden == 1) {
            return String.valueOf(tage) + " Tage " + stunden + " Stunde " + minuten + " Minute " + sekunden + " Sekunde";
        }
        return null;
    }
}
