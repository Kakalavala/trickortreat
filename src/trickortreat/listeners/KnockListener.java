package me.kakalavala.trickortreat.listeners;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import me.kakalavala.trickortreat.assets.PlayerYaml;
import me.kakalavala.trickortreat.core.TCore;

public class KnockListener implements Listener {
	
	private TCore core;
	private PlayerYaml pYml;
	
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
	
	public KnockListener(TCore core) {
		this.core = core;
	}
	
	private boolean isDoor(Player ply, Block bl) {
		boolean isDoor = false;
		
		for (Material d : doors) {
			if (bl.getType().equals(d)) {
				isDoor = true;
				break;
			}
		}
		
		return isDoor;
	}
	
	private void executeCommands(Player ply, List<String> cmds) {
		for (String c : cmds)
			Bukkit.dispatchCommand(Bukkit.getConsoleSender(), c.trim().substring(1).replace("%player%", ply.getName()));
	}
	
	@EventHandler
	public void onKnock(PlayerInteractEvent e) {
		try {
			final Player ply = e.getPlayer();
			final Action act = e.getAction();
			final Block bl = e.getClickedBlock();
			final Location loc = bl.getLocation();
			
			this.pYml = new PlayerYaml(core, ply);
			
			if (this.isDoor(ply, bl) && act.equals(Action.LEFT_CLICK_BLOCK)) {
				if (core.door.doorExists(loc)) {
					if (pYml.alreadyClaimed(ply, loc)) {
						if (core.useMessages())
							core.sendMessage(ply, core.getAlreadyClaimedMessage());
						e.setCancelled(true);
						return;
					} else {
						if (ply.getGameMode().equals(GameMode.CREATIVE))
							bl.getState().update();
						
						pYml.setClaimed(ply, loc);
						this.executeCommands(ply, core.door.getCommands(loc));
						
						if (core.useSound())
							ply.playSound(loc, core.getSound(), core.getVolume(), core.getPitch());
						
						e.setCancelled(true);
						return;
					}
				}
			}
		} catch (Exception exc) {}
	}

}
