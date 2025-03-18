package de.cuzim1tigaaa.easter.utils;

import de.cuzim1tigaaa.easter.EasterEggs;
import de.cuzim1tigaaa.easter.utils.egg.Egg;
import de.cuzim1tigaaa.easter.utils.egg.EggUtils;
import de.cuzim1tigaaa.easter.utils.progress.PlayerProgress;
import de.cuzim1tigaaa.easter.utils.progress.ProgressUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class Highlight {

	private final EasterEggs plugin;
	private Integer taskId;

	public Highlight(EasterEggs plugin) {
		this.plugin = plugin;
	}

	public void startHighlight() {
		if(taskId != null)
			return;

		taskId = plugin.getServer().getScheduler().runTaskTimer(plugin, () -> {
			Set<Player> players = new HashSet<>(Bukkit.getOnlinePlayers());
			Set<Egg> eggs = EggUtils.getEggs().stream().filter(egg -> egg.getLocation().isWorldLoaded())
					.collect(Collectors.toSet());

			players.forEach(player -> {
				eggs.stream()
						.filter(egg -> {
							Location loc = egg.getLocation();
							if(loc == null || !loc.getChunk().isLoaded())
								return false;
							if(!loc.getWorld().equals(player.getWorld()))
								return false;
							if(loc.distanceSquared(player.getLocation()) < 50)
								return false;

							PlayerProgress progress = ProgressUtils.getPlayerProgressByUUID(player.getUniqueId());
							return ProgressUtils.hasPlayerProgress(progress, egg);
						})
						.forEach(egg -> {
							Location loc = egg.getLocation().add(0, .5, 0);
							player.spawnParticle(Particle.HAPPY_VILLAGER, loc, 1, 0, 1, 0, 0);
						});
			});
		}, 100L, 15L).getTaskId();
	}

	public void endHighlight() {
		if(taskId == null)
			return;

		Bukkit.getScheduler().cancelTask(taskId);
		taskId = null;
	}

}
