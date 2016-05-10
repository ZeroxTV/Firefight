package me.ZeroxTV.Firefight;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;

import com.shampaggon.crackshot.CSUtility;
import com.shampaggon.crackshot.events.WeaponScopeEvent;

import net.md_5.bungee.api.ChatColor;

public class EventListener implements Listener {
	public static Team red,blue;
	public static Scoreboard board;
	
	public EventListener(Main main) {
		main.getServer().getPluginManager().registerEvents(this, main);
		
		ScoreboardManager manager = main.getServer().getScoreboardManager();
		board = manager.getNewScoreboard();
		red = board.registerNewTeam("Terroristen");
		blue = board.registerNewTeam("Spezialeinheit");
		
		red.setAllowFriendlyFire(false);
		red.setCanSeeFriendlyInvisibles(true);
		red.setPrefix("§c[T] ");
		
		blue.setAllowFriendlyFire(false);
		blue.setCanSeeFriendlyInvisibles(true);
		blue.setPrefix("§9[S] ");
	}


	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e) {
		e.getPlayer().setScoreboard(board);
		Methods.assignTeam(e.getPlayer());
		Main.klasse.put(e.getPlayer(), "funker");
		Main.weapon.put(e.getPlayer(), "rifle1");
		e.getPlayer().getInventory().clear();
		e.getPlayer().getInventory().setItem(4, Methods.selector());
		if(Bukkit.getOnlinePlayers().size() >= 6 && !Main.countdownStarted) {
			Methods.startCountdown();
			Main.countdownStarted = true;
		}
		e.setJoinMessage("§8" + e.getPlayer().getName() + " hat das Spiel betreten.");
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent e) {
		try {
		blue.removePlayer(e.getPlayer());
		} catch (Exception ex){}
		try {
		red.removePlayer(e.getPlayer());
		} catch (Exception ex){}
		Main.teams.remove(e.getPlayer());
	}
	
	@EventHandler
	public void onChat(AsyncPlayerChatEvent e) {
		if(e.getPlayer().hasPermission("firefight.color")) {
			e.setMessage(ChatColor.translateAlternateColorCodes('&', e.getMessage()));
		}
		
		
		
		if(Main.gameInProgress) {
			
			if(Main.teams.get(e.getPlayer()).equals("blue")) {
				e.setFormat("§9%1$s:§f %2$s");
				Methods.sendTeamMessage(Main.teams.get(e.getPlayer()), e.getMessage());
			}
			
			
			else if(Main.teams.get(e.getPlayer().getName()).equals("red")) {
				e.setFormat("§c%1$s:§f %2$s");
				Methods.sendTeamMessage(Main.teams.get(e.getPlayer()), e.getMessage());
			}
			
			e.setCancelled(true);
		} else {
			if(Main.teams.get(e.getPlayer()) != null) {
				if(Main.teams.get(e.getPlayer()).equals("blue")) {
					e.setFormat("§9%1$s:§f %2$s");
						}
				else if(Main.teams.get(e.getPlayer()).equals("red")) {
					e.setFormat("§c%1$s:§f %2$s");
				}
			}
		}
	}
	
	@EventHandler
	public void onClickSelector(PlayerInteractEvent e) {
		if (e.getItem() != null && e.getItem().equals(Methods.selector())) {
			e.getPlayer().openInventory(MenuItems.mainMenu());
		} else return;
	}
	
	@EventHandler
	public void onMenuClick(InventoryClickEvent e) {
		if(e.getInventory().getName().equals("Ausrüstung bearbeiten") || e.getInventory().getName().equals("Wähle deine Waffe")) {
			Player p = (Player) e.getWhoClicked();
			ItemStack item = e.getCurrentItem();
			if (e.getInventory().getName().equals("Ausrüstung bearbeiten")) {
				if (item != null) {
					if (item.equals(MenuItems.menuItem("primary"))) p.openInventory(MenuItems.subMenu("primary"));
					if (item.equals(MenuItems.menuItem("secondary"))) p.openInventory(MenuItems.subMenu("secondary"));
				}
			} else if (e.getInventory().getName().equals("Wähle deine Waffe")) {
				CSUtility cs = new CSUtility();
				Main.weapon.put(p, cs.getWeaponTitle(item));
				p.sendMessage(Main.pre + "Du hast die " + cs.getWeaponTitle(item) +  " ausgewählt.");
				p.closeInventory();
			}
			
			e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onZoom(WeaponScopeEvent e) {
		if (Main.sniper.contains(e.getWeaponTitle())) {
			if (e.getPlayer().getInventory().getHelmet() == null) {
				e.getPlayer().getInventory().setHelmet(new ItemStack(Material.PUMPKIN));
			} else {
				e.getPlayer().getInventory().setHelmet(null);
			}
		}
	}
}
