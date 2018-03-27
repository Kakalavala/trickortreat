package me.kakalavala.trickortreat.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.kakalavala.trickortreat.core.TCore;

public class Command_TrickOrTreat implements CommandExecutor {
	
	private TCore core;
	
	private final Material[] doors = {
			Material.ACACIA_DOOR,
			Material.BIRCH_DOOR,
			Material.DARK_OAK_DOOR,
			Material.IRON_DOOR,
			Material.JUNGLE_DOOR,
			Material.SPRUCE_DOOR,
			Material.WOOD_DOOR,
			Material.WOODEN_DOOR
	};
	
	public Command_TrickOrTreat(TCore core) {
		this.core = core;
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String command, String[] args) {
		if (!(sender instanceof Player)) {
			core.sendMessage(sender, "§cYou must be a player to perform this command!");
			return false;
		}
		
		final Player ply = (Player) sender;
		command = command.toLowerCase();
		
		if (ply.hasPermission("trickortreat.admin")) {
			if (args.length == 0) {
				this.sendHelpMessage(ply, command);
				return false;
			} else if (args.length == 1) {
				if (args[0].equalsIgnoreCase("remove"))
					return this.attemptRemove(ply);
				else {
					this.sendHelpMessage(ply, command);
					return false;
				}
			} else {
				if (args[0].equalsIgnoreCase("register")) {
					final List<String> cmds = new ArrayList<String>();
					
					for (String s : args)
						cmds.add(s);
					
					cmds.remove(args[0]);
					
					return this.attemptRegister(ply, cmds);
				}
				else {
					this.sendHelpMessage(ply, command);
					return false;
				}
			}
		} else {
			core.sendMessage(sender, "§cYou don't have permission to do that!");
			return false;
		}
	}
	
	private void sendHelpMessage(Player ply, String command) {
		final String[] msg = {
				String.format("§5 * §6/%s register <cmd|cmd|cmd|...>", command),
				String.format("§d * §f/%s remove", command)
		};
		
		core.sendMessage(ply, "§cTry:");
		core.sendMessage(ply, msg);
	}
	
	private boolean isLookingAtDoor(Player ply) {
		final Block bl = ply.getTargetBlock(null, 100);
		boolean isDoor = false;
		
		for (Material d : doors) {
			if (bl.getType().equals(d)) {
				isDoor = true;
				break;
			}
		}
		
		return isDoor;
	}
	
	private Location getDoorLocation(Player ply) {
		final Block bl = ply.getTargetBlock(null, 100);
		
		if (this.isLookingAtDoor(ply))
			return bl.getLocation();
		else return null;
	}
	
	private boolean attemptRegister(Player ply, List<String> cmds) {
		if (this.isLookingAtDoor(ply)) {
			final Location loc = this.getDoorLocation(ply);
			
			if (core.door.registerDoor(loc, cmds)) {
				core.sendMessage(ply, "§aDoor registered successfully.");
				return true;
			} else {
				core.sendMessage(ply, "§cFailed to register door!");
				return false;
			}
		} else {
			core.sendMessage(ply, "§cYou're not looking at a door!");
			return false;
		}
	}
	
	private boolean attemptRemove(Player ply) {
		if (this.isLookingAtDoor(ply)) {
			if (core.door.removeDoor(this.getDoorLocation(ply))) {
				core.sendMessage(ply, "§aDoor removed successfully.");
				return true;
			} else {
				core.sendMessage(ply, "§cFailed to remove door!");
				return false;
			}
		} else {
			core.sendMessage(ply, "§cYou're not looking at a door!");
			return false;
		}
	}

}
