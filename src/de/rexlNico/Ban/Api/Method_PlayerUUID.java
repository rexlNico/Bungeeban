package de.rexlNico.Ban.Api;

import java.sql.*;

import de.rexlNico.Ban.Main.main;

public class Method_PlayerUUID
{
    public static boolean playerExists(final String name) {
        final ResultSet rs = main.mysql.query("select * from uuids where name= '" + name.toLowerCase() + "'");
        try {
            if (rs.next() && rs.getString("name") != null) {
                MySQL.closeResultset(rs);
                return true;
            }
        }
        catch (SQLException ex) {}
        MySQL.closeResultset(rs);
        return false;
    }
    
    public static void createPlayerData(final String name, final String uuid) {
        if (!playerExists(name)) {
            main.mysql.update("insert into uuids (name, uniqueid, playername) values ('" + name.toLowerCase() + "','" + uuid + "', '" + name + "');");
        }
        setName(name, uuid);
    }
    
    public static String getUUID(final String name) {
        final ResultSet rs = main.mysql.query("select * from uuids where name= '" + name.toLowerCase() + "'");
        try {
            if (rs.next()) {
                final String uuid = rs.getString("uniqueid");
                MySQL.closeResultset(rs);
                return uuid;
            }
        }
        catch (SQLException ex) {}
        MySQL.closeResultset(rs);
        return "null";
    }
    
    public static String getName(final String name) {
        final ResultSet rs = main.mysql.query("select * from uuids where name= '" + name.toLowerCase() + "'");
        try {
            if (rs.next()) {
                final String uuid = rs.getString("playername");
                MySQL.closeResultset(rs);
                return uuid;
            }
        }
        catch (SQLException ex) {}
        MySQL.closeResultset(rs);
        return "null";
    }
    
    public static void setName(final String name, final String uuid) {
        main.mysql.update("update uuids set name= '" + name.toLowerCase() + "' where uniqueid= '" + uuid + "';");
    }
}
