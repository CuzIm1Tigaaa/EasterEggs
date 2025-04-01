package de.cuzim1tigaaa.easter.utils.progress;

import de.cuzim1tigaaa.easter.EasterEggs;
import de.cuzim1tigaaa.easter.utils.egg.*;
import de.cuzim1tigaaa.easter.utils.reward.Reward;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

public class ProgressUtils {

	@Getter
	private static final Set<PlayerProgress> playerProgress = new HashSet<>();
	@Getter
	private static final Set<Display> progressBars = new HashSet<>();

	private final EasterEggs plugin;

	public ProgressUtils(EasterEggs plugin) {
		this.plugin = plugin;

		for(final OfflinePlayer offlinePlayer : Bukkit.getOfflinePlayers())
			fetchPlayerProgress(offlinePlayer.getUniqueId());
	}

	public void fetchPlayerProgress(UUID uuid) {
		final PlayerProgress playerProgress = getPlayerProgress(uuid);
		ProgressUtils.getPlayerProgress().add(playerProgress);
		playerProgress.getProgress().clear();
		plugin.getSqlQueries().getPlayerProgress(uuid).thenAccept(p -> playerProgress.getProgress().addAll(p));
	}

	public PlayerProgress getPlayerProgress(UUID uuid) {
		PlayerProgress progress = playerProgress.stream().filter(p -> p.getUuid().equals(uuid))
				.findFirst().orElse(null);
		if(progress == null)
			playerProgress.add(progress = new PlayerProgress(uuid));
		return progress;
	}

	public boolean hasPlayerFound(UUID uuid, Egg egg) {
		final PlayerProgress playerProgress = getPlayerProgress(uuid);
		return playerProgress.getProgress().stream().anyMatch(p -> p.getEggId().equals(egg.getUniqueId()));
	}

	public void addPlayerProgress(UUID uuid, Egg egg, Reward reward) {
		PlayerProgress playerProgress = getPlayerProgress(uuid);
		ProgressUtils.getPlayerProgress().add(playerProgress);

		Progress toAdd = new Progress(egg.getUniqueId(), new Timestamp(System.currentTimeMillis()), reward);
		playerProgress.getProgress().add(toAdd);
		displayProgress(uuid);
		plugin.getSqlQueries().addProgress(uuid, toAdd).join();
	}

	public int getFoundEggs(UUID uuid) {
		PlayerProgress playerProgress = getPlayerProgress(uuid);
		return playerProgress.getProgress().size();
	}

	public int getFoundEggsByCategory(UUID uuid, Category category) {
		PlayerProgress playerProgress = getPlayerProgress(uuid);
		Set<UUID> foundEggs = playerProgress.getProgress().stream()
				.map(Progress::getEggId)
				.filter(eggId -> EggUtils.getEggById(eggId).getCategory().equals(category))
				.collect(Collectors.toSet());

		return (int) EggUtils.getEggsByCategory(category).stream().filter(egg ->
				foundEggs.contains(egg.getUniqueId())).count();
	}

	public void displayProgress(UUID uuid) {
		Display display = progressBars.stream().filter(d -> d.getUuid().equals(uuid))
				.findFirst().orElse(null);
		if(display == null)
			progressBars.add(display = new Display(uuid));
		display.extendProgressDisplay();
	}
}