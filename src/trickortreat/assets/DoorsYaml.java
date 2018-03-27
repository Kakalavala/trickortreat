package me.kakalavala.trickortreat.assets;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import me.kakalavala.trickortreat.core.TCore;

public class DoorsYaml {
	
	public File doorsYml;
	public FileConfiguration doorsCfg;
	
	private TCore core;
	
	public DoorsYaml(TCore core) {
		this.core = core;
		this.doorsYml = new File(core.getDataFolder() + "/doors.yml");
		this.doorsCfg = YamlConfiguration.loadConfiguration(this.doorsYml);
	}
	
	public boolean registerDoor(Location loc, List<String> cmds) {
		String cmdsData = "";
		
		for (String c : cmds)
			cmdsData += String.format("%s ", c);
		
		cmdsData = cmdsData.trim();
		
		final String data = String.format("%s[%s[%s[%s", loc.getWorld().getName(), loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
		final List<String> doors = this.getDoors();
		
		if (!this.doorExists(loc)) {
			doors.add(data);
			
			this.doorsCfg.set("DoorLocations", doors);
			this.doorsCfg.set(data, cmdsData);
			
			this.save();
			return true;
		} else return false;
	}
	
	public boolean removeDoor(Location loc) {
		final String data = String.format("%s[%s[%s[%s", loc.getWorld().getName(), loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
		final List<String> doors = this.getDoors();
		PlayerYaml pYml;
		
		if (this.doorExists(loc)) {
			doors.remove(data);
			
			for (Player ply : Bukkit.getOnlinePlayers()) {
				pYml = new PlayerYaml(core, ply);				
				pYml.removeClaimed(ply, loc);
			}
			
			for (OfflinePlayer ply : Bukkit.getOfflinePlayers()) {
				pYml = new PlayerYaml(core, ply);				
				pYml.removeClaimed(ply, loc);
			}
			
			this.doorsCfg.set("DoorLocations", doors);
			this.doorsCfg.set(data, null);
			
			this.save();
			return true;
		} else return false;
	}
	
	public List<String> getCommands(Location loc) {
		this.doorsYml = new File(core.getDataFolder() + "/doors.yml");
		this.doorsCfg = YamlConfiguration.loadConfiguration(this.doorsYml);
		
		final String data = String.format("%s[%s[%s[%s", loc.getWorld().getName(), loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
		final List<String> rCmds = new ArrayList<String>();
		
		if (this.doorExists(loc)) {
			final String allCmds = this.doorsCfg.getString(data);
			String[] cmds = allCmds.split("\\|");
			
			for (String c : cmds)
				rCmds.add(c);
		}
		
		return rCmds;
	}
	
	public List<String> getDoors() {
		this.doorsYml = new File(core.getDataFolder() + "/doors.yml");
		this.doorsCfg = YamlConfiguration.loadConfiguration(this.doorsYml);
		
		return this.doorsCfg.getStringList("DoorLocations");
	}
	
	public boolean doorExists(Location loc) {
		final String data = String.format("%s[%s[%s[%s", loc.getWorld().getName(), loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
		final List<String> doors = this.getDoors();
		
		boolean exists = false;
		
		for (String d : doors) {
			if (d.equalsIgnoreCase(data)) {
				exists = true;
				break;
			}
		}
		
		return exists;
	}
	
	public void save() {
		try {
			this.doorsCfg.save(this.doorsYml);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
