package me.ZeroxTV.Firefight;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import com.shampaggon.crackshot.CSUtility;



public class Methods {
	
	static JavaPlugin plugin = Main.plugin;
	
	
	public static int Countdown = plugin.getConfig().getInt("settings.game.Countdown");
	public static int counter;
	public static int checker;
	public static String domOld = "none";
	
	public static ItemStack generateWeapon(String w) {
		CSUtility cs = new CSUtility();
		ItemStack weapon = cs.generateWeapon(w);
		ItemMeta meta = weapon.getItemMeta();
		meta.spigot().setUnbreakable(true);
		weapon.setItemMeta(meta);
		return weapon;
	}
	
	public static void giveWeapon(Player p, String w) {
		p.getInventory().addItem(generateWeapon(w));
	}
	
	
	public static void assignAll() {
		Collection<? extends Player> cplayers = Bukkit.getOnlinePlayers();
		Player[] players = (Player[]) cplayers.toArray(new Player[0]);
		
		for(int i = 0; i < players.length; i++) {
			Player z = players[i];
			assignTeam(z);
		}
	}
	
	@SuppressWarnings("deprecation")
	public static void assignTeam(Player p) {
		if(EventListener.red.getSize() > EventListener.blue.getSize()) {
			EventListener.blue.addPlayer(p);
			p.sendMessage(Main.pre + "§7Du bist im Team der §9Spezialeinheit§7.");
			Main.teams.put(p, "blue");
		} else {
			EventListener.red.addPlayer(p);
			p.sendMessage(Main.pre + "§7Du bist im Team der §cTerroristen§7.");
			Main.teams.put(p, "red");
		}
	}
	
	public static void setSpawn(CommandSender sender, String team) throws IOException {
		if(sender instanceof Player) {
			Player p = (Player) sender;
			if(Methods.checkPerm(p, "firefight.setspawn")) {
				if(team.equalsIgnoreCase("blue")) {
					File config = new File("plugins//Firefight//config.yml");
					FileConfiguration cfg = YamlConfiguration.loadConfiguration(config);
					Location loc = p.getLocation();
					cfg.set("settings.Spawns.blue.X", loc.getX());
					cfg.set("settings.Spawns.blue.Y", loc.getY());
					cfg.set("settings.Spawns.blue.Z", loc.getZ());
					cfg.set("settings.Spawns.blue.Yaw", loc.getYaw());
					cfg.set("settings.Spawns.blue.Pitch", loc.getPitch());
					cfg.set("settings.Spawns.blue.World", p.getWorld().getName());
					cfg.save(config);
					p.sendMessage("§4[Firefight] §8Du hast den Spawn von Team §9Blau §8gesetzt.");
				} else if(team.equalsIgnoreCase("red")) {
					File config = new File("plugins//Firefight//config.yml");
					FileConfiguration cfg = YamlConfiguration.loadConfiguration(config);
					Location loc = p.getLocation();
					cfg.set("settings.Spawns.red.X", loc.getX());
					cfg.set("settings.Spawns.red.Y", loc.getY());
					cfg.set("settings.Spawns.red.Z", loc.getZ());
					cfg.set("settings.Spawns.red.Yaw", loc.getYaw());
					cfg.set("settings.Spawns.red.Pitch", loc.getPitch());
					cfg.set("settings.Spawns.red.World", p.getWorld().getName());
					cfg.save(config);
					p.sendMessage("§4[Firefight] §8Du hast den Spawn von Team §cRot §8gesetzt.");
				} else {
					p.sendMessage("§4[Firefight] §8Diese Team gibt es nicht, wähle zwischen §cred §8und §9blue");
				}
			}
		} else {
			sender.sendMessage("§4[Firefight] §8Dieser Befehl kann nur von Spielern ausgeführt werden");
		}
	}
	
	public static ItemStack selector() {
		ItemStack selector = new ItemStack(Material.NETHER_STAR);
		ItemMeta selectorM = selector.getItemMeta();
		selectorM.setDisplayName("Ausrüstung bearbeiten");
		selector.setItemMeta(selectorM);
		return selector;
	}
	
	public static boolean checkPerm(CommandSender sender, String permission) {
		if(!sender.hasPermission(permission)) {
			sender.sendMessage("§4Zugriff verweigert");
			return false;
		} else {
			return true;
		}
	}
	
	public static void sendTeamMessage(String team, String message) {
		Collection<? extends Player> cplayers = Bukkit.getOnlinePlayers();
		Player[] players = (Player[]) cplayers.toArray(new Player[0]);
		
		for(int i = 0; i < players.length; i++) {
			Player z = players[i];
			if(Main.teams.get(z).equals(team)) {
				z.sendMessage(message);
			}
		}
	}
	
	public static void instantStart(CommandSender sender) {
		if(checkPerm(sender, "firefight.start")) {
			if (Bukkit.getOnlinePlayers().size() >= 6) {
				
				startGame();
				Bukkit.broadcastMessage("§4[Firefight] §8Das Spiel startet jetzt");
				Bukkit.getScheduler().cancelTask(counter);
				
			}
		}
	}
	
	public static void startGame() {
		Collection<? extends Player> cplayers = Bukkit.getOnlinePlayers();
		Player[] players = (Player[]) cplayers.toArray(new Player[0]);
		for(int i = 0; i < players.length; i++) {
			Player z = players[i];
			if(Main.teams.get(z).equals("blue")) {
				teleportToSpawn("blue", z);
			} else {
				teleportToSpawn("red", z);
			}
			Methods.giveKit(z);
		}
		Methods.startCheckingArea();
		
	}
	
	public static void teleportToSpawn(String team, Player p) {
		File config = new File("plugins//Firefight//Arenas//config.yml");
		FileConfiguration cfg = YamlConfiguration.loadConfiguration(config);
		Location loc = p.getLocation();
		double x = cfg.getDouble("settings.Spawns." + team + ".X");
		double y = cfg.getDouble("settings.Spawns." + team + ".Y");
		double z = cfg.getDouble("settings.Spawns." + team + ".Z");
		double yaw = cfg.getDouble("settings.Spawns." + team + ".Yaw");
		double pitch = cfg.getDouble("settings.Spawns." + team + ".Pitch");
		String worldName = cfg.getString("settings.Spawns." + team + ".World");
		World world = Bukkit.getWorld(worldName);
		
		loc.setWorld(world);
		loc.setX(x);
		loc.setY(y);
		loc.setZ(z);
		loc.setYaw((float) yaw);
		loc.setPitch((float) pitch);
		
		p.teleport(loc);
	}
	
	public static void giveKit(Player p) {
		//TODO
	}
	
	public static void startCountdown() {
		counter = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
			@Override
			public void run() {
				if(Bukkit.getOnlinePlayers().size() > 6) {
					if(Countdown == 60) {
						Bukkit.broadcastMessage("$4[Firefight] §8Das Spiel startet in 60 Sekunden");
					}
					else if(Countdown == 30) {
						Bukkit.broadcastMessage("$4[Firefight] §8Das Spiel startet in 30 Sekunden");
					}
					else if(Countdown == 20) {
						Bukkit.broadcastMessage("$4[Firefight] §8Das Spiel startet in 20 Sekunden");
					}
					else if(Countdown == 15) {
						Bukkit.broadcastMessage("$4[Firefight] §8Das Spiel startet in 15 Sekunden");
					}
					else if(Countdown == 10) {
						Bukkit.broadcastMessage("$4[Firefight] §8Das Spiel startet in 10 Sekunden");
					}
					else if(Countdown == 9) {
						Bukkit.broadcastMessage("$4[Firefight] §8Das Spiel startet in 9 Sekunden");
					}
					else if(Countdown == 8) {
						Bukkit.broadcastMessage("$4[Firefight] §8Das Spiel startet in 8 Sekunden");
					}
					else if(Countdown == 7) {
						Bukkit.broadcastMessage("$4[Firefight] §8Das Spiel startet in 7 Sekunden");
					}
					else if(Countdown == 6) {
						Bukkit.broadcastMessage("$4[Firefight] §8Das Spiel startet in 6 Sekunden");
					}
					else if(Countdown == 5) {
						Bukkit.broadcastMessage("$4[Firefight] §8Das Spiel startet in 5 Sekunden");
					}
					else if(Countdown == 4) {
						Bukkit.broadcastMessage("$4[Firefight] §8Das Spiel startet in 4 Sekunden");
					}
					else if(Countdown == 3) {
						Bukkit.broadcastMessage("$4[Firefight] §8Das Spiel startet in 3 Sekunden");
					}
					else if(Countdown == 2) {
						Bukkit.broadcastMessage("$4[Firefight] §8Das Spiel startet in 2 Sekunden");
					}
					else if(Countdown == 1) {
						Bukkit.broadcastMessage("$4[Firefight] §8Das Spiel startet in 1 Sekunden");
					}
					else if(Countdown == 0) {
						Bukkit.broadcastMessage("$4[Firefight] §8Das Spiel startet jetzt");
					}
					if(Countdown != 0) {
						Countdown--;
					}
					else {
						Bukkit.getScheduler().cancelTask(counter);
						Main.gameInProgress = true;
						Methods.startGame();
					}
				}
				else {
					Countdown = plugin.getConfig().getInt("settings.game.Countdown");
					Bukkit.getScheduler().cancelTask(counter);
					Main.countdownStarted = false;
				}
			}
		}, 0, 20);
	}
	
	public static void startCheckingArea() {
		FileConfiguration cfg = plugin.getConfig();
		double xa = cfg.getInt("settings.area.xa");
		double xb = cfg.getInt("settings.area.xb");
		double za = cfg.getInt("settings.area.za");
		double zb = cfg.getInt("settings.area.zb");
		
		double minX;
		double maxX;
		double minZ;
		double maxZ;
		
		if(xa > xb) {
			minX = xb;
			maxX = xa;
		} else {
			minX = xa; 
			maxX = xb;
		}
		
		if(za > zb) {
			minZ = zb; 
			maxZ = za;
		} else {
			minZ = za; 
			maxZ = zb;
		}
		
		
		checker = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
			
			@Override
			public void run() {
				Collection<? extends Player> cplayers = Bukkit.getOnlinePlayers();
				Player[] players = (Player[]) cplayers.toArray(new Player[0]);
				
				String dom = "none";
				int r = 0;
				int b = 0;
				
				for(int i = 0; i < players.length; i++) {
					Player z = players[i];
					Location loc = z.getLocation();
					if(loc.getX()>=minX && loc.getX()<=maxX+1 && loc.getZ()>=minZ && loc.getZ()<=maxZ+1) {
						//Main.inRegion.put(z, true);
						if(Main.teams.get(z).equals("red")) r++;
						if(Main.teams.get(z).equals("blue")) b++;
					} //else {
						//Main.inRegion.put(z, false);
					//}
				}
				
				//String dom = Methods.checkDomination();
				//if (!dom.equals(domOld)) Methods.changeArea(dom);
				
				
				if (r == 0 && b == 0) {
					dom = "none"; 
				} else if (r > 0 && b == 0) {
					dom = "red"; 
				} else if (r == 0 && b > 0) {
					dom = "blue"; 
				} else if (r > 0 && b > 0) {
					dom = "none";
				}
				
				Main.dominating = dom;
				if (!dom.equals(domOld)) {
					Methods.changeArea(dom);
					domOld = dom;
				}
			}
			
		}, 0, 1);
	}
	
	/*public static String checkDomination() {
		Collection<? extends Player> cplayers = Bukkit.getOnlinePlayers();
		Player[] players = (Player[]) cplayers.toArray(new Player[0]);
		int r = 0;
		int b = 0;
		
		for(int i = 0; i < players.length; i++) {
			Player z = players[i];
			if (Main.inRegion.get(z)) {
				if(Main.teams.get(z).equals("red")) {
					r++;
				} else if(Main.teams.get(z).equals("blue")) {
					b++;
				}
			}
		}
		
		
		if (r == 0 && b == 0) return "none"; System.out.println("none");
		if (r > 0 && b == 0) return "red"; System.out.println("red");
		if (r == 0 && b > 0) return "blue"; System.out.println("blue");
		return "none"; 
	}
	*/
	
	@SuppressWarnings("deprecation")
	public static void changeArea(String team) {
		FileConfiguration cfg = plugin.getConfig();
		double xa = cfg.getInt("settings.area.xa");
		double xb = cfg.getInt("settings.area.xb");
		double za = cfg.getInt("settings.area.za");
		double zb = cfg.getInt("settings.area.zb");
		double ya = cfg.getInt("settings.area.ya");
		double yb = cfg.getInt("settings.area.yb");
		
		double minX;
		double maxX;
		double minZ;
		double maxZ;
		double minY;
		double maxY;
		
		if(xa > xb) {
			minX = xb; 
			maxX = xa;
		} else {
			minX = xa; 
			maxX = xb;
		}
		
		if(ya > yb) {
			minY = yb; 
			maxY = ya;
		} else {
			minY = ya; 
			maxY = yb;
		}
		
		if(za > zb) {
			minZ = zb; 
			maxZ = za;
		} else {
			minZ = za; 
			maxZ = zb;
		}
		World w = Bukkit.getWorld(cfg.getString("settings.area.world"));
		
		Location min = new Location(w, minX, minY, minZ);
		Location max = new Location(w, maxX, maxY, maxZ);
		
		for(int x = min.getBlockX(); x <= max.getBlockX(); x++) {
			for(int y = min.getBlockY(); y <= max.getBlockY(); y++){
				for(int z = min.getBlockZ(); z <= max.getBlockZ(); z++) {
					Location loc = new Location(w, x, y, z);
					if (loc.getBlock().getType().equals(Material.CARPET)) {
						if(team.equals("red")) loc.getBlock().setTypeIdAndData(171, DyeColor.RED.getData(), true);
						if(team.equals("blue")) loc.getBlock().setTypeIdAndData(171, DyeColor.BLUE.getData(), true);
						if(team.equals("none")) loc.getBlock().setTypeIdAndData(171, DyeColor.YELLOW.getData(), true);
						
					}
				}
			}
		}
	}
	
	public static void startCountingPoints() {
		
	}
}

