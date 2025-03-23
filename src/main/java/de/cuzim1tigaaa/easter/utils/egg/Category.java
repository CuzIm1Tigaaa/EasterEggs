package de.cuzim1tigaaa.easter.utils.egg;

import de.cuzim1tigaaa.easter.utils.RewardType;
import de.cuzim1tigaaa.easter.utils.reward.ItemsReward;
import de.cuzim1tigaaa.easter.utils.reward.Reward;
import de.cuzim1tigaaa.guimanager.CustomHead;
import lombok.Getter;
import org.bukkit.Material;

import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Getter
public class Category implements Comparable<Category> {

	private final String id;
	private final String name;
	private final String customHeadUrl;
	private final transient CustomHead customHead;
	private final RewardType rewardType;
	private final int min, max;
	private final List<Material> materials;

	public Category(String id, String name, String customHeadUrl, RewardType rewardType, int min, int max, List<Material> materials) {
		this.id = id;
		this.name = name;
		this.customHeadUrl = customHeadUrl;
		this.customHead = new CustomHead("", customHeadUrl);
		this.rewardType = rewardType;
		this.min = min;
		this.max = max;
		this.materials = materials;
	}

	public Reward createReward() {
		ThreadLocalRandom random = ThreadLocalRandom.current();
		switch(rewardType) {
			case TALER, COINS -> {
				return new Reward(rewardType, random.nextInt(min, max + 1));
			}
			case ITEMS -> {
				return new ItemsReward(materials.get(random.nextInt(materials.size())),
						random.nextInt(min, max + 1));
			}
		}
		return null;
	}

	@Override
	public int compareTo(Category o) {
		return Comparator.comparing(Category::getId)
				.thenComparing(Category::getName)
				.compare(this, o);
	}
}