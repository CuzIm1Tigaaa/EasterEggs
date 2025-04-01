package de.cuzim1tigaaa.easter.utils.progress;

import de.cuzim1tigaaa.easter.utils.RewardType;
import de.cuzim1tigaaa.easter.utils.egg.Egg;
import de.cuzim1tigaaa.easter.utils.egg.EggUtils;
import de.cuzim1tigaaa.easter.utils.reward.ItemsReward;
import lombok.Getter;
import org.bukkit.inventory.ItemStack;

import java.util.*;

@Getter
public class PlayerProgress {

	private final UUID uuid;
	private final Set<Progress> progress;

	public PlayerProgress(UUID uuid) {
		this.uuid = uuid;
		this.progress = new HashSet<>();
	}

	public int getRewardCount(RewardType rewardType) {
		return progress.stream().filter(p ->
						p.getReward().getRewardType().equals(rewardType))
				.mapToInt(p -> p.getReward().getAmount()).sum();
	}

	public int getTotalProgress() {
		return progress.size();
	}

	public int getProgressByCategory(String categoryId) {
		Set<Egg> eggs = new HashSet<>();
		progress.forEach(p -> {
			Egg egg = EggUtils.getEggById(p.getEggId());
			if(egg != null)
				eggs.add(egg);
		});
		return (int) eggs.stream().filter(egg -> egg.getCategoryId()
				.equalsIgnoreCase(categoryId)).count();
	}

	public Map<ItemStack, Integer> getRewardCount() {
		Map<ItemStack, Integer> rewardCount = new HashMap<>();
		progress.stream().filter(p -> p.getReward() instanceof ItemsReward)
				.forEach(p -> {
					ItemsReward itemsReward = (ItemsReward) p.getReward();
					rewardCount.put(itemsReward.getItemStack(),
							rewardCount.getOrDefault(itemsReward.getItemStack(), 0) + itemsReward.getAmount());
				});
		return rewardCount;
	}
}