package me.kakalavala.trickortreat.core;

import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import me.kakalavala.trickortreat.assets.DoorsYaml;
import me.kakalavala.trickortreat.commands.Command_TrickOrTreat;
import me.kakalavala.trickortreat.listeners.KnockListener;
import net.md_5.bungee.api.ChatColor;

public class TCore extends JavaPlugin {
	
	public final Logger log = Logger.getLogger("TrickOrTreat");
	public final PluginDescriptionFile pf = this.getDescription();
	public final PluginManager pm = Bukkit.getPluginManager();
	
	public final DoorsYaml door = new DoorsYaml(this);
	
	public void onEnable() {
		this.getConfig().options().copyDefaults(true);
		this.saveConfig();
		
		this.registerCommands();
		this.registerListeners();
	}
	
	public void onDisable() {
		this.getServer().getServicesManager().unregisterAll(this);
	}
	
	public void announce(String msg) {
		this.log.info(String.format("[%s] %s", pf.getName(), msg));
	}
	
	public void sendMessage(CommandSender sender, String msg) {
		sender.sendMessage(String.format("%s %s", this.getPrefix(), msg));
	}
	
	public void sendMessage(CommandSender sender, String[] msg) {
		sender.sendMessage(msg);
	}
	
	private void registerCommands() {
		this.getCommand("trickortreat").setExecutor(new Command_TrickOrTreat(this));
		
		this.announce("Registered commands.");
	}
	
	private void registerListeners() {
		pm.registerEvents(new KnockListener(this), this);
		
		this.announce("Registered listeners.");
	}
	
	public String getPrefix() {
		this.reloadConfig();
		return ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("prefix"));
	}
	
	public boolean useMessages() {
		this.reloadConfig();
		return this.getConfig().getBoolean("messages.enabled");
	}
	
	public String getAlreadyClaimedMessage() {
		this.reloadConfig();
		return ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("messages.alreadyClaimed"));
	}
	
	public boolean useSound() {
		this.reloadConfig();
		return this.getConfig().getBoolean("playsound.enabled");
	}
	
	public Sound getSound() {
		this.reloadConfig();
		
		final String name = this.getConfig().getString("playsound.sound");
		Sound snd = null;
		
		for (Sound s : Sound.values()) {
			if (name.equalsIgnoreCase(s.name())) {
				snd = s;
				break;
			}
		}
		
		return snd;
	}
	
	public float getVolume() {
		this.reloadConfig();
		return (float) this.getConfig().getDouble("playsound.volume");
	}
	
	public float getPitch() {
		this.reloadConfig();
		return (float) this.getConfig().getDouble("playsound.pitch");
	}

}
