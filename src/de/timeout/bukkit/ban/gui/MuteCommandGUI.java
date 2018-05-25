package de.timeout.bukkit.ban.gui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

import de.timeout.bukkit.ban.BanGUI;
import de.timeout.bukkit.ban.utils.BukkitReason;
import de.timeout.bukkit.ban.utils.ItemStackAPI;
import de.timeout.utils.BukkitSQLManager;

public class MuteCommandGUI implements Listener {

	private static BanGUI main = BanGUI.plugin;
	
	private static String inventoryname = main.getLanguage("gui.mute.inventory");
	private static String customname = main.getLanguage("gui.mute.custom");
	private static String banname = main.getLanguage("gui.mute.ban");
	private static String daysname = main.getLanguage("gui.mute.days");
	private static String hoursname = main.getLanguage("gui.mute.hours");
	private static String minutesname = main.getLanguage("gui.mute.minutes");
	private static String confirmname = main.getLanguage("gui.mute.confirm");
	private static String permaname = main.getLanguage("gui.mute.perma");
	
	private static HashMap<Player, String> openInvs = new HashMap<Player, String>();
	
	public static void openMuteMenu(Player p, String name) {
		List<BukkitReason> reasons = BukkitSQLManager.getMuteReasons();
		Inventory inv = Bukkit.createInventory(null, reasons.size()/9+27, inventoryname.replace("[name]", name));
		
		for(int i = 0; i < reasons.size(); i++)inv.setItem(i, reasons.get(i).getTitle());
		
		int linestart = (reasons.size() / 9 +1)*9;
		ItemStack n = ItemStackAPI.getItemStack(160, (short) 7, 1, "§5");
		for(int i = linestart; i < linestart+9; i++) inv.setItem(i, n);
		
		ItemStack custom = ItemStackAPI.createItemStack(Material.BOOK_AND_QUILL, 1, "§cCustom-Mute");
		ItemStack mute = ItemStackAPI.createItemStack(Material.SIGN, 1, "§7Zum §6Ban");
		inv.setItem(linestart + 10, mute);
		inv.setItem(linestart + 16, custom);
		
		p.openInventory(inv);
		openInvs.put(p, name);
	}
	
	@EventHandler
	public void onMenuClick(InventoryClickEvent event) {
		if(event.getWhoClicked() instanceof Player) {
			Player p = (Player) event.getWhoClicked();
			try {
				if(openInvs.containsKey(p)) {
					if(event.getClickedInventory().getName().equalsIgnoreCase(customname)) {
						Inventory inv = event.getClickedInventory();
						event.setCancelled(true);
						ItemStack item = event.getCurrentItem();
						if(item.getItemMeta().getDisplayName().equalsIgnoreCase("§a+")) {
							ItemStack time = inv.getItem(event.getSlot() +9);
							
							String value = time.getItemMeta().getLore().get(0).substring(2);
							try {
								int number = Integer.parseInt(value);
								if(number+1 < 60) {
									number++;
									ItemMeta meta = time.getItemMeta();
									String s = "§5" + number;
									List<String> timeLore = new ArrayList<String>();
									timeLore.add(s);
									meta.setLore(timeLore);
									time.setItemMeta(meta);
									inv.setItem(event.getSlot() +9, time);
									
									ItemStack confirm = inv.getItem(34);
									ItemMeta confirmMeta = confirm.getItemMeta();
									List<String> lore = new ArrayList<String>();
									lore.add(inv.getItem(19).getItemMeta().getLore().get(0) + daysname);
									lore.add(inv.getItem(21).getItemMeta().getLore().get(0) + hoursname);
									lore.add(inv.getItem(23).getItemMeta().getLore().get(0) + minutesname);
									confirmMeta.setLore(lore);
									confirm.setItemMeta(confirmMeta);
									inv.setItem(34, confirm);
									
									p.updateInventory();
								} else p.playSound(p.getLocation(), Sound.NOTE_BASS, 1F, 1F);
							} catch(NumberFormatException e) {e.printStackTrace();}
						} else if(item.getItemMeta().getDisplayName().equalsIgnoreCase("§c-")) {
							ItemStack time = inv.getItem(event.getSlot() -9);
							
							String value = time.getItemMeta().getLore().get(0).substring(2);
							try {
								int number = Integer.parseInt(value);
								if(number-1 > -1) {
									number--;
									ItemMeta meta = time.getItemMeta();
									value = "§5" + number;
									List<String> timeLore = new ArrayList<String>();
									timeLore.add(value);
									meta.setLore(timeLore);
									time.setItemMeta(meta);
									inv.setItem(event.getSlot()-9, time);
									
									ItemStack confirm = inv.getItem(34);
									ItemMeta confirmMeta = confirm.getItemMeta();
									List<String> lore = new ArrayList<String>();
									lore.add(inv.getItem(19).getItemMeta().getLore().get(0) + daysname);
									lore.add(inv.getItem(21).getItemMeta().getLore().get(0) + hoursname);
									lore.add(inv.getItem(23).getItemMeta().getLore().get(0) + minutesname);
									confirmMeta.setLore(lore);
									confirm.setItemMeta(confirmMeta);
									inv.setItem(34, confirm);
									
									p.updateInventory();
								}
							} catch(NumberFormatException e) {}
						} else if(event.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(confirmname)) {
							if(Integer.valueOf(inv.getItem(19).getItemMeta().getLore().get(0).substring(2)) + 
									Integer.valueOf(inv.getItem(21).getItemMeta().getLore().get(0).substring(2)) + 
									Integer.valueOf(inv.getItem(23).getItemMeta().getLore().get(0).substring(2)) > 0) {
								int days = Integer.valueOf(inv.getItem(19).getItemMeta().getLore().get(0).substring(2));
								int hours = Integer.valueOf(inv.getItem(21).getItemMeta().getLore().get(0).substring(2));
								int minutes = Integer.valueOf(inv.getItem(23).getItemMeta().getLore().get(0).substring(2));
								
								ByteArrayDataOutput out = ByteStreams.newDataOutput();
								out.writeUTF("CustomMute");
								out.writeUTF(openInvs.get(p));
								out.writeLong(days);
								out.writeLong(hours);
								out.writeLong(minutes);
								out.writeUTF(p.getDisplayName());
								
								p.sendPluginMessage(main, "BanSystem", out.toByteArray());
								p.closeInventory();
								openInvs.remove(p);
							} else p.playSound(p.getLocation(), Sound.NOTE_BASS, 1F, 1F);
						} else if(event.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(permaname)) {
							if(p.hasPermission("easyban.permamute")) {
								ByteArrayDataOutput out = ByteStreams.newDataOutput();
								out.writeUTF("PermaMute");
								out.writeUTF(openInvs.get(p));
								out.writeUTF(p.getDisplayName());
								
								p.sendPluginMessage(main, "BanSystem", out.toByteArray());
								p.closeInventory();
								openInvs.remove(p);
							} else p.playSound(p.getLocation(), Sound.NOTE_BASS, 1F, 1F);
						}
					} else {
						event.setCancelled(true);
						String name = openInvs.get(p);
						if(event.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(customname)) {
							if(p.hasPermission("easyban.custommute")) {
								Inventory inv = Bukkit.createInventory(null, 9*5, customname);
								ItemStack n = ItemStackAPI.getItemStack(160, (short) 7, 1, "§5");
								for(int i = 0; i < inv.getSize(); i++)inv.setItem(i, n);
								
								ItemStack plus = ItemStackAPI.getItemStack(160, (short) 5, 1, "§a+");
								ItemStack minus = ItemStackAPI.getItemStack(160, (short) 14, 1, "§c-");
								
								ItemStack minutes = ItemStackAPI.createItemStack(Material.PAPER, 1, "§7" + minutesname.substring(3));
								ItemStack hours = ItemStackAPI.createItemStack(Material.PAPER, 1, "§7" + hoursname.substring(3));
								ItemStack days = ItemStackAPI.createItemStack(Material.PAPER, 1, "§7" + daysname.substring(3));
								
								ItemStack perma = ItemStackAPI.createItemStack(Material.NETHER_STAR, 1, permaname);
								ItemStack confirm = ItemStackAPI.createItemStack(Material.BOOK, 1, confirmname);
								ItemStackAPI.enchantItem(confirm, Enchantment.DURABILITY, 1, true);
								
								//substring 2 für aktuelle Zahl
								ItemStackAPI.setLore(minutes, "§51");
								ItemStackAPI.setLore(hours, "§50");
								ItemStackAPI.setLore(days, "§50");
								
								List<String> confirmlore = new ArrayList<String>();
								confirmlore.add("§50" + daysname);
								confirmlore.add("§50" + hoursname);
								confirmlore.add("§51" + minutesname);
								ItemStackAPI.setLore(confirm, confirmlore);
								
								inv.setItem(10, plus);
								inv.setItem(12, plus);
								inv.setItem(14, plus);
								inv.setItem(16, perma);
								
								inv.setItem(19, days);
								inv.setItem(21, hours);
								inv.setItem(23, minutes);
								
								inv.setItem(28, minus);
								inv.setItem(30, minus);
								inv.setItem(32, minus);
								inv.setItem(34, confirm);
								
								p.openInventory(inv);
							} else p.playSound(p.getLocation(), Sound.NOTE_BASS, 1F, 1F);
						} else if(event.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(banname)) {
							BanCommandGUI.openBanMenu(p, openInvs.get(p));
							openInvs.remove(p);
						} else {
							ItemStack reason = event.getCurrentItem();
							
							ByteArrayDataOutput out = ByteStreams.newDataOutput();
							out.writeUTF("Mute");
							out.writeUTF(name);
							out.writeUTF(BukkitSQLManager.getNameByItemStack(reason, "Mute"));
							out.writeUTF(p.getDisplayName());
							
							p.sendPluginMessage(main, "BanSystem", out.toByteArray());
							p.closeInventory();
							openInvs.remove(p);
						}
					}
				}
			} catch(NullPointerException e) {}
		}
	}
	
	@EventHandler
	public void onMenuDrop(PlayerDropItemEvent event) {
		event.setCancelled(openInvs.containsKey(event.getPlayer()));
	}
}
