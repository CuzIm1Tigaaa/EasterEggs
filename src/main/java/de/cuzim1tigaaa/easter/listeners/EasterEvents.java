package de.cuzim1tigaaa.easter.listeners;

import de.cuzim1tigaaa.easter.EasterEggs;
import de.cuzim1tigaaa.easter.files.Messages;
import de.cuzim1tigaaa.easter.files.Paths;
import de.cuzim1tigaaa.easter.utils.egg.Egg;
import de.cuzim1tigaaa.easter.utils.egg.EggUtils;
import de.cuzim1tigaaa.easter.utils.progress.ProgressUtils;
import de.cuzim1tigaaa.easter.utils.reward.Reward;
import lombok.Getter;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.*;

public class EasterEvents implements Listener {

	@Getter
	private static final Set<UUID> removeEggs = new HashSet<>();

	private final EasterEggs plugin;
	private final Set<UUID> cooldown = new HashSet<>();
	private final ProgressUtils progressUtils;

	public EasterEvents(EasterEggs plugin) {
		this.plugin = plugin;
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
		this.progressUtils = plugin.getProgressUtils();
	}

	@EventHandler
	public void rightClickEgg(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		Block block = event.getClickedBlock();
		if(block == null || block.getType() != Material.PLAYER_HEAD)
			return;

		Egg egg = EggUtils.getEggByLocation(block.getLocation());
		if(egg == null)
			return;

		UUID uuid = player.getUniqueId();
		Messages messages = Messages.getMessages();

		if(removeEggs.contains(uuid)) {
			EggUtils.getEggs().remove(egg);
			removeEggs.remove(uuid);

			messages.send(player, Paths.COMMANDS_REMOVE_EGG);
			player.sendMessage("Egg count: " + EggUtils.getEggs().size());
			player.sendMessage("Category count: " + EggUtils.getEggsByCategory(egg.getCategory()).size());
			return;
		}

		if(cooldown.contains(uuid))
			return;
		this.cooldown(player);

		if(!progressUtils.hasPlayerFound(uuid, egg)) {
			Reward reward = egg.getCategory().createReward();
			progressUtils.addPlayerProgress(uuid, egg, reward);
			player.sendMessage(reward.toString());
			return;
		}

		player.spigot().sendMessage(ChatMessageType.ACTION_BAR,
				new TextComponent(messages.get(Paths.MESSAGES_EGG_ALREADY)));
	}

	private void cooldown(Player player) {
		UUID uuid = player.getUniqueId();
		cooldown.add(uuid);
		Bukkit.getScheduler().runTaskLater(plugin, () -> cooldown.remove(uuid), 10L);
	}

	@EventHandler
	public void breakEgg(BlockBreakEvent event) {
		Block block = event.getBlock();
		if(block.getType() != Material.PLAYER_HEAD)
			return;

		Egg egg = EggUtils.getEggByLocation(block.getLocation());
		if(egg == null)
			return;

		event.setCancelled(true);
	}
}