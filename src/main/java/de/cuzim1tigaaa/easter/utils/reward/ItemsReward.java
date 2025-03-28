package de.cuzim1tigaaa.easter.utils.reward;

import de.cuzim1tigaaa.colorlib.ColorLib;
import de.cuzim1tigaaa.easter.files.Messages;
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
		return ColorLib.format("&7Du hast %dx %s gefunden!",
				amount, Messages.capitalizeFully(material.name()));
	}
}
