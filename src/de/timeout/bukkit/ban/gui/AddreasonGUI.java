package de.timeout.bukkit.ban.gui;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import de.timeout.bukkit.ban.BanGUI;
import de.timeout.bukkit.ban.api.AddReasonEvent;
import de.timeout.bukkit.ban.utils.AnvilGUI;
import de.timeout.bukkit.ban.utils.ItemStackAPI;
import de.timeout.bukkit.ban.utils.AnvilGUI.AnvilClickEvent;
import de.timeout.bukkit.ban.utils.AnvilGUI.AnvilClickEventHandler;
import de.timeout.bukkit.ban.utils.AnvilGUI.AnvilSlot;
import de.timeout.bukkit.ban.utils.BukkitReason;
import de.timeout.utils.DateConverter;
import de.timeout.utils.Reason.ReasonType;

public class AddreasonGUI implements Listener {
	
	private static BanGUI main = BanGUI.plugin;
	
	private static String reasonInv = main.getLanguage("gui.reason.inventory");
	private static String time1name = main.getLanguage("gui.reason.time1");
	private static String time2name = main.getLanguage("gui.reason.time2");
	private static String time3name = main.getLanguage("gui.reason.time3");
	private static String mutename = main.getLanguage("gui.reason.mute");
	private static String banname = main.getLanguage("gui.reason.ban");
	private static String line12name = main.getLanguage("gui.reason.line12");
	private static String line23name = main.getLanguage("gui.reason.line23");
	private static String timemenuname = main.getLanguage("gui.reason.timemenu");
	private static String daysname = main.getLanguage("gui.reason.days");
	private static String hoursname = main.getLanguage("gui.reason.hours");
	private static String minutesname = main.getLanguage("gui.reason.minutes");
	private static String permaname = main.getLanguage("gui.reason.perma");
	private static String backname = main.getLanguage("gui.reason.back");
	private static String pointsraisename = main.getLanguage("gui.reason.pointsraise");
	
	private static HashMap<Player, String > nameCache = new HashMap<Player, String>();
	private static HashMap<Player, String> displayCache = new HashMap<Player, String>();
	private static HashMap<Player, Long> time1Cache = new HashMap<Player, Long>();
	private static HashMap<Player, Long> time2Cache = new HashMap<Player, Long>();
	private static HashMap<Player, Long> time3Cache = new HashMap<Player, Long>();
	private static HashMap<Player, Long> line1Cache = new HashMap<Player, Long>();
	private static HashMap<Player, Long> line2Cache = new HashMap<Player, Long>();
	private static HashMap<Player, ReasonType> typeCache = new HashMap<Player, ReasonType>();
	
	private static HashMap<Player, Integer> slotCache = new HashMap<Player, Integer>();
	private static HashMap<Player, Integer> daysCache = new HashMap<Player, Integer>();
	private static HashMap<Player, Integer> hoursCache = new HashMap<Player, Integer>();
	private static HashMap<Player, Integer> minutesCache = new HashMap<Player, Integer>();
	
	
	public static void addReasonGUI(Player player, String name, String display) {
		if(nameCache.containsKey(player))nameCache.replace(player, name);else nameCache.put(player, name);
		if(displayCache.containsKey(player))displayCache.replace(player, display);else displayCache.put(player, display);
		if(time1Cache.containsKey(player))time1Cache.replace(player, 0L);else time1Cache.put(player, 0L);
		if(time2Cache.containsKey(player))time2Cache.replace(player, 0L);else time2Cache.put(player, 0L);
		if(time3Cache.containsKey(player))time3Cache.replace(player, 0L);else time3Cache.put(player, 0L);
		if(line1Cache.containsKey(player))line1Cache.replace(player, 0L);else line1Cache.put(player, 0L);
		if(line2Cache.containsKey(player))line2Cache.replace(player, 0L);else line2Cache.put(player, 0L);
		
		player.openInventory(getMenuGUI(player, name, display));
	}
	
	@EventHandler
	public void onClick(InventoryClickEvent event) {
		if(event.getWhoClicked() instanceof Player) {
			Player p = (Player) event.getWhoClicked();
			try {
				Inventory inv = event.getClickedInventory();
				if(inv.getName().equalsIgnoreCase(reasonInv.replace("[name]", nameCache.get(p)))) {
					event.setCancelled(true);
					ItemStack item = event.getCurrentItem();
					if(event.getSlot() == 22) {
						if(isValid(p)) {
							String name = nameCache.get(p);
							String display = displayCache.get(p);
							long time1 = time1Cache.get(p);
							long time2 = time2Cache.get(p);
							long time3 = time3Cache.get(p);
							long line1 = line1Cache.get(p);
							long line2 = line2Cache.get(p);
							ReasonType type = typeCache.get(p);
							ItemStack title = ItemStackAPI.createItemStack(p.getInventory().getItemInHand().getType() != Material.AIR ? 
									p.getItemInHand().getType() : Material.BARRIER, 1, display);
							
							AnvilGUI gui = new AnvilGUI(p, new AnvilClickEventHandler() {
								
								@Override
								public void onAnvilClick(AnvilClickEvent event) {
									if(event.getSlot() == AnvilSlot.OUTPUT) {
										String result = event.getName();
										try {
											long points = Long.valueOf(result);
											BukkitReason reason = new BukkitReason(name, type, line1, line2, points, time1, time2, time3, display, title);
											AddReasonEvent addEvent = new AddReasonEvent(reason);
											Bukkit.getPluginManager().callEvent(addEvent);
											if(!addEvent.isCancelled()) {
												if(!main.isFileSupportEnabled())
													reason.saveToMySQL();
												else {
													if(main.isBungeeCordEnabled())reason.sendToBungeeCord();
												}
												
												deleteCache(p);
												event.setWillClose(true);
												event.setWillDestroy(true);
											}
										} catch(NumberFormatException e) {}
									}
								}
							});
							gui.setSlot(AnvilSlot.INPUT_LEFT, ItemStackAPI.createItemStack(title.getType(), 1, pointsraisename));
							gui.open();
						} else p.playSound(p.getLocation(), Sound.NOTE_BASS, 1F, 1F);
					} else if(item.getType() == Material.EMPTY_MAP) {
						if(typeCache.containsKey(p))typeCache.put(p, ReasonType.MUTE);
						else typeCache.put(p, ReasonType.MUTE);
						ItemStackAPI.enchantItem(item, Enchantment.DURABILITY, 1, false);
						
						int slot = event.getSlot();
						inv.setItem(slot, item);
						
						ItemStack ban = inv.getItem(5);
						ItemStackAPI.removeEnchantments(ban);
						inv.setItem(5, ban);
						p.updateInventory();
					} else if(item.getType() == Material.BARRIER) {
						if(typeCache.containsKey(p))typeCache.put(p, ReasonType.BAN);
						else typeCache.put(p, ReasonType.BAN);
						ItemStackAPI.enchantItem(item, Enchantment.DURABILITY, 1, false);
						
						int slot = event.getSlot();
						inv.setItem(slot, item);
						
						ItemStack mute = inv.getItem(3);
						ItemStackAPI.removeEnchantments(mute);
						inv.setItem(3, mute);
						p.updateInventory();
					} else if(item.getType() == Material.PAPER) {
						AnvilGUI gui;
						ItemStack nametag; 
						switch(event.getSlot()) {
						case 10:
							gui = new AnvilGUI(p, new AnvilClickEventHandler() {
								
								@Override
								public void onAnvilClick(AnvilClickEvent event) {
									if(event.getSlot() == AnvilSlot.OUTPUT) {
										String result = event.getName();
										try {
											long l = Long.valueOf(result);
											if(l > -1) {
												line1Cache.replace(p, l);
												
												p.openInventory(getMenuGUI(p, nameCache.get(p), displayCache.get(p)));
											}
										} catch(NumberFormatException e) {}
									}
									event.setWillClose(false);
									event.setWillDestroy(false);
								}
							});
							nametag = ItemStackAPI.createItemStack(Material.NAME_TAG, 1, line12name);
							gui.setSlot(AnvilSlot.INPUT_LEFT, nametag);
							gui.open();
							break;
						case 16:
							gui = new AnvilGUI(p, new AnvilClickEventHandler() {
								
								@Override
								public void onAnvilClick(AnvilClickEvent event) {
									if(event.getSlot() == AnvilSlot.OUTPUT) {
										String result = event.getName();
										try {
											long l = Long.valueOf(result);
											if(l > -1) {
												line2Cache.replace(p, l);
												
												p.openInventory(getMenuGUI(p, nameCache.get(p), displayCache.get(p)));
											}
										} catch(NumberFormatException e) {}
									}
									event.setWillClose(false);
									event.setWillDestroy(false);
								}
							});
							nametag = ItemStackAPI.createItemStack(Material.NAME_TAG, 1, line23name);
							gui.setSlot(AnvilSlot.INPUT_LEFT, nametag);
							gui.open();
							break;
						}
					} else if(item.getType() == Material.WOOL) {
						switch(event.getSlot()) {
						case 28:
							if(slotCache.containsKey(p))slotCache.replace(p, 28);
							else slotCache.put(p, 28);
							p.openInventory(getTimeSettingGUI(p, time2Cache));
							break;
						case 34:
							if(slotCache.containsKey(p))slotCache.replace(p, 34);
							else slotCache.put(p, 34);
							p.openInventory(getTimeSettingGUI(p, time3Cache));
							break;
						case 40:
							if(slotCache.containsKey(p))slotCache.replace(p, 40);
							else slotCache.put(p, 40);
							p.openInventory(getTimeSettingGUI(p, time1Cache));
							break;
						}
					}
				} else if(inv.getName().equalsIgnoreCase(timemenuname)) {
					event.setCancelled(true);
					ItemStack item = event.getCurrentItem();
					AnvilGUI gui;
					ItemStack nametag;
					if(item.getItemMeta().getDisplayName().equalsIgnoreCase(daysname)) {
						gui = new AnvilGUI(p, new AnvilClickEventHandler() {
							
							@Override
							public void onAnvilClick(AnvilClickEvent event) {
								if(event.getSlot() == AnvilSlot.OUTPUT) {
									String result = event.getName();
									try {
										int days = Integer.valueOf(result);
										if(days > -1) {
											if(daysCache.containsKey(p)) daysCache.replace(p, days);
											else daysCache.put(p, days);
											
											p.openInventory(getTimeSettingGUI(p, getCache(slotCache.get(p))));
										}
									} catch(NumberFormatException e) {}
								}
								event.setWillClose(false);
								event.setWillDestroy(false);
							}
						});
						nametag = ItemStackAPI.createItemStack(Material.NAME_TAG, 1, daysname);
						gui.setSlot(AnvilSlot.INPUT_LEFT, nametag);
						gui.open();
					} else if(item.getItemMeta().getDisplayName().equalsIgnoreCase(hoursname)) {
						gui = new AnvilGUI(p, new AnvilClickEventHandler() {
							
							@Override
							public void onAnvilClick(AnvilClickEvent event) {
								if(event.getSlot() == AnvilSlot.OUTPUT) {
									String result = event.getName();
									try {
										int hours = Integer.valueOf(result);
										if(hours > -1) {
											if(hoursCache.containsKey(p)) hoursCache.replace(p, hours);
											else hoursCache.put(p, hours);

											p.openInventory(getTimeSettingGUI(p, getCache(slotCache.get(p))));
										}
									} catch(NumberFormatException e) {}
								}
								event.setWillClose(false);
								event.setWillDestroy(false);
							}
						});
						nametag = ItemStackAPI.createItemStack(Material.NAME_TAG, 1, hoursname);
						gui.setSlot(AnvilSlot.INPUT_LEFT, nametag);
						gui.open();
					} else if(item.getItemMeta().getDisplayName().equalsIgnoreCase(minutesname)) {
						gui = new AnvilGUI(p, new AnvilClickEventHandler() {
							
							@Override
							public void onAnvilClick(AnvilClickEvent event) {
								if(event.getSlot() == AnvilSlot.OUTPUT) {
									String result = event.getName();
									try {
										int minutes = Integer.valueOf(result);
										if(minutes > -1) {
											if(minutesCache.containsKey(p)) minutesCache.replace(p, minutes);
											else minutesCache.put(p, minutes);
											
											p.openInventory(getTimeSettingGUI(p, getCache(slotCache.get(p))));
										}
									} catch(NumberFormatException e) {}
								}
								event.setWillClose(false);
								event.setWillDestroy(false);
							}
						});
						nametag = ItemStackAPI.createItemStack(Material.NAME_TAG, 1, minutesname);
						gui.setSlot(AnvilSlot.INPUT_LEFT, nametag);
						gui.open();
					} else if(item.getItemMeta().getDisplayName().equalsIgnoreCase(backname)) {
						int d = daysCache.containsKey(p) ? daysCache.get(p) : 0;
						int h = hoursCache.containsKey(p) ? hoursCache.get(p) : 0;
						int m = minutesCache.containsKey(p) ? minutesCache.get(p) : 0;
						
						long time = DateConverter.getTimeMillis(d, h, m);
						if(time > 0) {
							if(getCache(slotCache.get(p)).containsKey(p)) getCache(slotCache.get(p)).replace(p, time);
							else getCache(slotCache.get(p)).put(p, time);
							p.openInventory(getMenuGUI(p, nameCache.get(p), displayCache.get(p)));
							
							daysCache.remove(p);
							hoursCache.remove(p);
							minutesCache.remove(p);
							slotCache.remove(p);
						} else p.playSound(p.getLocation(), Sound.NOTE_BASS, 1F, 1F);
					} else if(item.getItemMeta().getDisplayName().equalsIgnoreCase(permaname)) {
						if(getCache(slotCache.get(p)).containsKey(p)) getCache(slotCache.get(p)).replace(p, -1L);
						else getCache(slotCache.get(p)).put(p, -1L);
						p.openInventory(getMenuGUI(p, nameCache.get(p), displayCache.get(p)));
						
						daysCache.remove(p);
						hoursCache.remove(p);
						minutesCache.remove(p);
						slotCache.remove(p);
					}
				}
			} catch(NullPointerException e ) {}
		}
	}
	

	private static void deleteCache(Player p) {
		daysCache.remove(p);
		displayCache.remove(p);
		hoursCache.remove(p);
		line1Cache.remove(p);
		line2Cache.remove(p);
		minutesCache.remove(p);
		nameCache.remove(p);
		slotCache.remove(p);
		time1Cache.remove(p);
		time2Cache.remove(p);
		time3Cache.remove(p);
		typeCache.remove(p);
	}
	
	private static Inventory getMenuGUI(Player player, String name, String display) {
		Inventory inv = Bukkit.createInventory(null, 9*5, reasonInv.replace("[name]", name));
		ItemStack n = ItemStackAPI.getItemStack(160, (short) 7, 1, "§5");
		for(int i = 0; i < inv.getSize(); i++)inv.setItem(i, n);
		
		ItemStack time1 = ItemStackAPI.getItemStack(35, (short) 5, 1, time1name);
		ItemStack time2 = ItemStackAPI.getItemStack(35, (short) 1, 1, time2name);
		ItemStack time3 = ItemStackAPI.getItemStack(35, (short) 14, 1, time3name);
		
		ItemStack mute = ItemStackAPI.createItemStack(Material.EMPTY_MAP, 1, mutename);
		ItemStack ban = ItemStackAPI.createItemStack(Material.BARRIER, 1, banname);
		ItemStack confirm = ItemStackAPI.createItemStack(player.getItemInHand().getType() != Material.AIR ?
				player.getInventory().getItemInHand().getType() : Material.BARRIER, 1, display);
		
		ItemStack line12 = ItemStackAPI.createItemStack(Material.PAPER, 1, line12name);
		ItemStack line23 = ItemStackAPI.createItemStack(Material.PAPER, 1, line23name);
		
		ItemStackAPI.setLore(time1, String.valueOf(time1Cache.get(player)));
		ItemStackAPI.setLore(time2, String.valueOf(time2Cache.get(player)));
		ItemStackAPI.setLore(time3, String.valueOf(time3Cache.get(player)));
		ItemStackAPI.setLore(line12, String.valueOf(line1Cache.get(player)));
		ItemStackAPI.setLore(line23, String.valueOf(line2Cache.get(player)));
		
		if(typeCache.containsKey(player)) {
			if(typeCache.get(player) == ReasonType.BAN) ItemStackAPI.enchantItem(ban, Enchantment.DURABILITY, 1, true);
			else ItemStackAPI.enchantItem(mute, Enchantment.DURABILITY, 1, true);
		}
		
		inv.setItem(3, mute);
		inv.setItem(5, ban);
		inv.setItem(10, line12);
		inv.setItem(16, line23);
		inv.setItem(22, confirm);
		inv.setItem(28, time2);
		inv.setItem(34, time3);
		inv.setItem(40, time1);
		
		return inv;
	}
	
	private static Inventory getTimeSettingGUI(Player player, HashMap<Player, Long> cache) {
		int[] time = DateConverter.getDateTime(cache.get(player));
		Inventory timemenu = Bukkit.createInventory(null, 9*2, timemenuname);
		ItemStack n = ItemStackAPI.getItemStack(160, (short) 7, 1, "§5");
		ItemStack days = ItemStackAPI.createItemStack(Material.PAPER, 1, daysname);
		ItemStack hours = ItemStackAPI.createItemStack(Material.PAPER, 1, hoursname);
		ItemStack minutes = ItemStackAPI.createItemStack(Material.PAPER, 1, minutesname);
		ItemStack perma = ItemStackAPI.createItemStack(Material.BARRIER, 1, permaname);
		ItemStack back = ItemStackAPI.createItemStack(Material.BOOK, 1, backname);
		
		ItemStackAPI.setLore(days, daysCache.containsKey(player) ? String.valueOf(daysCache.get(player)) : String.valueOf(time[0]));
		ItemStackAPI.setLore(hours, hoursCache.containsKey(player) ? String.valueOf(hoursCache.get(player)) : String.valueOf(time[1]));
		ItemStackAPI.setLore(minutes, minutesCache.containsKey(player) ? String.valueOf(minutesCache.get(player)) : String.valueOf(time[2]));
		
		for(int i = 0; i < timemenu.getSize(); i++) timemenu.setItem(i, n);
		timemenu.setItem(1, days);
		timemenu.setItem(4, hours);
		timemenu.setItem(7, minutes);
		timemenu.setItem(13, perma);
		timemenu.setItem(17, back);
		
		return timemenu;
	}
	
	private static HashMap<Player, Long> getCache(int slot) {
		switch(slot) {
		case 28:
			return time2Cache;
		case 34:
			return time3Cache;
		case 40:
			return time1Cache;
		default:
			throw new NumberFormatException("Slot cannot be found");
		}
	}
	
	private boolean isValid(Player p) {
		return (nameCache.containsKey(p) && displayCache.containsKey(p) && time1Cache.containsKey(p) && time2Cache.containsKey(p) && time3Cache.containsKey(p)
				&& line1Cache.containsKey(p) && line2Cache.containsKey(p) && typeCache.containsKey(p));
	}
}
