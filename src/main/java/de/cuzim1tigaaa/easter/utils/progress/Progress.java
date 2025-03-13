package de.cuzim1tigaaa.easter.utils.progress;

import de.cuzim1tigaaa.easter.utils.reward.Reward;
import lombok.Getter;

import java.sql.Timestamp;
import java.util.UUID;

@Getter
public class Progress {

	private final UUID eggId;
	private final Timestamp timestamp;
	private final Reward reward;

	public Progress(UUID eggId, Timestamp timestamp, Reward reward) {
		this.eggId = eggId;
		this.timestamp = timestamp;
		this.reward = reward;
	}
}
