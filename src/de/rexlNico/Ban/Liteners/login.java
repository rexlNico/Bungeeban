package de.rexlNico.Ban.Liteners;

import net.md_5.bungee.api.plugin.*;
import net.md_5.bungee.event.*;
import net.md_5.bungee.api.event.*;
import de.rexlNico.Ban.Api.Method_PlayerUUID;
import de.rexlNico.Ban.Api.Tempban;
import de.rexlNico.Ban.Main.main;
import net.md_5.bungee.api.connection.*;

public class login implements Listener
{
    @SuppressWarnings("deprecation")
	@EventHandler
    public void onLogin(final PreLoginEvent e) {
        final String name = e.getConnection().getName();
        if (Method_PlayerUUID.playerExists(name)) {
            final String uuid = Method_PlayerUUID.getUUID(name);
            if (Tempban.playerExists(uuid) && Tempban.getTempBanned(uuid) == 1) {
                if (Tempban.getEnd2(uuid) == -1L) {
                    e.setCancelled(true);
                    e.setCancelReason("§cYou were banned from our network! \n\n§cReason: §4" + Tempban.getReason(uuid) + "\n\n§cRemaining time: §4PERMANENT\n\n§cRequest for unban can you put in the forum or in the TeamSpeak");
                    return;
                }
                if (Tempban.getEnd(uuid) == 0L) {
                    Tempban.unban(uuid, 2);
                    return;
                }
                e.setCancelled(true);
                e.setCancelReason("§cYou were banned from our network! \n\n§cReason: §4" + Tempban.getReason(uuid) + "\n\n§cRemaining time: §4"+Tempban.getTime(uuid)+"\n\n§cRequest for unban can you put in the forum or in the TeamSpeak");
            }
        }
    }
    
    @SuppressWarnings("deprecation")
	@EventHandler
    public void onPostLogin(final PostLoginEvent event) {
        final ProxiedPlayer player = event.getPlayer();
        final String uuid = player.getUniqueId().toString();
        if (!Method_PlayerUUID.playerExists(player.getName())) {
            Method_PlayerUUID.createPlayerData(player.getName(), uuid);
        }
        if (Tempban.playerExists(uuid) && Tempban.getTempBanned(uuid) == 1) {
            if (Tempban.getEnd2(uuid) == -1L) {
                player.disconnect("§cYou were banned from our network! \n\n§cReason: §4" + Tempban.getReason(uuid) + "\n\n§cRemaining time: §4PERMANENT\n\n§cRequest for unban can you put in the forum or in the TeamSpeak");
                return;
            }
            if (Tempban.getEnd(uuid) == 0L) {
                Tempban.unban(uuid, 2);
                return;
            }
            player.disconnect("§cYou were banned from our network! \n\n§cReason: §4" + Tempban.getReason(uuid) + "\n\n§cRemaining time: §4"+Tempban.getTime(uuid)+"\n\n§cRequest for unban can you put in the forum or in the TeamSpeak");
        }
        if (player.getGroups().contains("admin") || player.getGroups().contains("Team") || player.hasPermission("BanSystem.ban")) {
            main.teamban.add(player);
        }
    }
}
