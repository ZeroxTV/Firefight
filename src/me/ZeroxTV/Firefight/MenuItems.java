package me.ZeroxTV.Firefight;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.shampaggon.crackshot.CSUtility;

public class MenuItems {
	
	public static Inventory mainMenu() {
		
		Inventory menu = Bukkit.createInventory(null, 54, "Ausrüstung bearbeiten");
		
		menu.setItem(10, MenuItems.menuItem("primary"));
		menu.setItem(16, MenuItems.menuItem("secondary"));
		
		return menu;
	}
	
	public static Inventory subMenu(String s) {
		CSUtility cs = new CSUtility();
		Inventory menu = null;
		if(s.equals("primary")) {
			menu = Bukkit.createInventory(null, 54, "Primärwaffe wählen");
			
			for (int i = 0; i < Main.primary.size(); i++) {
				menu.setItem(i, cs.generateWeapon((String) Main.primary.toArray()[i]));
			}
		}
		else if(s.equals("secondary")) {
			menu = Bukkit.createInventory(null, 54, "Sekundärwaffe wählen");
			
			for (int i = 0; i < Main.secondary.size(); i++) {
				menu.setItem(i, cs.generateWeapon((String) Main.secondary.toArray()[i]));
			}
		}
		
		return menu;
	}
	
	@SuppressWarnings("unused")
	public static Inventory choose(String weapon) {
		CSUtility cs = new CSUtility();
		Inventory menu = Bukkit.createInventory(null, 9, "Wähle deine Waffe");
		
		ItemStack a = null;
		ItemStack b = null;
		ItemStack c = null;
		ItemStack d = null;
		ItemStack e = null;
		ItemStack f = null;
		ItemStack g = null;
		ItemStack h = null;
		ItemStack i = null;
		
		
		if(weapon.equals("rifle")) {
		}
		else if(weapon.equals("machine")) {
			a = cs.generateWeapon("MP5");
		}
		else if(weapon.equals("sniper")) {
			a = cs.generateWeapon("Dragunov");
		}
		else //if(weapon.equals("shotgun")) 
			{
		}
		
		if (a != null) menu.setItem(0, a);
		if (b != null) menu.setItem(1, b);
		if (c != null) menu.setItem(2, c);
		if (d != null) menu.setItem(3, d);
		if (e != null) menu.setItem(4, e);
		if (f != null) menu.setItem(5, f);
		if (g != null) menu.setItem(6, g);
		if (h != null) menu.setItem(7, h);
		if (i != null) menu.setItem(8, i);
		
		return menu;
	}
	
	
	public static ItemStack menuItem(String s) {
		ItemStack st = null;
		ItemMeta stm;
		
		if(s.equals("primary")) {
			st = new ItemStack(Material.IRON_AXE);
			stm = st.getItemMeta();
			stm.setDisplayName("Primärwaffe");
			st.setItemMeta(stm);
		} else if(s.equals("secondary")) {
			st = new ItemStack(Material.IRON_SPADE);
			stm = st.getItemMeta();
			stm.setDisplayName("Sekundärwaffe");
			st.setItemMeta(stm);
		}
		return st;
	}
	
	public static ItemStack tierGun(String w, int t) {
		ItemStack gun = null;
		if (w.equals("rifle")) gun = new ItemStack(Material.IRON_PICKAXE);
		if (w.equals("machine")) gun = new ItemStack(Material.IRON_AXE);
		if (w.equals("sniper")) gun = new ItemStack(Material.IRON_HOE);
		
		ItemMeta meta = gun.getItemMeta();
		if (t==1) meta.setDisplayName("§a[Tier 1]");
		if (t==2) meta.setDisplayName("§6[Tier 2]");
		if (t==3) meta.setDisplayName("§b[Tier 1]");
		
		gun.setItemMeta(meta);
		return gun;
	}
}
