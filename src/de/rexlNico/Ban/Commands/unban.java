package de.rexlNico.Ban.Commands;

import net.md_5.bungee.api.plugin.*;
import de.rexlNico.Ban.Api.Method_PlayerUUID;
import de.rexlNico.Ban.Api.Tempban;
import de.rexlNico.Ban.Main.main;
import net.md_5.bungee.api.*;
import net.md_5.bungee.api.connection.*;

public class unban extends Command
{
    public unban(final String name) {
        super(name);
    }
    
    @SuppressWarnings({ "deprecation", "null" })
	public void execute(final CommandSender s, final String[] args) {
        final ProxiedPlayer p = (ProxiedPlayer)s;
        if (p.hasPermission("BanSystem.unban")) {
            if (args.length == 1) {
                final ProxiedPlayer proxiedPlayer = null;
                main.main.executorService.submit(() -> {
                	final String uuid;
                    if (Method_PlayerUUID.playerExists(args[0])) {
                        uuid = Method_PlayerUUID.getUUID(args[0]);
                        if (Tempban.playerExists(uuid)) {
                            if (Tempban.getTempBanned(uuid) == 1) {
                                Tempban.unban(uuid, 2);
                                proxiedPlayer.sendMessage(String.valueOf(main.main.pr) + "§eThe Player §6" + args[0] + " §eis now unbaned");
                            }
                            else {
                                proxiedPlayer.sendMessage(String.valueOf(main.main.pr) + "§cThe Player is not baned!");
                            }
                        }
                        else {
                        	 proxiedPlayer.sendMessage(String.valueOf(main.main.pr) + "§cThe Player is not baned!");
                        }
                    }
                    else {
                        proxiedPlayer.sendMessage(String.valueOf(main.main.pr) + "§cThe specified player has never been online.");
                    }
                });
            }
            else {
                p.sendMessage(String.valueOf(main.main.pr) + "§c/unban [Name]");
            }
        }
        else {
            p.sendMessage(String.valueOf(main.main.pr) + "§cYou do not have enough permissions.");
        }
    }
}
