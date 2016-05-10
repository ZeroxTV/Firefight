package me.ZeroxTV.Firefight;

import java.util.HashMap;
import java.util.HashSet;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
	
	
	
	public static HashMap<Player,String> klasse = new HashMap<Player,String>();
	public static HashMap<Player,String> weapon = new HashMap<Player,String>();
	public static HashMap<Player, String> teams = new HashMap<Player, String>();
	public static boolean gameInProgress = false;
	public static HashMap<Player, Boolean> inRegion = new HashMap<Player, Boolean>();
	public static boolean countdownStarted = false;
	public static String dominating = "none";
	public static JavaPlugin plugin;
	public static String pre = "§8[§6Fire§4Fight§8] §7";
	public static HashSet<String> sniper = new HashSet<String>();
	public static HashSet<String> primary = new HashSet<String>();
	public static HashSet<String> secondary = new HashSet<String>();
	public static HashMap<Player, ItemStack> helmet = new HashMap<Player, ItemStack>();
	
	public void onEnable() {
		Bukkit.getConsoleSender().sendMessage(pre + "§7Plugin wurde aktiviert.");
		Bukkit.getConsoleSender().sendMessage(pre + "§7Version: " + this.getDescription().getVersion());
		Bukkit.getConsoleSender().sendMessage(pre + "§7Author: " + this.getDescription().getAuthors());
		initConfig();
		new EventListener(this);
		plugin = this;
		this.getCommand("firefight").setExecutor(new Commands());
		
		if (Bukkit.getOnlinePlayers().size() > 0) {
			Methods.assignAll();
		}
		
		//Snipers:
		sniper.add("Dragunov");
		
		//Primary:
		primary.add("MG4");
		primary.add("MP5");
		primary.add("Dragunov");
		
		//Secondary:
		secondary.add("USP");
	}
	
	
	
	public void onDisable() {
		Bukkit.getConsoleSender().sendMessage("§4[Firefight]§8 Plugin wurde deaktiviert.");	
	}
	
	
	
	public void initConfig() {
		
		
		//this.reloadConfig();
		
		//this.getConfig().options().header("---------------Firefight Config---------------");
		//this.getConfig().addDefault("settings.game.Countdown", 60);
		//this.getConfig().addDefault("settings.Spawns.blue.X", 0);
		//this.getConfig().addDefault("settings.Spawns.blue.Y", 70);
		//this.getConfig().addDefault("settings.Spawns.blue.Z", 0);
		//this.getConfig().addDefault("settings.Spawns.blue.Yaw", 0);
		//this.getConfig().addDefault("settings.Spawns.blue.Pitch", 0);
		//this.getConfig().addDefault("settings.Spawns.blue.World", "world");
		
		//this.getConfig().addDefault("settings.Spawns.red.X", 0);
		//this.getConfig().addDefault("settings.Spawns.red.Y", 70);
		//this.getConfig().addDefault("settings.Spawns.red.Z", 0);
		//this.getConfig().addDefault("settings.Spawns.red.Yaw", 0);
		//this.getConfig().addDefault("settings.Spawns.red.Pitch", 0);
		//this.getConfig().addDefault("settings.Spawns.red.World", "world");
		
		this.getConfig().options().copyDefaults(true);
		this.saveConfig();
	}
	
}
