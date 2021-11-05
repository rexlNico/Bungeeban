package de.rexlNico.Ban.Main;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import de.rexlNico.Ban.Api.MySQL;
import de.rexlNico.Ban.Commands.ban;
import de.rexlNico.Ban.Commands.check;
import de.rexlNico.Ban.Commands.unban;
import de.rexlNico.Ban.Commands.unmute;
import de.rexlNico.Ban.Liteners.chat;
import de.rexlNico.Ban.Liteners.login;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

public class main extends Plugin
{
    public static main main;
    public static MySQL mysql;
    public static ArrayList<ProxiedPlayer> teamban;
    public ExecutorService executorService;
    public String pr;
    
    static {
    	teamban = new ArrayList<ProxiedPlayer>();
    }
    
    public main() throws IOException {
        this.executorService = Executors.newCachedThreadPool();
        this.pr = "§6BanSystem §8| §e";
    }
    
    public void onEnable() {
        main = this;
        BungeeCord.getInstance().getPluginManager().registerCommand((Plugin)this, (Command)new ban("ban"));
        BungeeCord.getInstance().getPluginManager().registerCommand((Plugin)this, (Command)new check("check"));
        BungeeCord.getInstance().getPluginManager().registerCommand((Plugin)this, (Command)new ban("warn"));
        BungeeCord.getInstance().getPluginManager().registerCommand((Plugin)this, (Command)new ban("b"));
        BungeeCord.getInstance().getPluginManager().registerCommand((Plugin)this, (Command)new unban("unban"));
        BungeeCord.getInstance().getPluginManager().registerCommand((Plugin)this, (Command)new unmute("unmute"));
        BungeeCord.getInstance().getPluginManager().registerListener((Plugin)this, (Listener)new login());
        BungeeCord.getInstance().getPluginManager().registerListener((Plugin)this, (Listener)new chat());
        try {
            this.ConnectMySQL();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        
        
        
    }
    
    @SuppressWarnings({ "unchecked", "rawtypes" })
	private void ConnectMySQL() throws IOException {
    	final Configuration configuration = ConfigurationProvider.getProvider((Class)YamlConfiguration.class).load(new File(this.getDataFolder(), "config.yml"));
        if (!this.getDataFolder().exists()) {
            this.getDataFolder().mkdir();
        }
        final File file = new File(this.getDataFolder(), "config.yml");
        if (!file.exists()) {
            try {
                Throwable t = null;
                try {
                    final InputStream in = this.getResourceAsStream("config.yml");
                    try {
                        Files.copy(in, file.toPath(), new CopyOption[0]);
                    }
                    finally {
                        if (in != null) {
                            in.close();
                        }
                    }
                }
                finally {
                    if (t == null) {
                        final Throwable t2 = null;
                        t = t2;
                    }
                    else {
                        final Throwable t2 = null;
                        if (t != t2) {
                            t.addSuppressed(t2);
                        }
                    }
                }
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        mysql = new MySQL(configuration.getString("Host"), configuration.getString("Database"), configuration.getString("User"), configuration.getString("Passwort"));
        ConfigurationProvider.getProvider((Class)YamlConfiguration.class).save(configuration, new File(this.getDataFolder(), "config.yml"));
        mysql.update("CREATE TABLE IF NOT EXISTS HistoryBan(UUID varchar(64), Datum varchar(100), Uhrzeit varchar(100), Grund varchar(100), Length int, UNIQUE KEY(UUID));");
        mysql.update("CREATE TABLE IF NOT EXISTS Tempban(UUID varchar(64), End varchar(100), Reason varchar(100), VON varchar(100), POINTS int, BANNED int, UNIQUE KEY(UUID));");
        mysql.update("CREATE TABLE IF NOT EXISTS HistoryMute(UUID varchar(64), Datum varchar(100), Uhrzeit varchar(100), Grund varchar(100), Length int, UNIQUE KEY(UUID));");
        mysql.update("CREATE TABLE IF NOT EXISTS Tempmute(UUID varchar(64), End varchar(100), Reason varchar(100), VON varchar(100), POINTS int, BANNED int, UNIQUE KEY(UUID));");
        mysql.update("create table if not exists uuids (name varchar(64), uniqueid varchar(100), playername varchar(100), UNIQUE KEY(name));");
    }
}
