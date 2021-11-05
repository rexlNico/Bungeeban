package de.rexlNico.Ban.Commands;

import net.md_5.bungee.api.plugin.*;
import de.rexlNico.Ban.Api.Method_PlayerUUID;
import de.rexlNico.Ban.Api.Tempban;
import de.rexlNico.Ban.Api.Tempmute;
import de.rexlNico.Ban.Main.main;
import net.md_5.bungee.api.*;
import net.md_5.bungee.api.connection.*;

public class check extends Command
{
    public check(final String name) {
        super(name);
    }
    
    @SuppressWarnings({ "deprecation", "null" })
	public void execute(final CommandSender s, final String[] args) {
        final ProxiedPlayer p = (ProxiedPlayer)s;
        if (p.hasPermission("BanSystem.ban")) {
            if (args.length == 1) {
                final ProxiedPlayer proxiedPlayer = null;
                main.main.executorService.submit(() -> {
                	final String uuid;
                    if (Method_PlayerUUID.playerExists(args[0])) {
                        uuid = Method_PlayerUUID.getUUID(args[0]);
                        proxiedPlayer.sendMessage("§7§m------------------------------------------");
                        proxiedPlayer.sendMessage("");
                        if (Tempban.playerExists(uuid)) {
                            if (Tempban.getTempBanned(uuid) == 1) {
                                proxiedPlayer.sendMessage("§7The Player §6" + args[0] + " §7is §cbaned!");
                                proxiedPlayer.sendMessage("§7Reason: §c" + Tempban.getReason(uuid));
                                proxiedPlayer.sendMessage("§7Time: §c" + Tempban.getTime(uuid));
                            }
                            else {
                                proxiedPlayer.sendMessage("§7The Player §6" + args[0] + " §7is not baned!");
                            }
                        }
                        else {
                        	proxiedPlayer.sendMessage("§7The Player §6" + args[0] + " §7is not baned!");
                        }
                        proxiedPlayer.sendMessage("");
                        if (Tempmute.playerExists(uuid)) {
                            if (Tempmute.getTempBanned(uuid) == 1) {
                                proxiedPlayer.sendMessage("§7The Player §6" + args[0] + " §7is §cmuted!");
                                proxiedPlayer.sendMessage("§7Reason: §c" + Tempmute.getReason(uuid));
                                proxiedPlayer.sendMessage("§7Time: §c" + Tempmute.getTime(uuid));
                            }
                            else {
                            	proxiedPlayer.sendMessage("§7The Player §6" + args[0] + " §7is not muted!");
                            }
                        }
                        else {
                        	proxiedPlayer.sendMessage("§7The Player §6" + args[0] + " §7is not muted!");
                        }
                        proxiedPlayer.sendMessage("");
                        proxiedPlayer.sendMessage("§7§m------------------------------------------");
                    }
                });
            }
            else {
                p.sendMessage(String.valueOf(main.main.pr) + "§c/check [Name]");
            }
        }
    }
}
