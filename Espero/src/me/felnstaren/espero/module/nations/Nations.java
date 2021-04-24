package me.felnstaren.espero.module.nations;

import java.util.ArrayList;

import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import me.felnstaren.espero.Espero;
import me.felnstaren.espero.config.EsperoPlayer;
import me.felnstaren.espero.module.IModule;
import me.felnstaren.espero.module.nations.chat.NationPlayerChatManager;
import me.felnstaren.espero.module.nations.claim.ClaimBoard;
import me.felnstaren.espero.module.nations.command.nation.NationCommandMaster;
import me.felnstaren.espero.module.nations.listener.ClaimInfoListener;
import me.felnstaren.espero.module.nations.listener.CofferListener;
import me.felnstaren.espero.module.nations.listener.NationRelationCombatListener;
import me.felnstaren.espero.module.nations.listener.PlayerClaimInteractHandler;
import me.felnstaren.espero.module.nations.nation.Nation;
import me.felnstaren.espero.module.nations.nation.NationPlayerRank;
import me.felnstaren.espero.module.nations.nation.NationRegistry;
import me.felnstaren.espero.module.nations.nation.NationRelation;
import me.felnstaren.felib.chat.Color;
import me.felnstaren.felib.logger.Level;

@SuppressWarnings("deprecation")
public class Nations implements IModule {
	
	BukkitRunnable save_task = new BukkitRunnable() {
		public void run() {
			NationRegistry.inst().save();
			ClaimBoard.inst().save();
			Espero.LOGGER.log(Level.INFO, "Saved nations and claims");
		}
	};
	
	public void onEnable(JavaPlugin plugin) {
		NationRegistry.init();
		ClaimBoard.init();
		NationPlayerChatManager.init(plugin);

		save_task.runTaskTimer(plugin, 100, 10000);
		
		plugin.getCommand("nation").setExecutor(new NationCommandMaster());
		
		PluginManager pm = plugin.getServer().getPluginManager();
		pm.registerEvents(new ClaimInfoListener(), plugin);
		pm.registerEvents(new PlayerClaimInteractHandler(), plugin);
		pm.registerEvents(new CofferListener(), plugin);
		pm.registerEvents(new NationRelationCombatListener(), plugin);
	}

	public void onDisable(JavaPlugin plugin) {
		save_task.cancel();
		NationRegistry.inst().save();
		ClaimBoard.inst().save();
	}
	
	
	
	/** |----------------------------------------------------------|
	 *  |     Centralized Nation/Town/Player Utility Commands      |
	 *  |----------------------------------------------------------| */ 
	public static void setNationRank(EsperoPlayer player, String value) {
		if(value == null || player.getNation() == null) player.setNationRank(null);
		else player.setNationRank(player.getNation().getRank(value));
	}
	
	public static void setNationRank(EsperoPlayer player, NationPlayerRank value) {
		if(value == null) player.setNationRank(null);
		else player.setNationRank(value);
	}
	
	public static void setNation(EsperoPlayer player, Nation nation) {
		if(player.getNation() != null) 
			player.getNation().getMembers().remove(player.getUniqueId());
		
		if(nation == null) {
			if(player.getNation() == null) return;
			player.setNationRank(null);
			player.setNation(null);
		} else {
			nation.getInvites().remove(player.getUniqueId());
			nation.getMembers().add(player.getUniqueId());
			player.setNation(nation);
			player.setNationRank(nation.getLowestRank());
		}
	}
	
	public static boolean nationSetRelation(Nation requester, Nation requestee, NationRelation relation) {
		if(relation.outranks(requestee.getRelationRequest(requester.getID()))) return false;
		setRelation(requester, requestee, relation);
		return true;
	}
	
	public static void setRelation(Nation nation1, Nation nation2, NationRelation relation) {
		nation1.setRelation(nation2.getID(), relation);
		nation2.setRelation(nation1.getID(), relation);
		nation1.broadcast(Color.GREEN + "You are now " + relation.name() + " with " + nation2.getDisplayName());
		nation2.broadcast(Color.GREEN + "You are now " + relation.name() + " with " + nation1.getDisplayName());
	}

	public static void requestRelation(Nation sender, Nation receiver, NationRelation relation) {
		if(relation.outranks(receiver.getRelation(sender.getID())) && relation.outranks(receiver.getRelationRequest(sender.getID()))) {
			sender.broadcast(Color.GREEN + "You have requested to " + relation.name() + " with " + receiver.getDisplayName());
			receiver.broadcast(Color.GREEN + sender.getDisplayName() + " has requested to " + relation.name());
			receiver.setRelationRequest(sender.getID(), relation);
		}
		else setRelation(sender, receiver, relation);
	}
	
	public static void disband(Nation nation) {
		ArrayList<EsperoPlayer> players = nation.getLoadedMembers();
		for(EsperoPlayer player : players) player.setNation(null);
		NationRegistry.inst().unregister(nation.getID());
		Espero.LOADER.delete(nation.getPath());
	}
	
	public static boolean isPermitted(EsperoPlayer player, Nation nation, String permission) {
		if(player.getNation() == null) return false;
		if(!player.getNation().equals(nation)) return false;   //TODO: Relation-Specific Permissions
		return player.getNationRank().isPermitted(permission);
	}
	
	public static void invite(Nation nation, EsperoPlayer player) {
		if(nation.getInvites().contains(player.getUniqueId())) return;
		nation.getInvites().add(player.getUniqueId());
	}
}
