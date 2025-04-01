package de.cuzim1tigaaa.easter.utils;

import de.cuzim1tigaaa.easter.EasterEggs;
import de.cuzim1tigaaa.easter.files.Config;
import de.cuzim1tigaaa.easter.files.Paths;
import de.cuzim1tigaaa.easter.utils.egg.Egg;
import de.cuzim1tigaaa.easter.utils.egg.EggUtils;
import de.cuzim1tigaaa.easter.utils.progress.ProgressUtils;
import org.bukkit.*;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.stream.Collectors;

public class Highlight {

	private final EasterEggs plugin;
	private Integer taskId;

	public Highlight(EasterEggs plugin) {
		this.plugin = plugin;
	}

	public void startHighlight() {
		if(!Config.getConfig().getBoolean(Paths.CONFIG_HIGHLIGHT_USE))
			return;

		if(taskId != null)
			return;

		ProgressUtils progressUtils = plugin.getProgressUtils();
		final double distance = Math.pow(Config.getConfig().getDouble(Paths.CONFIG_HIGHLIGHT_DISTANCE), 2.0);
		final Particle particle = (Particle) EasterEggs.getEnumByName(Config.getConfig().getString(Paths.CONFIG_HIGHLIGHT_PARTICLE),
				Particle.class, Particle.HAPPY_VILLAGER);

		taskId = plugin.getServer().getScheduler().runTaskTimer(plugin, () -> {
			Set<Player> players = new HashSet<>(Bukkit.getOnlinePlayers());
			Set<Egg> eggs = EggUtils.getEggs().stream().filter(egg -> egg.getLocation().isWorldLoaded())
					.collect(Collectors.toSet());

			players.forEach(player -> {
				final UUID uuid = player.getUniqueId();
				eggs.stream()
						.filter(egg -> {
							Location loc = egg.getLocation();
							if(loc == null || !loc.getChunk().isLoaded())
								return false;
							if(!loc.getWorld().equals(player.getWorld()))
								return false;

							if(progressUtils.hasPlayerFound(uuid, egg))
								return false;

							return loc.distanceSquared(player.getLocation()) < distance;
						})
						.forEach(egg -> {
							Location loc = egg.getLocation().clone().add(0, 1, 0);
							player.spawnParticle(particle, loc, 10, 0, 1, 0.5, 0.5);
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