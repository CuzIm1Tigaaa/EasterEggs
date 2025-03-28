package de.cuzim1tigaaa.easter.utils.reward;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import de.cuzim1tigaaa.colorlib.ColorLib;
import de.cuzim1tigaaa.easter.utils.RewardType;
import lombok.Getter;

import java.util.Base64;

@Getter
public class Reward {

	private static final Gson gson = new GsonBuilder()
			.registerTypeAdapter(Reward.class, new RewardDeserializer())
			.create();

	protected final RewardType rewardType;
	protected final int amount;

	public Reward(RewardType rewardType, int amount) {
		this.rewardType = rewardType;
		this.amount = amount;
	}

	public static Reward fromBase64(String base64) {
		String json = new String(Base64.getDecoder().decode(base64));
		return gson.fromJson(json, Reward.class);
	}

	public String toBase64() {
		String json = gson.toJson(this);
		return Base64.getEncoder().encodeToString(json.getBytes());
	}

	@Override
	public String toString() {
		return ColorLib.format("&7Du hast %d %s gefunden!", amount, rewardType.getName());
	}
}
