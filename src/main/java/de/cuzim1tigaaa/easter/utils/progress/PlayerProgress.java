package de.cuzim1tigaaa.easter.utils.progress;

import de.cuzim1tigaaa.easter.utils.RewardType;
import de.cuzim1tigaaa.easter.utils.reward.ItemsReward;
import lombok.Getter;
import org.bukkit.Material;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
public class PlayerProgress {

	private final UUID uuid;
	private final Set<Progress> progress;

	public PlayerProgress(UUID uuid, Set<Progress> progress) {
		this.uuid = uuid;
		this.progress = progress;
	}

	public PlayerProgress(UUID uuid) {
		this.uuid = uuid;
		this.progress = new HashSet<>();
	}

	public int getRewardCount(RewardType rewardType) {
		return progress.stream().filter(p ->
				p.getReward().getRewardType().equals(rewardType))
				.mapToInt(p -> p.getReward().getAmount()).sum();
	}

	public int getRewardCount(Material material) {
		return progress.stream().filter(p -> {
			if(!(p.getReward() instanceof ItemsReward itemsReward))
				return false;

			return itemsReward.getMaterial().equals(material);
		}).mapToInt(p -> p.getReward().getAmount()).sum();
	}
}
