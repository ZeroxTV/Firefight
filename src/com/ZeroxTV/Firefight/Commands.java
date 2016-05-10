package me.ZeroxTV.Firefight;

import java.io.File;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public class Commands implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(args.length == 0) {
			if(Methods.checkPerm(sender, "firefight.help")) {
				sender.sendMessage("§4--------------------Firefight--------------------");
				sender.sendMessage("§4Commands§8:");
				sender.sendMessage("§4/help§8: Zeige diese Command-Liste");
				sender.sendMessage("§4/setspawn§8 (red/blue): Setze den Spawn des jeweiligen Teams");
				sender.sendMessage("");
				sender.sendMessage("");
				sender.sendMessage("");
			}
		}
		else if(args.length == 1) {
			if(args[0].equalsIgnoreCase("start")) {
				Methods.instantStart(sender);
			}
			else if(args[0].equalsIgnoreCase("spawn")) {
				Methods.teleportToSpawn(Main.teams.get(sender), (Player) sender);
			} else if(args[0].equalsIgnoreCase("check")) {
				Methods.startCheckingArea();
			} else if(args[0].equalsIgnoreCase("cancel")) {
				Bukkit.getScheduler().cancelTask(Methods.checker);
			}
		}
		else if(args.length == 2) {
			if(args[0].equalsIgnoreCase("setspawn")) {
				try {
					Methods.setSpawn(sender, args[1]);
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else if(args[0].equalsIgnoreCase("weapon")) {
				Methods.giveWeapon((Player) sender, args[1]);
			} else if(args[0].equalsIgnoreCase("area")) {
				if (!(sender instanceof Player)) {
					sender.sendMessage("Diese Befehl ist nur fuer Spieler");
				} else {
					Player p = (Player) sender;
					Location loc = p.getLocation();
					File config = new File("plugins//Firefight//config.yml");
					FileConfiguration cfg = YamlConfiguration.loadConfiguration(config);
					
					if(args[1].equals("a")) {
						cfg.set("settings.area.xa", loc.getBlockX());
						cfg.set("settings.area.ya", loc.getBlockY());
						cfg.set("settings.area.za", loc.getBlockZ());
						p.sendMessage(Main.pre + "Punkt a der Area wurde gesetzt.");
					} else if(args[1].equals("b")) {
						cfg.set("settings.area.xb", loc.getBlockX());
						cfg.set("settings.area.yb", loc.getBlockY());
						cfg.set("settings.area.zb", loc.getBlockZ());
						p.sendMessage(Main.pre + "Punkt b der Area wurde gesetzt.");
					}
					cfg.set("settings.area.world", loc.getWorld().getName());
					try {
						cfg.save(config);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			} else if(args[0].equalsIgnoreCase("dom")) {
				Main.dominating = args[1];
				Methods.changeArea(args[1]);
			}
		}
		else {
			sender.sendMessage("§4[Firefight] §8Unbekannter Befehl");
		}

		return true;
	}
}
