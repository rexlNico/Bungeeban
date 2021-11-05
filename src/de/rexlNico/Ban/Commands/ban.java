package de.rexlNico.Ban.Commands;

import net.md_5.bungee.api.plugin.*;
import net.md_5.bungee.api.connection.*;
import java.util.*;

import de.rexlNico.Ban.Api.Method_PlayerUUID;
import de.rexlNico.Ban.Api.Tempban;
import de.rexlNico.Ban.Api.Tempmute;
import de.rexlNico.Ban.Main.main;
import net.md_5.bungee.api.*;
import net.md_5.bungee.*;

public class ban extends Command
{
    public ban(final String name) {
        super(name);
    }
    
    @SuppressWarnings("deprecation")
	public void banplayer(final ProxiedPlayer banner, final ProxiedPlayer banneduser, final String bannedname, final String grund) {
        if (!Method_PlayerUUID.playerExists(bannedname)) {
            banner.sendMessage(String.valueOf(main.main.pr) + "§7The specified player has never been online.");
            return;
        }
        final String uuid = Method_PlayerUUID.getUUID(bannedname);
        if (Tempban.playerExists(uuid) && Tempban.getTempBanned(uuid) == 1) {
            banner.sendMessage(String.valueOf(main.main.pr) + "This player is already banned!");
            return;
        }
        if (Tempban.getPoints(uuid) == 1) {
            Tempban.setTempban2(uuid, grund, 2592000L, banner.getName(), 2, 1);
            if (banneduser != null) {
                banneduser.disconnect("§cYou were banned from our network! \n\n§cReason: §4" + grund + "\n\n§cRequest for unban can you put in the forum or in the TeamSpeak");
            }
            for (final ProxiedPlayer all : main.teamban) {
                all.sendMessage(String.valueOf(main.main.pr) + "§c" + bannedname + " §ewas baned from §6" + banner.getName());
                all.sendMessage(String.valueOf(main.main.pr) + "§cReason: §4" + grund);
            }
            return;
        }
        if (Tempban.getPoints(uuid) == 2) {
            Tempban.setTempban2(uuid, grund, -1L, banner.getName(), 2, 1);
            if (banneduser != null) {
            	banneduser.disconnect("§cYou were banned from our network! \n\n§cReason: §4" + grund + "\n\n§cRequest for unban can you put in the forum or in the TeamSpeak");
            }
            for (final ProxiedPlayer all : main.teamban) {
                all.sendMessage(String.valueOf(main.main.pr) + "§c" + bannedname + " §ewas baned from §6" + banner.getName());
                all.sendMessage(String.valueOf(main.main.pr) + "§cReason: §4" + grund);
            }
            return;
        }
        if (Tempban.getPoints(uuid) == 0) {
            Tempban.setTempban(uuid, grund, 1209600L, banner.getName(), 1);
            if (banneduser != null) {
            	banneduser.disconnect("§cYou were banned from our network! \n\n§cReason: §4" + grund + "\n\n§cRequest for unban can you put in the forum or in the TeamSpeak");
            }
            for (final ProxiedPlayer all : main.teamban) {
                all.sendMessage(String.valueOf(main.main.pr) + "§c" + bannedname + " §ewas baned from §6" + banner.getName());
                all.sendMessage(String.valueOf(main.main.pr) + "§cReason: §4" + grund);
            }
        }
    }
    
    @SuppressWarnings({ "deprecation", "null" })
	public void muteplayer(final ProxiedPlayer banner, final String bannedname, final String grund) {
        main.main.executorService.submit(() -> {
        	 final String uuid;
             final Iterator<ProxiedPlayer> iterator = null;
             ProxiedPlayer all;
             final Iterator<ProxiedPlayer> iterator2 = null;
             ProxiedPlayer all2;
             final Iterator<ProxiedPlayer> iterator3 = null;
             ProxiedPlayer all3;
            if (!Method_PlayerUUID.playerExists(bannedname)) {
                banner.sendMessage(String.valueOf(main.main.pr) + "§7The specified player has never been online.");
            }
            else {
                uuid = Method_PlayerUUID.getUUID(bannedname);
                if (Tempmute.playerExists(uuid) && Tempmute.getTempBanned(uuid) == 1) {
                    banner.sendMessage(String.valueOf(main.main.pr) + "This player is already banned!");
                }
                else if (Tempmute.getPoints(uuid) == 1) {
                    Tempmute.setTempmute2(uuid, grund, 2592000L, banner.getName(), 2, 1);
                    main.teamban.iterator();
                    while (iterator.hasNext()) {
                        all = iterator.next();
                        all.sendMessage(String.valueOf(main.main.pr) + "§c" + bannedname + " §ewas muted from §6" + banner.getName());
                        all.sendMessage(String.valueOf(main.main.pr) + "§cReason: §4" + grund);
                    }
                }
                else if (Tempmute.getPoints(uuid) == 2) {
                    Tempmute.setTempmute2(uuid, grund, -1L, banner.getName(), 2, 1);
                    main.teamban.iterator();
                    while (iterator2.hasNext()) {
                        all2 = iterator2.next();
                        all2.sendMessage(String.valueOf(main.main.pr) + "§c" + bannedname + " §ewas muted from §6" + banner.getName());
                        all2.sendMessage(String.valueOf(main.main.pr) + "§cReason: §4" + grund);;
                    }
                }
                else if (Tempmute.getPoints(uuid) == 0) {
                    Tempmute.setTempmute(uuid, grund, 1209600L, banner.getName(), 1);
                    main.teamban.iterator();
                    while (iterator3.hasNext()) {
                        all3 = iterator3.next();
                        all3.sendMessage(String.valueOf(main.main.pr) + "§c" + bannedname + " §ewas muted from §6" + banner.getName());
                        all3.sendMessage(String.valueOf(main.main.pr) + "§cReason: §4" + grund);
                    }
                }
            }
        });
    }
    
    @SuppressWarnings("deprecation")
	public void execute(final CommandSender s, final String[] args) {
        final ProxiedPlayer p = (ProxiedPlayer)s;
        if (p.hasPermission("BanSystem.ban")) {
            if (args.length == 2) {
                final ProxiedPlayer p2 = BungeeCord.getInstance().getPlayer(args[0]);
                if (args[0].equals(p.getName())) {
                    p.sendMessage(String.valueOf(main.main.pr) + "§cYou can not ban yourself!");
                    return;
                }
                if (args[1].equalsIgnoreCase("1")) {
                    this.banplayer(p, p2, args[0], "Hacking");
                }
                else if (args[1].equalsIgnoreCase("2")) {
                    this.muteplayer(p, args[0], "Insult");
                }
                else if (args[1].equalsIgnoreCase("3")) {
                    this.muteplayer(p, args[0], "Respectless behavior");
                }
                else if (args[1].equalsIgnoreCase("4")) {
                    this.muteplayer(p, args[0], "Provocative behavior");
                }
                else if (args[1].equalsIgnoreCase("5")) {
                    this.muteplayer(p, args[0], "Spamming");
                }
                else if (args[1].equalsIgnoreCase("6")) {
                    this.muteplayer(p, args[0], "advertising");
                }
                else if (args[1].equalsIgnoreCase("7")) {
                    this.banplayer(p, p2, args[0], "Report exploitation");
                }
                else if (args[1].equalsIgnoreCase("8")) {
                    this.muteplayer(p, args[0], "threat");
                }
                else if (args[1].equalsIgnoreCase("9")) {
                    this.banplayer(p, p2, args[0], "Name/Skin");
                }
                else if (args[1].equalsIgnoreCase("10")) {
                    this.banplayer(p, p2, args[0], "Teaming");
                }
                else if (args[1].equalsIgnoreCase("11")) {
                    this.banplayer(p, p2, args[0], "Bugusing");
                }
                else if (args[1].equalsIgnoreCase("12")) {
                    final String uuid = Method_PlayerUUID.getUUID(args[0]);
                    if (Tempban.playerExists(uuid) && Tempban.getTempBanned(uuid) == 1) {
                        p.sendMessage(String.valueOf(main.main.pr) + "This player is already banned!");
                        return;
                    }
                    if (Tempban.getPoints(uuid) <= 29 && Tempban.getPoints(uuid) >= 0) {
                        Tempban.setTempban2(uuid, "House ban", -1L, p.getName(), 31, 1);
                    }
                    else {
                        Tempban.setTempban(uuid, "House ban", -1L, p.getName(), 31);
                    }
                    if (p2 != null) {
                        p2.disconnect(String.valueOf(main.main.pr) + "§cYou were banned from our network! \n\n§cReason: §4House ban" + "\n\n§cRequest for unban can you put in the forum or in the TeamSpeak");
                    }
                    for (final ProxiedPlayer all : main.teamban) {
                        all.sendMessage(String.valueOf(main.main.pr) + "§c" + args[0] + " §ewas baned from §6" + p.getName() );
                        all.sendMessage(String.valueOf(main.main.pr) + "§cReason: §4House ban");
                    }
                }
                else if (args[1].equalsIgnoreCase("13")) {
                    this.banplayer(p, p2, args[0], "Spawntrapping");
                }
            }
            else {
                p.sendMessage("§cBitte gib eine Bann Id an");
                p.sendMessage("§6M\u00f6gliche Bann Ids sind:");
                p.sendMessage(" §8§o- §c 1 §7| §cHacks/ Hackclient §7> §4NETWORK BAN");
                p.sendMessage(" §8§o- §c 2 §7| §cInsult §7> §4MUTE");
                p.sendMessage(" §8§o- §c 3 §7| §cRespectless behavior §7> §4MUTE");
                p.sendMessage(" §8§o- §c 4 §7| §cProvocative behavior §7> §4MUTE");
                p.sendMessage(" §8§o- §c 5 §7| §cSpamming §7> §4MUTE");
                p.sendMessage(" §8§o- §c 6 §7| §cadvertising §7> §4MUTE");
                p.sendMessage(" §8§o- §c 7 §7| §cReport exploitation §7> §4NETWORK BAN");
                p.sendMessage(" §8§o- §c 8 §7| §cthreat §7> §4MUTE");
                p.sendMessage(" §8§o- §c 9 §7| §cImproper Skin/Name §7> §4NETWORK BAN");
                p.sendMessage(" §8§o- §c10 §7| §cTeaming §7> §4NETWORK BAN");
                p.sendMessage(" §8§o- §c11 §7| §cBugusing §7> §4NETWORK BAN");
                p.sendMessage(" §8§o- §c12 §7| §cHouse bant §7> §4NETWORK BAN");
                p.sendMessage(" §8§o- §c13 §7| §cSpawntrapping §7> §4NETWORK BAN");
            }
        }
        else {
            p.sendMessage(String.valueOf(main.main.pr) + "§cYou do not have enough permissions");
        }
    }
}
