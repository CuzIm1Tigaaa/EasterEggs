package de.cuzim1tigaaa.easter.utils.reward;

import de.cuzim1tigaaa.easter.utils.RewardType;
import lombok.Getter;
import org.bukkit.Material;

@Getter
public class ItemsReward extends Reward {

	private final Material material;

	public ItemsReward(Material material, int amount) {
		super(RewardType.ITEMS, amount);
		this.material = material;
	}

	public String getMessage() {
		return String.format("Du hast %d %s gefunden!", amount, material.name());
	}
}
