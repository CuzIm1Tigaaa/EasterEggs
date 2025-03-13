package de.cuzim1tigaaa.easter.utils.reward;

import de.cuzim1tigaaa.easter.utils.RewardType;
import lombok.Getter;

@Getter
public class Reward {

	protected final RewardType rewardType;
	protected final int amount;

	public Reward(RewardType rewardType, int amount) {
		this.rewardType = rewardType;
		this.amount = amount;
	}

	@Override
	public String toString() {
		return String.format("Du hast %d %s gefunden!", amount, rewardType.getName());
	}
}
