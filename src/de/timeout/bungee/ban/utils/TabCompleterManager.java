package de.timeout.bungee.ban.utils;

import java.util.ArrayList;

import de.timeout.bungee.ban.BanSystem;
import de.timeout.utils.BungeeSQLManager;
import de.timeout.utils.Reflections;
import net.md_5.bungee.api.event.TabCompleteEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class TabCompleterManager implements Listener {
	
	private BanSystem main = BanSystem.plugin;

	@EventHandler
	public void onBanTabComplete(TabCompleteEvent event) {
		String message = event.getCursor();
		try {
			String[] split = message.substring(1).split(" ");
			if(split[0].equalsIgnoreCase("ban") || split[0].equalsIgnoreCase("mute")) {
				ArrayList<String> list = new ArrayList<String>();
				main.getProxy().getPlayers().forEach(player -> {
					list.add(player.getName());
				});
				
				String suggest = split[split.length -1];
				for(int i = 0; i < list.size(); i++) {
					String s = list.get(i);
					if(s.toLowerCase().startsWith(suggest.toLowerCase())) {
						String first = list.get(0);
						list.set(0, s);
						list.set(i, first);
						break;
					}
				}
				Reflections.setField(event, "suggestions", list);
			}
		} catch(ArrayIndexOutOfBoundsException e ) {}
	}
	
	@EventHandler
	public void onUnbanTabComplete(TabCompleteEvent event) {
		String message = event.getCursor();
		try {
			String[] split = message.substring(1).split(" ");
			if(split[0].equalsIgnoreCase("unban") || split[0].equalsIgnoreCase("unmute")) {
				ArrayList<String> list = split[0].equalsIgnoreCase("unban") ? BungeeSQLManager.getBannedPlayers() : BungeeSQLManager.getMutedPlayers();
				
				String suggest = split[split.length -1];
				for(int i = 0; i < list.size(); i++) {
					String s = list.get(i);
					if(s.toLowerCase().startsWith(suggest.toLowerCase())) {
						String first = list.get(0);
						list.set(0, s);
						list.set(i, first);
						break;
					}
				}
				Reflections.setField(event, "suggestions", list);
			}
		} catch(ArrayIndexOutOfBoundsException e) {}
	}
}
