package de.cuzim1tigaaa.easter.listeners;

import de.cuzim1tigaaa.easter.EasterEggs;
import de.cuzim1tigaaa.easter.files.Messages;
import de.cuzim1tigaaa.easter.files.Paths;
import de.cuzim1tigaaa.easter.utils.egg.Egg;
import de.cuzim1tigaaa.easter.utils.egg.EggUtils;
import de.cuzim1tigaaa.easter.utils.progress.PlayerProgress;
import de.cuzim1tigaaa.easter.utils.progress.ProgressUtils;
import de.cuzim1tigaaa.easter.utils.reward.Reward;
import lombok.Getter;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class EasterEvents implements Listener {

	@Getter
	private static final Set<UUID> removeEggs = new HashSet<>();

	public EasterEvents(EasterEggs plugin) {
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
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
			player.sendMessage("Egg removed");
			player.sendMessage("Egg count: " + EggUtils.getEggs().size());
			player.sendMessage("Category count: " + EggUtils.getEggsByCategory(egg.getCategory()).size());
			return;
		}

		PlayerProgress progress = ProgressUtils.getPlayerProgressByUUID(uuid);
		if(progress == null) {
			progress = new PlayerProgress(uuid);
			ProgressUtils.getPlayerProgress().add(progress);
		}

		if(ProgressUtils.hasPlayerProgress(progress, egg)) {
			player.spigot().sendMessage(ChatMessageType.ACTION_BAR,
					new TextComponent(messages.get(Paths.MESSAGES_EGG_ALREADY)));
			return;
		}

		Reward reward = egg.getCategory().createReward();
		ProgressUtils.addPlayerProgress(progress, egg, reward);
		player.sendMessage(reward.toString());
		player.sendMessage(String.format("%d / %d Eier gefunden",
				ProgressUtils.getProgressByCategory(progress, egg.getCategory()),
				EggUtils.getEggsByCategory(egg.getCategory()).size()));
		player.sendMessage(String.format("%d / %d Eier der Kategorie %s gefunden",
				ProgressUtils.getProgress(progress),
				EggUtils.getEggs().size(), egg.getCategory().getName()));
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