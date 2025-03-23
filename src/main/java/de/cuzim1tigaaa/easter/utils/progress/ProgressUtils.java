package de.cuzim1tigaaa.easter.utils.progress;

import de.cuzim1tigaaa.easter.utils.egg.Category;
import de.cuzim1tigaaa.easter.utils.egg.Egg;
import de.cuzim1tigaaa.easter.utils.egg.EggUtils;
import de.cuzim1tigaaa.easter.utils.reward.Reward;
import lombok.Getter;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public class ProgressUtils {

	@Getter
	private static final Set<PlayerProgress> playerProgress = new HashSet<>();

	public static PlayerProgress getPlayerProgressByUUID(UUID uuid) {
		return playerProgress.stream().filter(p -> p.getUuid().equals(uuid))
				.findFirst().orElse(null);
	}

	public static boolean hasPlayerProgress(PlayerProgress progress, Egg egg) {
		return progress.getProgress().stream()
				.anyMatch(p -> p.getEggId().equals(egg.getUniqueId()));
	}

	public static void addPlayerProgress(PlayerProgress progress, Egg egg, Reward reward) {
		progress.getProgress().add(new Progress(
				egg.getUniqueId(), new Timestamp(System.currentTimeMillis()), reward));
	}

	public static int getProgress(PlayerProgress progress) {
		Set<UUID> foundEggs = progress.getProgress().stream()
				.map(Progress::getEggId)
				.collect(Collectors.toSet());

		return (int) EggUtils.getEggs().stream().filter(egg ->
				foundEggs.contains(egg.getUniqueId())).count();
	}
	public static int getProgressByCategory(PlayerProgress progress, Category category) {
		Set<UUID> foundEggs = progress.getProgress().stream()
				.map(Progress::getEggId)
				.filter(eggId -> EggUtils.getEggById(eggId).getCategory().equals(category))
				.collect(Collectors.toSet());

		return (int) EggUtils.getEggsByCategory(category).stream().filter(egg ->
				foundEggs.contains(egg.getUniqueId())).count();
	}
}