package me.kakalavala.trickortreat.assets;

import java.io.File;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import me.kakalavala.trickortreat.core.TCore;

public class PlayerYaml {
	
	public File plyYml;
	public FileConfiguration plyCfg;
	
	private TCore core;
	
	public PlayerYaml(TCore core, Player ply) {
		this.core = core;
		this.plyYml = new File(core.getDataFolder() + "/playerdata/" + ply.getUniqueId().toString() + ".yml");
		this.plyCfg = YamlConfiguration.loadConfiguration(this.plyYml);
	}
	
	public PlayerYaml(TCore core, OfflinePlayer ply) {
		this.core = core;
		this.plyYml = new File(core.getDataFolder() + "/playerdata/" + ply.getUniqueId().toString() + ".yml");
		this.plyCfg = YamlConfiguration.loadConfiguration(this.plyYml);
	}
	
	public void createFile() {
		this.save();
	}
	
	public boolean setClaimed(Player ply, Location loc) {
		final String data = String.format("%s[%s[%s[%s", loc.getWorld().getName(), loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
		final List<String> claimed = this.getClaimedDoors(ply);
		
		if (!this.alreadyClaimed(ply, loc)) {
			claimed.add(data);
			this.plyCfg.set("ClaimedDoors", claimed);
			this.save();
			return true;
		} else return false;
		
	}
	
	public void removeClaimed(Player ply, Location loc) {
		final String data = String.format("%s[%s[%s[%s", loc.getWorld().getName(), loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
		final List<String> claimed = this.getClaimedDoors(ply);
		
		if (this.alreadyClaimed(ply, loc)) {
			claimed.remove(data);
			this.plyCfg.set("ClaimedDoors", claimed);
			this.save();
		}
	}
	
	public void removeClaimed(OfflinePlayer ply, Location loc) {
		final String data = String.format("%s[%s[%s[%s", loc.getWorld().getName(), loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
		final List<String> claimed = this.getClaimedDoors(ply);
		
		if (this.alreadyClaimed(ply, loc)) {
			claimed.remove(data);
			this.plyCfg.set("ClaimedDoors", claimed);
			this.save();
		}
	}
	
	public List<String> getClaimedDoors(Player ply) {
		this.plyYml = new File(core.getDataFolder() + "/playerdata/" + ply.getUniqueId().toString() + ".yml");
		this.plyCfg = YamlConfiguration.loadConfiguration(this.plyYml);
		
		return this.plyCfg.getStringList("ClaimedDoors");
	}
	
	public List<String> getClaimedDoors(OfflinePlayer ply) {
		this.plyYml = new File(core.getDataFolder() + "/playerdata/" + ply.getUniqueId().toString() + ".yml");
		this.plyCfg = YamlConfiguration.loadConfiguration(this.plyYml);
		
		return this.plyCfg.getStringList("ClaimedDoors");
	}
	
	public boolean alreadyClaimed(Player ply, Location loc) {
		final String data = String.format("%s[%s[%s[%s", loc.getWorld().getName(), loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
		final List<String> claimed = this.getClaimedDoors(ply);
		
		boolean isClaimed = false;
		
		for (String c : claimed) {
			if (data.equalsIgnoreCase(c)) {
				isClaimed = true;
				break;
			}
		}
		
		return isClaimed;
	}
	
	public boolean alreadyClaimed(OfflinePlayer ply, Location loc) {
		final String data = String.format("%s[%s[%s[%s", loc.getWorld().getName(), loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
		final List<String> claimed = this.getClaimedDoors(ply);
		
		boolean isClaimed = false;
		
		for (String c : claimed) {
			if (data.equalsIgnoreCase(c)) {
				isClaimed = true;
				break;
			}
		}
		
		return isClaimed;
	}
	
	public void save() {
		try {
			this.plyCfg.save(this.plyYml);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
